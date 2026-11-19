package dmirzayants.voidvpn.feature.servers

import dmirzayants.voidvpn.core.MainDispatcherRule
import dmirzayants.voidvpn.feature.servers.presentation.ServersEffect
import dmirzayants.voidvpn.feature.servers.presentation.ServersFilter
import dmirzayants.voidvpn.feature.servers.presentation.ServersIntent
import dmirzayants.voidvpn.feature.servers.presentation.ServersViewModel
import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.feature.vpn.domain.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ServersViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private class TestServerRepository(initial: List<Server>) : ServerRepository {
        private val list = MutableStateFlow(initial)
        private val selected = MutableStateFlow<String?>(null)
        var pingRefreshed = false

        override val servers: Flow<List<Server>> = list
        override val selectedId: Flow<String?> = selected
        override val activeServer: Flow<Server?> = combine(list, selected) { all, id ->
            if (id == null) all.filter { it.pingMs != null }.minByOrNull { it.pingMs!! }
            else all.firstOrNull { it.id == id }
        }

        override suspend fun select(id: String?) {
            selected.value = id
        }

        override suspend fun toggleFavorite(id: String) {
            list.update { all -> all.map { if (it.id == id) it.copy(favorite = !it.favorite) else it } }
        }

        override suspend fun refreshPing() {
            pingRefreshed = true
        }
    }

    private val servers = listOf(
        Server("nl", "nl", "Нидерланды", "Амстердам", pingMs = 40, load = 30, favorite = true),
        Server("de", "de", "Германия", "Франкфурт", pingMs = 60, load = 50),
        Server("us", "us", "США", "Нью-Йорк", pingMs = 120, load = 20, favorite = true),
    )

    private lateinit var repository: TestServerRepository
    private lateinit var vm: ServersViewModel

    @Before
    fun setUp() {
        repository = TestServerRepository(servers)
        vm = ServersViewModel(repository)
    }

    @Test
    fun `initial state lists all servers in auto mode with the fastest as best`() {
        val state = vm.state.value
        assertEquals(3, state.servers.size)
        assertEquals(3, state.totalCount)
        assertEquals(2, state.favoritesCount)
        assertTrue(state.isAuto)
        assertEquals("Нидерланды", state.bestServerName)
    }

    @Test
    fun `search matches country and city ignoring case`() {
        vm.onIntent(ServersIntent.QueryChanged("герм"))
        assertEquals(listOf("de"), vm.state.value.servers.map { it.id })
        vm.onIntent(ServersIntent.QueryChanged("нью"))
        assertEquals(listOf("us"), vm.state.value.servers.map { it.id })
    }

    @Test
    fun `favorites filter keeps only favorites`() {
        vm.onIntent(ServersIntent.FilterChanged(ServersFilter.FAVORITES))
        assertEquals(listOf("nl", "us"), vm.state.value.servers.map { it.id })
    }

    @Test
    fun `toggling favorite updates counters`() {
        vm.onIntent(ServersIntent.FavoriteClicked("de"))
        assertEquals(3, vm.state.value.favoritesCount)
        vm.onIntent(ServersIntent.FavoriteClicked("nl"))
        assertEquals(2, vm.state.value.favoritesCount)
    }

    @Test
    fun `choosing a server selects it and navigates home`() = runTest {
        vm.onIntent(ServersIntent.ServerClicked("us"))
        assertEquals("us", vm.state.value.selectedId)
        assertFalse(vm.state.value.isAuto)
        assertEquals(ServersEffect.NavigateToHome, vm.effects.first())
    }

    @Test
    fun `auto clears the selection`() = runTest {
        vm.onIntent(ServersIntent.ServerClicked("us"))
        vm.onIntent(ServersIntent.AutoClicked)
        assertNull(vm.state.value.selectedId)
        assertTrue(vm.state.value.isAuto)
    }

    @Test
    fun `refresh asks repository to measure ping`() {
        vm.onIntent(ServersIntent.RefreshPing)
        assertTrue(repository.pingRefreshed)
    }
}

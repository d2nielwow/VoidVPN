package dmirzayants.voidvpn.feature.account

import dmirzayants.voidvpn.core.MainDispatcherRule
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import dmirzayants.voidvpn.feature.account.presentation.importsub.ImportEffect
import dmirzayants.voidvpn.feature.account.presentation.importsub.ImportIntent
import dmirzayants.voidvpn.feature.account.presentation.importsub.ImportViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ImportViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private class RecordingRepository : AccountRepository {
        override val accessKey: Flow<String?> = flowOf(null)
        override val hasAccount: Flow<Boolean> = flowOf(false)
        var imported: String? = null

        override suspend fun createAccount(): String = ""
        override suspend fun importSubscription(link: String) {
            imported = link
        }
        override suspend fun login(key: String): Boolean = false
    }

    private val repository = RecordingRepository()
    private val vm = ImportViewModel(repository)

    @Test
    fun `clipboard link is detected when input is empty`() {
        vm.onIntent(ImportIntent.ClipboardRead("vless://id@host.net:443"))
        assertTrue(vm.state.value.clipboardDetected)
        assertEquals("host.net", vm.state.value.detected?.host)
    }

    @Test
    fun `typed link takes priority over clipboard`() {
        vm.onIntent(ImportIntent.ClipboardRead("vless://id@clip.net:443"))
        vm.onIntent(ImportIntent.InputChanged("hy2://pass@typed.net:443"))
        assertFalse(vm.state.value.clipboardDetected)
        assertEquals("typed.net", vm.state.value.detected?.host)
    }

    @Test
    fun `import with no valid link shows error and saves nothing`() {
        vm.onIntent(ImportIntent.InputChanged("not a link"))
        vm.onIntent(ImportIntent.ImportClicked)
        assertTrue(vm.state.value.error)
        assertNull(repository.imported)
    }

    @Test
    fun `typing clears the error`() {
        vm.onIntent(ImportIntent.ImportClicked)
        assertTrue(vm.state.value.error)
        vm.onIntent(ImportIntent.InputChanged("v"))
        assertFalse(vm.state.value.error)
    }

    @Test
    fun `import of a valid link is saved and navigates home`() = runTest {
        vm.onIntent(ImportIntent.InputChanged("https://panel.example.com/sub/abc"))
        vm.onIntent(ImportIntent.ImportClicked)
        assertEquals("https://panel.example.com/sub/abc", repository.imported)
        assertEquals(ImportEffect.NavigateToHome, vm.effects.first())
    }

    @Test
    fun `back produces navigate back`() = runTest {
        vm.onIntent(ImportIntent.BackClicked)
        assertEquals(ImportEffect.NavigateBack, vm.effects.first())
    }
}

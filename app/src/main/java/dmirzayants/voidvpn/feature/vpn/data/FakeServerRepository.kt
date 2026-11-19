package dmirzayants.voidvpn.feature.vpn.data

import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.feature.vpn.domain.ServerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeServerRepository @Inject constructor() : ServerRepository {

    private val _servers = MutableStateFlow(initialServers())
    private val _selectedId = MutableStateFlow<String?>(null)

    override val servers: Flow<List<Server>> = _servers
    override val selectedId: Flow<String?> = _selectedId

    override val activeServer: Flow<Server?> =
        combine(_servers, _selectedId) { list, id ->
            if (id == null) {
                list.filter { it.available && it.pingMs != null }.minByOrNull { it.pingMs!! }
                    ?: list.firstOrNull { it.available }
            } else {
                list.firstOrNull { it.id == id }
            }
        }

    override suspend fun select(id: String?) {
        _selectedId.value = id
    }

    override suspend fun toggleFavorite(id: String) {
        _servers.update { list ->
            list.map { if (it.id == id) it.copy(favorite = !it.favorite) else it }
        }
    }

    override suspend fun refreshPing() {
        _servers.update { list -> list.map { if (it.available) it.copy(pingMs = null) else it } }
        delay(1_200)
        _servers.update { list ->
            list.map {
                if (it.available) it.copy(pingMs = basePing(it.id) + Random.nextInt(-4, 5)) else it
            }
        }
    }

    private fun basePing(id: String): Int = when (id) {
        "fi" -> 29
        "nl" -> 38
        "de" -> 44
        "tr" -> 71
        "jp" -> 96
        "us" -> 132
        else -> 60
    }

    private fun initialServers() = listOf(
        Server("fi", "fi", "Финляндия", "Хельсинки", 29, 40, favorite = true),
        Server("nl", "nl", "Нидерланды", "Амстердам · 3 сервера", 38, 65, favorite = true),
        Server("de", "de", "Германия", "Франкфурт", 44, 50),
        Server("tr", "tr", "Турция", "Стамбул", 71, 30),
        Server("jp", "jp", "Япония", "Токио", null, 0),
        Server("us", "us", "США", "Нью-Йорк", 132, 80),
        Server("kz", "kz", "Казахстан", "Алматы · недоступен", null, 0, available = false),
    )
}

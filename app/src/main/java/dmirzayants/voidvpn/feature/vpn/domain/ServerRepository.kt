package dmirzayants.voidvpn.feature.vpn.domain

import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    val servers: Flow<List<Server>>

    val selectedId: Flow<String?>

    val activeServer: Flow<Server?>

    suspend fun select(id: String?)

    suspend fun toggleFavorite(id: String)

    suspend fun refreshPing()
}

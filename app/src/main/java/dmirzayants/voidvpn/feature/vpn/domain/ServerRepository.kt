package dmirzayants.voidvpn.feature.vpn.domain

import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    val servers: Flow<List<Server>>

    /** null означает режим «Авто». */
    val selectedId: Flow<String?>

    /** Выбранный сервер или (в режиме «Авто») лучший по пингу. */
    val activeServer: Flow<Server?>

    suspend fun select(id: String?)

    suspend fun toggleFavorite(id: String)

    suspend fun refreshPing()
}

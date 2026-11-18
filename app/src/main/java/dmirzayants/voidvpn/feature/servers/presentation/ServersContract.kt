package dmirzayants.voidvpn.feature.servers.presentation

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.feature.vpn.domain.Server

enum class ServersFilter { ALL, FAVORITES }

data class ServersState(
    val query: String = "",
    val filter: ServersFilter = ServersFilter.ALL,
    val servers: List<Server> = emptyList(),
    val totalCount: Int = 0,
    val favoritesCount: Int = 0,
    val isAuto: Boolean = true,
    val selectedId: String? = null,
    val bestServerName: String = "",
) : UiState

sealed interface ServersIntent : UiIntent {
    data class QueryChanged(val value: String) : ServersIntent
    data class FilterChanged(val filter: ServersFilter) : ServersIntent
    data object AutoClicked : ServersIntent
    data class ServerClicked(val id: String) : ServersIntent
    data class FavoriteClicked(val id: String) : ServersIntent
    data object RefreshPing : ServersIntent
}

sealed interface ServersEffect : UiEffect {
    data object NavigateToHome : ServersEffect
}

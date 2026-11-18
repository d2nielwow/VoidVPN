package dmirzayants.voidvpn.feature.servers.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.vpn.domain.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServersViewModel @Inject constructor(
    private val repository: ServerRepository
) : MviViewModel<ServersState, ServersIntent, ServersEffect>(ServersState()) {

    private val query = MutableStateFlow("")
    private val filter = MutableStateFlow(ServersFilter.ALL)

    init {
        viewModelScope.launch {
            combine(
                repository.servers,
                repository.selectedId,
                repository.activeServer,
                query,
                filter,
            ) { all, selectedId, active, q, f ->
                val visible = all
                    .filter { f == ServersFilter.ALL || it.favorite }
                    .filter {
                        q.isBlank() ||
                            it.country.contains(q, ignoreCase = true) ||
                            it.city.contains(q, ignoreCase = true)
                    }
                ServersState(
                    query = q,
                    filter = f,
                    servers = visible,
                    totalCount = all.size,
                    favoritesCount = all.count { it.favorite },
                    isAuto = selectedId == null,
                    selectedId = selectedId,
                    bestServerName = active?.country.orEmpty(),
                )
            }.collect { newState -> setState { newState } }
        }
    }

    override fun onIntent(intent: ServersIntent) {
        when (intent) {
            is ServersIntent.QueryChanged -> query.value = intent.value
            is ServersIntent.FilterChanged -> filter.value = intent.filter
            ServersIntent.AutoClicked -> choose(null)
            is ServersIntent.ServerClicked -> choose(intent.id)
            is ServersIntent.FavoriteClicked ->
                viewModelScope.launch { repository.toggleFavorite(intent.id) }
            ServersIntent.RefreshPing -> viewModelScope.launch { repository.refreshPing() }
        }
    }

    private fun choose(id: String?) {
        viewModelScope.launch {
            repository.select(id)
            sendEffect(ServersEffect.NavigateToHome)
        }
    }
}

package dmirzayants.voidvpn.feature.home.presentation

import android.content.Context
import android.net.VpnService
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.vpn.domain.ProtocolRepository
import dmirzayants.voidvpn.feature.vpn.domain.ServerRepository
import dmirzayants.voidvpn.feature.vpn.domain.VpnController
import dmirzayants.voidvpn.feature.vpn.domain.VpnStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vpn: VpnController,
    private val servers: ServerRepository,
    private val protocols: ProtocolRepository,
    @ApplicationContext private val context: Context,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    init {
        val ticker = flow {
            while (true) {
                emit(Unit)
                delay(1_000)
            }
        }
        viewModelScope.launch {
            combine(
                vpn.status,
                servers.activeServer,
                servers.selectedId,
                protocols.protocol,
                ticker,
            ) { status, server, selectedId, protocol, _ ->
                val elapsed = (status as? VpnStatus.Connected)
                    ?.let { (System.currentTimeMillis() - it.sinceMillis) / 1_000 }
                    ?: 0L
                HomeState(
                    status = status,
                    server = server,
                    isAuto = selectedId == null,
                    protocol = protocol,
                    elapsedSeconds = elapsed,
                    showBanner = false,
                )
            }.collect { newState -> setState { newState } }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ToggleClicked -> onToggle()
            HomeIntent.ServerClicked -> sendEffect(HomeEffect.NavigateToServers)
            HomeIntent.RenewClicked -> sendEffect(HomeEffect.NavigateToPlan)
            HomeIntent.ChangeProtocolClicked -> sendEffect(HomeEffect.NavigateToProtocol)
            is HomeIntent.PermissionResult -> if (intent.granted) connect()
        }
    }

    private fun onToggle() {
        when (state.value.status) {
            is VpnStatus.Connected, is VpnStatus.Connecting ->
                viewModelScope.launch { vpn.disconnect() }

            else -> {
                val systemIntent = VpnService.prepare(context)
                if (systemIntent == null) connect()
                else sendEffect(HomeEffect.RequestVpnPermission(systemIntent))
            }
        }
    }

    private fun connect() {
        val server = state.value.server ?: return
        viewModelScope.launch {
            val protocol = protocols.protocol.first()
            vpn.connect(server, protocol)
        }
    }
}

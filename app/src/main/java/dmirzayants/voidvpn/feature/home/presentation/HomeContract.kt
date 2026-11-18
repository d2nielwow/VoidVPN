package dmirzayants.voidvpn.feature.home.presentation

import android.content.Intent
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import dmirzayants.voidvpn.feature.vpn.domain.VpnStatus

data class HomeState(
    val status: VpnStatus = VpnStatus.Disconnected,
    val server: Server? = null,
    val isAuto: Boolean = true,
    val protocol: VpnProtocol = VpnProtocol.AUTO,
    val elapsedSeconds: Long = 0,
    val showBanner: Boolean = false,
) : UiState

sealed interface HomeIntent : UiIntent {
    data object ToggleClicked : HomeIntent
    data object ServerClicked : HomeIntent
    data object RenewClicked : HomeIntent
    data object ChangeProtocolClicked : HomeIntent
    data class PermissionResult(val granted: Boolean) : HomeIntent
}

sealed interface HomeEffect : UiEffect {
    data class RequestVpnPermission(val intent: Intent) : HomeEffect
    data object NavigateToServers : HomeEffect
    data object NavigateToPlan : HomeEffect
    data object NavigateToProtocol : HomeEffect
}

package dmirzayants.voidvpn.feature.settings.presentation

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.feature.settings.domain.AppSettings
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol

data class SettingsState(
    val settings: AppSettings = AppSettings(),
    val protocol: VpnProtocol = VpnProtocol.AUTO,
) : UiState

sealed interface SettingsIntent : UiIntent {
    data class KillSwitchChanged(val value: Boolean) : SettingsIntent
    data class AutoConnectChanged(val value: Boolean) : SettingsIntent
    data class LaunchOnBootChanged(val value: Boolean) : SettingsIntent
    data object ProtocolClicked : SettingsIntent
    data object SplitTunnelingClicked : SettingsIntent
    data object LogsClicked : SettingsIntent
}

sealed interface SettingsEffect : UiEffect {
    data object NavigateToProtocol : SettingsEffect
    data object NavigateToSplitTunneling : SettingsEffect
    data object NavigateToLogs : SettingsEffect
}

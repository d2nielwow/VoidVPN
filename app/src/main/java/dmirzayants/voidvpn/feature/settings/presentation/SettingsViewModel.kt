package dmirzayants.voidvpn.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.settings.domain.SettingsRepository
import dmirzayants.voidvpn.feature.vpn.domain.ProtocolRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    protocolRepository: ProtocolRepository,
) : MviViewModel<SettingsState, SettingsIntent, SettingsEffect>(SettingsState()) {

    init {
        viewModelScope.launch {
            combine(settingsRepository.settings, protocolRepository.protocol) { s, p ->
                SettingsState(settings = s, protocol = p)
            }.collect { newState -> setState { newState } }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.KillSwitchChanged ->
                viewModelScope.launch { settingsRepository.setKillSwitch(intent.value) }
            is SettingsIntent.AutoConnectChanged ->
                viewModelScope.launch { settingsRepository.setAutoConnect(intent.value) }
            is SettingsIntent.LaunchOnBootChanged ->
                viewModelScope.launch { settingsRepository.setLaunchOnBoot(intent.value) }
            SettingsIntent.ProtocolClicked -> sendEffect(SettingsEffect.NavigateToProtocol)
            SettingsIntent.SplitTunnelingClicked -> sendEffect(SettingsEffect.NavigateToSplitTunneling)
            SettingsIntent.LogsClicked -> sendEffect(SettingsEffect.NavigateToLogs)
        }
    }
}

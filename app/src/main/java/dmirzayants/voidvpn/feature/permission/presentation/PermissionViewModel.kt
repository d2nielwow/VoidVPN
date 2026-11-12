package dmirzayants.voidvpn.feature.permission.presentation

import android.content.Context
import android.net.VpnService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dmirzayants.voidvpn.core.mvi.MviViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : MviViewModel<PermissionState, PermissionIntent, PermissionEffect>(PermissionState()) {

    override fun onIntent(intent: PermissionIntent) {
        when(intent) {
            PermissionIntent.ContinueClicked -> onContinue()
            is PermissionIntent.PermissionResult -> onResult(intent.granted)
            PermissionIntent.DismissDenied -> setState { copy(denied = false) }
        }
    }

    private fun onContinue() {
        setState { copy(denied = false) }
        val systemIntent = VpnService.prepare(context)
        if (systemIntent == null) {
            sendEffect(PermissionEffect.NavigateToHome)
        } else {
            sendEffect(PermissionEffect.RequestVpnPermission(systemIntent))
        }
    }

    private fun onResult(granted: Boolean) {
        if (granted) {
            sendEffect(PermissionEffect.NavigateToHome)
        } else {
            setState { copy(denied = true) }
        }
    }
}
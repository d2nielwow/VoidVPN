package dmirzayants.voidvpn.feature.permission.presentation

import android.content.Intent
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState

data class PermissionState(val denied: Boolean = false) : UiState

sealed interface PermissionIntent : UiIntent {
    data object ContinueClicked : PermissionIntent
    data class PermissionResult(val granted: Boolean) : PermissionIntent
    data object DismissDenied : PermissionIntent
}

sealed interface PermissionEffect : UiEffect {
    data class RequestVpnPermission(val intent: Intent) : PermissionEffect
    data object NavigateToHome : PermissionEffect
}
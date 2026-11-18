package dmirzayants.voidvpn.feature.account.presentation.login

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState

data class LoginState(
    val input: String = "",
    val isError: Boolean = false
) : UiState {
    val canSubmit: Boolean get() = input.length == 16
}

sealed interface LoginIntent : UiIntent {
    data class InputChanged(val value: String) : LoginIntent
    data object PasteClicked : LoginIntent
    data object SubmitClicked : LoginIntent
    data object ImportClicked : LoginIntent
    data object BackClicked : LoginIntent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToImport : LoginEffect
    data object NavigateBack : LoginEffect
    data object RequestPaste : LoginEffect
}
package dmirzayants.voidvpn.feature.account.presentation.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.account.domain.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : MviViewModel<LoginState, LoginIntent, LoginEffect>(
    LoginState()
) {
    override fun onIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.InputChanged -> onInputChanged(intent.value)
            LoginIntent.PasteClicked -> sendEffect(LoginEffect.RequestPaste)
            LoginIntent.SubmitClicked -> onSubmit()
            LoginIntent.ImportClicked -> sendEffect(LoginEffect.NavigateToImport)
            LoginIntent.BackClicked -> sendEffect(LoginEffect.NavigateBack)
        }
    }
    private fun onInputChanged(value: String) {
        val cleaned = value
            .filter { it.isLetterOrDigit() }
            .uppercase()
            .take(16)
        setState { copy(input = cleaned, isError = false) }
    }
    private fun onSubmit() {
        val state = state.value
        if (!state.canSubmit) return
        viewModelScope.launch {
            val ok = loginUseCase(state.input)
            if (ok) {
                sendEffect(LoginEffect.NavigateToHome)
            } else {
                setState { copy(isError = true) }
            }
        }
    }
}
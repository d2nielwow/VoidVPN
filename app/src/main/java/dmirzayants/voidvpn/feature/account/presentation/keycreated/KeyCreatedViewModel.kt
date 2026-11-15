package dmirzayants.voidvpn.feature.account.presentation.keycreated

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.account.domain.CreateAccountUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyCreatedViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase
) : MviViewModel<KeyCreatedState, KeyCreatedIntent, KeyCreatedEffect>(KeyCreatedState()) {

    init {
        viewModelScope.launch {
            val key = createAccountUseCase()
            setState { copy(keyParts = key.chunked(4)) }
        }
    }

    override fun onIntent(intent: KeyCreatedIntent) {
        when(intent) {
            KeyCreatedIntent.CopyClicked -> onCopy()
            KeyCreatedIntent.SaveQrClicked -> {}
            KeyCreatedIntent.Confirm -> sendEffect(KeyCreatedEffect.NavigateToHome)
        }
    }

    private fun onCopy() {
        val key = state.value.keyParts.joinToString("")
        sendEffect(KeyCreatedEffect.CopyToClipboard(key))
    }
}
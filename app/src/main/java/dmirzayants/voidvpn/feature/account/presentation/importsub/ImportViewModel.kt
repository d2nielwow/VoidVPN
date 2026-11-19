package dmirzayants.voidvpn.feature.account.presentation.importsub

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import dmirzayants.voidvpn.feature.account.domain.LinkParser
import dmirzayants.voidvpn.feature.account.domain.ParsedLink
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImportState(
    val input: String = "",
    val clipboard: String = "",
    val error: Boolean = false,
) : UiState {
    val detected: ParsedLink? get() = LinkParser.parse(input) ?: LinkParser.parse(clipboard)
    val clipboardDetected: Boolean get() = input.isBlank() && LinkParser.parse(clipboard) != null
}

sealed interface ImportIntent : UiIntent {
    data class InputChanged(val value: String) : ImportIntent
    data class ClipboardRead(val text: String) : ImportIntent
    data object ImportClicked : ImportIntent
    data object ScanClicked : ImportIntent
    data object BackClicked : ImportIntent
}

sealed interface ImportEffect : UiEffect {
    data object NavigateBack : ImportEffect
    data object NavigateToHome : ImportEffect
}

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : MviViewModel<ImportState, ImportIntent, ImportEffect>(ImportState()) {

    override fun onIntent(intent: ImportIntent) {
        when (intent) {
            is ImportIntent.InputChanged -> setState { copy(input = intent.value, error = false) }
            is ImportIntent.ClipboardRead -> setState { copy(clipboard = intent.text) }
            ImportIntent.ImportClicked -> onImport()
            ImportIntent.ScanClicked -> Unit
            ImportIntent.BackClicked -> sendEffect(ImportEffect.NavigateBack)
        }
    }

    private fun onImport() {
        val link = state.value.detected
        if (link == null) {
            setState { copy(error = true) }
            return
        }
        viewModelScope.launch {
            accountRepository.importSubscription(link.raw)
            sendEffect(ImportEffect.NavigateToHome)
        }
    }
}

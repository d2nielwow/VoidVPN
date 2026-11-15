package dmirzayants.voidvpn.feature.account.presentation.keycreated

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState

data class KeyCreatedState(val keyParts: List<String> = emptyList()) : UiState

sealed interface KeyCreatedIntent : UiIntent {
    data object CopyClicked : KeyCreatedIntent
    data object SaveQrClicked : KeyCreatedIntent
    data object Confirm : KeyCreatedIntent
}

sealed interface KeyCreatedEffect : UiEffect {
    data class CopyToClipboard(val text: String) : KeyCreatedEffect
    data object NavigateToHome : KeyCreatedEffect
}
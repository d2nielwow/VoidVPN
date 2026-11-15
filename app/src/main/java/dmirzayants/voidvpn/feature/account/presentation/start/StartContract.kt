package dmirzayants.voidvpn.feature.account.presentation.start

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState

data object StartState : UiState

sealed interface StartIntent : UiIntent {
    data object CreateAccount : StartIntent
    data object HaveKey : StartIntent
}

sealed interface StartEffect : UiEffect {
    data object NavigateToKeyCreated : StartEffect
    data object NavigateToLogin : StartEffect
}

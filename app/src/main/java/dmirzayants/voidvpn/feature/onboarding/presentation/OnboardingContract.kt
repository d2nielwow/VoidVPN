package dmirzayants.voidvpn.feature.onboarding.presentation

import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState

data class OnboardingState(val page: Int = 0) : UiState

sealed interface OnboardingIntent : UiIntent {
    data object Next : OnboardingIntent
    data object Skip : OnboardingIntent
    data class PageChanged(val page: Int) : OnboardingIntent
}

sealed interface OnboardingEffect : UiEffect {
    data object NavigateToPermission : OnboardingEffect
}
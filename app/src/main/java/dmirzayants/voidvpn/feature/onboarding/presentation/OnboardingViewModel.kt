package dmirzayants.voidvpn.feature.onboarding.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.feature.onboarding.domain.CompleteOnboardingUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboarding: CompleteOnboardingUseCase
) : MviViewModel<OnboardingState, OnboardingIntent, OnboardingEffect>(OnboardingState()) {
    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Next -> onNext()
            OnboardingIntent.Skip -> finish()
            is OnboardingIntent.PageChanged -> {
                setState { copy(page = intent.page) }
            }
        }
    }

    private fun onNext() {
        if (state.value.page < LAST_PAGE) {
            setState { copy(page = page + 1) }
        } else {
            finish()
        }
    }

    private fun finish() {
        viewModelScope.launch {
            completeOnboarding()
            sendEffect(OnboardingEffect.NavigateToPermission)
        }
    }

    private companion object {
        const val LAST_PAGE = 2
    }
}
package dmirzayants.voidvpn.feature.onboarding.domain

import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke() {
        repository.complete()
    }
}
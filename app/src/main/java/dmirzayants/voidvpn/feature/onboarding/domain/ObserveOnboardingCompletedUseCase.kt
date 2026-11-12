package dmirzayants.voidvpn.feature.onboarding.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOnboardingCompletedUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isCompleted
    }
}
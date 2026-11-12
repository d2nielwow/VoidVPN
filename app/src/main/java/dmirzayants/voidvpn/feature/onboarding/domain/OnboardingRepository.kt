package dmirzayants.voidvpn.feature.onboarding.domain

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {

    val isCompleted: Flow<Boolean>
    suspend fun complete()
}
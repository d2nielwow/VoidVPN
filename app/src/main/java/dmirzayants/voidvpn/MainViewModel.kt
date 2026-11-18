package dmirzayants.voidvpn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.feature.onboarding.domain.ObserveOnboardingCompletedUseCase
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import dmirzayants.voidvpn.navigation.HomeRoute
import dmirzayants.voidvpn.navigation.StartRoute
import dmirzayants.voidvpn.navigation.OnboardingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeOnboardingCompleted: ObserveOnboardingCompletedUseCase,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination: StateFlow<Any?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val completed = observeOnboardingCompleted().first()
            val hasAccount = accountRepository.accessKey.first() != null
            _startDestination.value = when {
                !completed -> OnboardingRoute
                !hasAccount -> StartRoute
                else -> HomeRoute
            }
        }
    }
}

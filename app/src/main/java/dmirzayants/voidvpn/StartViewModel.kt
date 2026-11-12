package dmirzayants.voidvpn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.feature.onboarding.domain.ObserveOnboardingCompletedUseCase
import dmirzayants.voidvpn.navigation.HomeRoute
import dmirzayants.voidvpn.navigation.OnboardingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    observeOnboardingCompleted: ObserveOnboardingCompletedUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination: StateFlow<Any?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val completed = observeOnboardingCompleted().first()
            _startDestination.value = if (completed) HomeRoute else OnboardingRoute
        }
    }
}

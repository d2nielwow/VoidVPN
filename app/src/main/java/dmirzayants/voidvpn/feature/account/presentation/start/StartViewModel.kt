package dmirzayants.voidvpn.feature.account.presentation.start

import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.core.mvi.MviViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() :
    MviViewModel<StartState, StartIntent, StartEffect>(StartState) {

    override fun onIntent(intent: StartIntent) {
        when (intent) {
            StartIntent.CreateAccount -> sendEffect(StartEffect.NavigateToKeyCreated)
            StartIntent.HaveKey -> sendEffect(StartEffect.NavigateToLogin)
        }
    }
}

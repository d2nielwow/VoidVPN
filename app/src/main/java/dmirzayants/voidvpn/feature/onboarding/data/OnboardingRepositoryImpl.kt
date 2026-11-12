package dmirzayants.voidvpn.feature.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dmirzayants.voidvpn.feature.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepositoryImpl @Inject constructor (private val dataStore: DataStore<Preferences>) : OnboardingRepository {

    private companion object {
        val KEY_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override val isCompleted: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_COMPLETED] ?: false }

    override suspend fun complete() {
        dataStore.edit { prefs -> prefs[KEY_COMPLETED] = true }
    }
}
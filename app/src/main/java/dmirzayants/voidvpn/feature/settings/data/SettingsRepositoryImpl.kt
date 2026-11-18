package dmirzayants.voidvpn.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dmirzayants.voidvpn.feature.settings.domain.AppSettings
import dmirzayants.voidvpn.feature.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val KILL_SWITCH = booleanPreferencesKey("kill_switch")
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
        val LAUNCH_ON_BOOT = booleanPreferencesKey("launch_on_boot")
    }

    override val settings: Flow<AppSettings> = dataStore.data.map { p ->
        AppSettings(
            killSwitch = p[KILL_SWITCH] ?: true,
            autoConnect = p[AUTO_CONNECT] ?: true,
            launchOnBoot = p[LAUNCH_ON_BOOT] ?: false,
        )
    }

    override suspend fun setKillSwitch(value: Boolean) {
        dataStore.edit { it[KILL_SWITCH] = value }
    }

    override suspend fun setAutoConnect(value: Boolean) {
        dataStore.edit { it[AUTO_CONNECT] = value }
    }

    override suspend fun setLaunchOnBoot(value: Boolean) {
        dataStore.edit { it[LAUNCH_ON_BOOT] = value }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}

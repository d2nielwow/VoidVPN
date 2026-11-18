package dmirzayants.voidvpn.feature.settings.domain

import kotlinx.coroutines.flow.Flow

data class AppSettings(
    val killSwitch: Boolean = true,
    val autoConnect: Boolean = true,
    val launchOnBoot: Boolean = false,
)

interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun setKillSwitch(value: Boolean)

    suspend fun setAutoConnect(value: Boolean)

    suspend fun setLaunchOnBoot(value: Boolean)
}

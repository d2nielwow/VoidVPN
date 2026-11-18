package dmirzayants.voidvpn.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dmirzayants.voidvpn.feature.settings.domain.SplitTunnelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplitTunnelRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SplitTunnelRepository {

    private companion object {
        val BYPASS = stringSetPreferencesKey("split_bypass_packages")
    }

    override val bypassPackages: Flow<Set<String>> =
        dataStore.data.map { it[BYPASS] ?: emptySet() }

    override suspend fun toggle(packageName: String) {
        dataStore.edit { prefs ->
            val current = prefs[BYPASS] ?: emptySet()
            prefs[BYPASS] = if (packageName in current) current - packageName else current + packageName
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SplitTunnelModule {
    @Binds
    @Singleton
    abstract fun bind(impl: SplitTunnelRepositoryImpl): SplitTunnelRepository
}

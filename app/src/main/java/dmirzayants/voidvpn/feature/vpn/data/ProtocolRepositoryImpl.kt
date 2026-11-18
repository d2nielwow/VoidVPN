package dmirzayants.voidvpn.feature.vpn.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dmirzayants.voidvpn.feature.vpn.domain.ProtocolRepository
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtocolRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ProtocolRepository {

    private companion object {
        val KEY = stringPreferencesKey("protocol")
    }

    override val protocol: Flow<VpnProtocol> = dataStore.data.map { prefs ->
        prefs[KEY]?.let { name -> VpnProtocol.entries.firstOrNull { it.name == name } }
            ?: VpnProtocol.AUTO
    }

    override suspend fun setProtocol(protocol: VpnProtocol) {
        dataStore.edit { it[KEY] = protocol.name }
    }
}

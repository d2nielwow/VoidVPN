package dmirzayants.voidvpn.feature.account.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AccountRepository {

    private companion object {
        val KEY = stringPreferencesKey("access_key")
        const val KEY_PREFIX = "VOID"
        const val KEY_RANDOM_LENGTH = 12
        const val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    }

    override val accessKey: Flow<String?> = dataStore.data.map { it[KEY] }

    override suspend fun createAccount(): String {
        val key = generateKey()
        dataStore.edit { it[KEY] = key }
        return key
    }

    override suspend fun login(key: String): Boolean {
        if (!isValidKey(key)) return false
        dataStore.edit { it[KEY] = key }
        return  true
    }

    private fun isValidKey(key: String): Boolean =
        key.length == KEY_PREFIX.length + KEY_RANDOM_LENGTH &&
                key.startsWith(KEY_PREFIX) &&
                key.drop(KEY_PREFIX.length).all { it in ALPHABET }

    private fun generateKey(): String {
        val random = SecureRandom()
        val suffix = (1..KEY_RANDOM_LENGTH)
            .map { ALPHABET[random.nextInt(ALPHABET.length)] }
            .joinToString("")
        return KEY_PREFIX + suffix
    }

}
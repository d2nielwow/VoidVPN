package dmirzayants.voidvpn.feature.account.domain

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val accessKey: Flow<String?>

    val hasAccount: Flow<Boolean>

    suspend fun createAccount(): String

    suspend fun importSubscription(link: String)
    suspend fun login(key: String): Boolean
}
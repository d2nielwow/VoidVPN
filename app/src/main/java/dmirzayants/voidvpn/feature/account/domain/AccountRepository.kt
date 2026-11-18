package dmirzayants.voidvpn.feature.account.domain

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val accessKey: Flow<String?>

    /** true, если есть ключ доступа или импортированная подписка. */
    val hasAccount: Flow<Boolean>

    suspend fun createAccount(): String

    suspend fun importSubscription(link: String)
    suspend fun login(key: String): Boolean
}
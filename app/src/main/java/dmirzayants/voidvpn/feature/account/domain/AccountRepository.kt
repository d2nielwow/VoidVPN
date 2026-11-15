package dmirzayants.voidvpn.feature.account.domain

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val accessKey: Flow<String?>

    suspend fun createAccount(): String
}
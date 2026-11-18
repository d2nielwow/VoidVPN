package dmirzayants.voidvpn.feature.account.domain

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(key: String): Boolean = repository.login(key)
}
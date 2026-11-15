package dmirzayants.voidvpn.feature.account.domain

import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
  private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): String = accountRepository.createAccount()
}
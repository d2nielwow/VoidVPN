package dmirzayants.voidvpn.feature.account

import dmirzayants.voidvpn.core.MainDispatcherRule
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import dmirzayants.voidvpn.feature.account.domain.LoginUseCase
import dmirzayants.voidvpn.feature.account.presentation.login.LoginEffect
import dmirzayants.voidvpn.feature.account.presentation.login.LoginIntent
import dmirzayants.voidvpn.feature.account.presentation.login.LoginViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private class FakeAccountRepository(private val loginResult: Boolean) : AccountRepository {
        override val accessKey: Flow<String?> = flowOf(null)
        override val hasAccount: Flow<Boolean> = flowOf(false)
        var imported: String? = null

        override suspend fun createAccount(): String = "VOIDABCDEFGH2345"
        override suspend fun importSubscription(link: String) {
            imported = link
        }
        override suspend fun login(key: String): Boolean = loginResult
    }

    private fun viewModel(loginResult: Boolean = true) =
        LoginViewModel(LoginUseCase(FakeAccountRepository(loginResult)))

    @Test
    fun `input is cleaned, uppercased and limited to 16 characters`() {
        val vm = viewModel()
        vm.onIntent(LoginIntent.InputChanged("void-abcd efgh 2345 extra"))
        assertEquals("VOIDABCDEFGH2345", vm.state.value.input)
    }

    @Test
    fun `submit is available only for a full key`() {
        val vm = viewModel()
        vm.onIntent(LoginIntent.InputChanged("VOIDABC"))
        assertFalse(vm.state.value.canSubmit)
        vm.onIntent(LoginIntent.InputChanged("VOIDABCDEFGH2345"))
        assertTrue(vm.state.value.canSubmit)
    }

    @Test
    fun `successful login navigates home`() = runTest {
        val vm = viewModel(loginResult = true)
        vm.onIntent(LoginIntent.InputChanged("VOIDABCDEFGH2345"))
        vm.onIntent(LoginIntent.SubmitClicked)
        assertEquals(LoginEffect.NavigateToHome, vm.effects.first())
    }

    @Test
    fun `failed login shows error and typing clears it`() {
        val vm = viewModel(loginResult = false)
        vm.onIntent(LoginIntent.InputChanged("VOIDABCDEFGH2345"))
        vm.onIntent(LoginIntent.SubmitClicked)
        assertTrue(vm.state.value.isError)
        vm.onIntent(LoginIntent.InputChanged("VOIDABCDEFGH234"))
        assertFalse(vm.state.value.isError)
    }

    @Test
    fun `submit with incomplete key does nothing`() {
        val vm = viewModel(loginResult = false)
        vm.onIntent(LoginIntent.InputChanged("VOID"))
        vm.onIntent(LoginIntent.SubmitClicked)
        assertFalse(vm.state.value.isError)
    }

    @Test
    fun `navigation intents produce effects`() = runTest {
        val vm = viewModel()
        vm.onIntent(LoginIntent.PasteClicked)
        assertEquals(LoginEffect.RequestPaste, vm.effects.first())
        vm.onIntent(LoginIntent.ImportClicked)
        assertEquals(LoginEffect.NavigateToImport, vm.effects.first())
        vm.onIntent(LoginIntent.BackClicked)
        assertEquals(LoginEffect.NavigateBack, vm.effects.first())
    }
}

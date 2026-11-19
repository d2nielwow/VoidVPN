package dmirzayants.voidvpn.feature.account

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dmirzayants.voidvpn.feature.account.data.AccountRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AccountRepositoryImplTest {

    @get:Rule
    val folder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var repository: AccountRepositoryImpl

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore = PreferenceDataStoreFactory.create(scope = scope) {
            folder.newFile("test.preferences_pb")
        }
        repository = AccountRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `new repository has no account`() = runTest {
        assertFalse(repository.hasAccount.first())
        assertNull(repository.accessKey.first())
    }

    @Test
    fun `created key has VOID prefix, valid length and alphabet`() = runTest {
        val key = repository.createAccount()
        assertEquals(16, key.length)
        assertTrue(key.startsWith("VOID"))
        assertTrue(key.drop(4).all { it in "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" })
    }

    @Test
    fun `created key is stored and account appears`() = runTest {
        val key = repository.createAccount()
        assertEquals(key, repository.accessKey.first())
        assertTrue(repository.hasAccount.first())
    }

    @Test
    fun `two created keys differ`() = runTest {
        assertTrue(repository.createAccount() != repository.createAccount())
    }

    @Test
    fun `login accepts a valid key and stores it`() = runTest {
        assertTrue(repository.login("VOIDABCDEFGH2345"))
        assertEquals("VOIDABCDEFGH2345", repository.accessKey.first())
    }

    @Test
    fun `login rejects wrong prefix, length and characters`() = runTest {
        assertFalse(repository.login("ABCDABCDEFGH2345"))
        assertFalse(repository.login("VOIDABC"))
        assertFalse(repository.login("VOIDABCDEFGH234O"))
        assertFalse(repository.hasAccount.first())
    }

    @Test
    fun `imported subscription counts as account`() = runTest {
        repository.importSubscription("https://panel.example.com/sub/abc")
        assertTrue(repository.hasAccount.first())
        assertNull(repository.accessKey.first())
    }
}

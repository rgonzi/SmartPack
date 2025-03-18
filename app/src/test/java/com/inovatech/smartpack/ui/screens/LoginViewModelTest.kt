import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.ui.screens.LoginViewModel
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import junit.framework.Assert.assertFalse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var tokenRepository: TokenRepository

    @Mock
    private lateinit var smartPackRepository: SmartPackRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginViewModel = LoginViewModel(tokenRepository, smartPackRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Restaurar el dispatcher després dels tests
    }

    @Test
    fun testLoginEmptyFields() = runTest {
        loginViewModel.updateEmail("")
        loginViewModel.updatePassword("")

        loginViewModel.login()

        assertEquals("Omple tots els camps", loginViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidEmail() = runTest {
        val email = "email"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword("Password1")

        loginViewModel.login()

        assertFalse(email.isValidEmail())
        assertEquals("Revisa les dades introduïdes", loginViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidPassword() = runTest {
        val pass = "1234"
        loginViewModel.updateEmail("william@gmail.com")
        loginViewModel.updatePassword(pass)

        loginViewModel.login()

        assertFalse(pass.isValidPassword())
        assertEquals("Revisa les dades introduïdes", loginViewModel.uiState.value.error)
    }

    @Test
    fun testValidCredentials() {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        assertTrue(email.isValidEmail())
        assertTrue(pass.isValidPassword())
    }

    @Test
    fun testInvalidLogin() = runTest {
        val email = "william@gmail.com"
        val pass = "1234"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        loginViewModel.login()

        with(loginViewModel.uiState.value) {
            assertTrue(hasTriedLogin)
            assertEquals("Revisa les dades introduïdes", error)
            assertEquals(null, token)
        }
        verify(tokenRepository, never()).saveAuthToken(any())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testValidLogin() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        val mockResponse = LoginResponse(token = "test_token")
        whenever(smartPackRepository.login(email, pass)).thenReturn(Response.success(mockResponse))

        loginViewModel.login()
        advanceUntilIdle()

        with(loginViewModel.uiState.value) {
            assertTrue(hasTriedLogin)
            assertEquals(null, error)
            assertEquals("test_token", token)
        }

        verify(tokenRepository).saveAuthToken("test_token")
    }
}

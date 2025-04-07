import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.api.LoginRequest
import com.inovatech.smartpack.model.api.LoginResponse
import com.inovatech.smartpack.ui.screens.LoginViewModel
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var tokenRepository: TokenRepository

    @Mock
    private lateinit var mockRepository: SmartPackRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginViewModel = LoginViewModel(tokenRepository, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Restaurar el dispatcher després dels tests
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'una entrada buida
     */
    @Test
    fun testLoginEmptyFields() = runTest {
        loginViewModel.updateEmail("")
        loginViewModel.updatePassword("")

        loginViewModel.login()
        advanceUntilIdle()

        assertEquals("El correu és obligatori", loginViewModel.uiState.value.error)
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'un correu no vàlid
     */
    @Test
    fun testInvalidEmail() = runTest {
        val email = "email"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword("Password1")

        loginViewModel.login()
        advanceUntilIdle()

        assertFalse(email.isValidEmail())
        assertEquals("Introdueix un correu vàlid", loginViewModel.uiState.value.error)
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'una
     * contrasenya que no compleix els requisits.
     */
    @Test
    fun testInvalidPassword() = runTest {
        val pass = "1234"
        loginViewModel.updateEmail("william@gmail.com")
        loginViewModel.updatePassword(pass)

        loginViewModel.login()
        advanceUntilIdle()

        assertFalse(pass.isValidPassword())
        assertEquals(
            "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número",
            loginViewModel.uiState.value.error
        )
    }

    /**
     * Test que verifica que el viewmodel és capaç de comprovar que tant el email com la
     * contrasenya estan ben escrits segons requisits.
     */
    @Test
    fun testValidCredentials() {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        assertTrue(email.isValidEmail())
        assertTrue(pass.isValidPassword())
        assertEquals(null, loginViewModel.uiState.value.error)
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'un intent d'inici
     * de sessió en el que l'usuari proporcionat no existeix en un servidor fictici (error 403)
     */
    @Test
    fun testUnauthorizedLoginMock() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        val mockResponse = Response.error<LoginResponse>(403, "".toResponseBody())
        val request = LoginRequest(email, pass)

        whenever(mockRepository.login(request)).thenReturn(mockResponse)

        loginViewModel.login()
        advanceUntilIdle()

        assertEquals(403, mockResponse.code())
        assertTrue(loginViewModel.uiState.value.hasTriedLogin)
        verify(tokenRepository, never()).saveAuthToken(any())
    }

    /**
     * Test que verifica que el viewmodel és capaç de realitzar un inici de sessió correcte davant
     * d'unes credencials vàlides i que l'estat de l'error es manté en null.
     */
    @Test
    fun testLoginSuccessMock() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        val mockResponse = LoginResponse(token = "test_token", expiresIn = Date())
        val request = LoginRequest(email, pass)

        whenever(mockRepository.login(request)).thenReturn(Response.success(mockResponse))

        loginViewModel.login()
        advanceUntilIdle()

        with(loginViewModel.uiState.value) {
            assertTrue(hasTriedLogin)
            assertEquals(null, error)
        }

        verify(tokenRepository).saveAuthToken("test_token")
    }
}

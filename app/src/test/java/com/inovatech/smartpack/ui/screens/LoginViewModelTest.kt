package com.inovatech.smartpack.ui.screens

import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    
    @Mock
    private val tokenRepository = mock(TokenRepository::class.java)
    private val smartPackRepository = mock(SmartPackRepository::class.java)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        loginViewModel = LoginViewModel(
            tokenRepository, smartPackRepository
        )
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
    fun testValidCredentials() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        assertTrue(email.isValidEmail())
        assertTrue(pass.isValidPassword())
        assertEquals(null, loginViewModel.uiState.value.error)
    }

    @Test
    fun testValidLogin() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        loginViewModel.updateEmail(email)
        loginViewModel.updatePassword(pass)

        val mockResponse = LoginResponse(token = "test_token")
        whenever(smartPackRepository.login(loginViewModel.uiState.value.email,loginViewModel.uiState.value.password))
            .thenReturn(Response.success(mockResponse))

        loginViewModel.login()

        assertEquals(true, loginViewModel.uiState.value.hasTriedLogin)
        assertEquals(null, loginViewModel.uiState.value.error)
        assertEquals("test_token", loginViewModel.uiState.value.token)


        verify(tokenRepository).saveAuthToken("test_token")
    }
}
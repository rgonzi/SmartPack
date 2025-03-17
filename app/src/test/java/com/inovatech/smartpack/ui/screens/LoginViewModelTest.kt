package com.inovatech.smartpack.ui.screens

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response


class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private val tokenRepository = mock(TokenRepository::class.java)
    private val smartPackRepository = mock(SmartPackRepository::class.java)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        loginViewModel = LoginViewModel(tokenRepository)
    }

    @Test
    fun testLoginEmptyFields() {
        loginViewModel.updateEmail("")
        loginViewModel.updatePassword("")

        loginViewModel.login()

        assertEquals("Omple tots els camps", loginViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidEmail() {
        loginViewModel.updateEmail("email")
        loginViewModel.updatePassword("Password1")

        loginViewModel.login()

        assertEquals("Revisa les dades introduïdes", loginViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidPassword() {
        loginViewModel.updateEmail("william@gmail.com")
        loginViewModel.updatePassword("1234")

        loginViewModel.login()

        assertEquals("Revisa les dades introduïdes", loginViewModel.uiState.value.error)
    }

    @Test
    fun testValidLogin() = runBlocking {
        loginViewModel.updateEmail("willy@gmail.com")
        loginViewModel.updatePassword("Password1")

        val mockResponse = LoginResponse(token = "test_token")
        whenever(smartPackRepository.login("willy@gmail.com", "Password1"))
            .thenReturn(Response.success(mockResponse))

        loginViewModel.login()

        assertEquals(true, loginViewModel.uiState.value.hasTriedLogin)
        assertEquals(null, loginViewModel.uiState.value.error)
        assertEquals("test_token", loginViewModel.uiState.value.token)


        verify(tokenRepository).saveAuthToken("test_token")
    }
}
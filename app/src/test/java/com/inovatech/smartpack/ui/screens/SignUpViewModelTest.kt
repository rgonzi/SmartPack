package com.inovatech.smartpack.ui.screens

import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.Usuari
import com.inovatech.smartpack.utils.isValidEmail
import com.inovatech.smartpack.utils.isValidPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest {

    private lateinit var signUpViewModel: SignUpViewModel

    @Mock
    private lateinit var smartPackRepository: SmartPackRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        signUpViewModel = SignUpViewModel(smartPackRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInvalidSignUp() = runTest {
        signUpViewModel.updateEmail("email")
        signUpViewModel.updatePassword("1234")
        signUpViewModel.updateRepeatedPassword("1234")

        signUpViewModel.register()
        advanceUntilIdle()

        assertEquals("Introdueix un correu vàlid", signUpViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidEmail() = runTest {
        val email = "email"
        signUpViewModel.updateEmail(email)
        signUpViewModel.updatePassword("Password1")
        signUpViewModel.updateRepeatedPassword("Password1")

        signUpViewModel.register()
        advanceUntilIdle()

        assertFalse(email.isValidEmail())
    assertEquals("Introdueix un correu vàlid", signUpViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidPassword() = runTest {
        val pass = "1234"
        signUpViewModel.updateEmail("william@gmail.com")
        signUpViewModel.updatePassword(pass)
        signUpViewModel.updateRepeatedPassword(pass)

        signUpViewModel.register()
        advanceUntilIdle()

        assertFalse(pass.isValidPassword())
        assertEquals("La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número", signUpViewModel.uiState.value.error)
    }

    @Test
    fun testInvalidRepeatedPassword() = runTest {
        val pass = "Password1"
        signUpViewModel.updateEmail("william@gmail.com")
        signUpViewModel.updatePassword(pass)
        signUpViewModel.updateRepeatedPassword("Password2")

        signUpViewModel.register()
        advanceUntilIdle()

        assertEquals("Les contrasenyes no coincideixen", signUpViewModel.uiState.value.error)
    }

    @Test
    fun testValidCredentials() {
        val email = "william@gmail.com"
        val pass = "Password1"
        signUpViewModel.updateEmail(email)
        signUpViewModel.updatePassword(pass)
        signUpViewModel.updateRepeatedPassword(pass)

        assertTrue(email.isValidEmail())
        assertTrue(pass.isValidPassword())
        assertEquals(null, signUpViewModel.uiState.value.error)
    }

    @Test
    fun testSignUpSuccess() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"
        signUpViewModel.updateEmail(email)
        signUpViewModel.updatePassword(pass)
        signUpViewModel.updateRepeatedPassword(pass)

        val mockResponse = Usuari(email = email, password = pass)
        val request = RegisterRequest(email, pass)

        whenever(smartPackRepository.register(request)).thenReturn(Response.success(mockResponse))

        signUpViewModel.register()
        advanceUntilIdle()

        with(signUpViewModel.uiState.value) {
            assertEquals(null, error)
            assertEquals(true, hasTriedRegister)
            assertEquals(false, isLoading)
            assertEquals(true, signUpSuccess)
        }
    }
}
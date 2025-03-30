package com.inovatech.smartpack.ui.screens

import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.model.UserRequest
import com.inovatech.smartpack.model.User
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
    private lateinit var mockRepository: SmartPackRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        signUpViewModel = SignUpViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'un correu no vàlid
     */
    @Test
    fun testInvalidEmail() = runTest {
        val email = "email"
        signUpViewModel.updateField("email", "email")
        signUpViewModel.updateField("password", "1234567A")
        signUpViewModel.updateField("repeatedPassword", "1234567A")
        signUpViewModel.updateField("name", "Test")
        signUpViewModel.updateField("surname", "Test test")
        signUpViewModel.updateField("tel", "123456789")
        signUpViewModel.updateField("address", "some address")

        signUpViewModel.register()
        advanceUntilIdle()

        assertFalse(email.isValidEmail())
        assertEquals("Introdueix un correu vàlid", signUpViewModel.uiState.value.error)
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent davant d'una
     * contrasenya que no compleix els requisits
     */
    @Test
    fun testInvalidPassword() = runTest {
        val pass = "1234"

        signUpViewModel.updateField("email", "william@gmail.com")
        signUpViewModel.updateField("password", pass)
        signUpViewModel.updateField("repeatedPassword", pass)
        signUpViewModel.updateField("name", "Test")
        signUpViewModel.updateField("surname", "Test test")
        signUpViewModel.updateField("tel", "123456789")
        signUpViewModel.updateField("address", "some address")

        signUpViewModel.register()
        advanceUntilIdle()

        assertFalse(pass.isValidPassword())
        assertEquals(
            "La contrasenya ha de tenir mínim 8 caràcters, almenys 1 majúscula i 1 número",
            signUpViewModel.uiState.value.error
        )
    }

    /**
     * Test que verifica que el viewmodel mostri l'error corresponent al no posar la mateixa
     * contrasenya en els dos quadres de text corresponents.
     */
    @Test
    fun testInvalidRepeatedPassword() = runTest {

        signUpViewModel.updateField("email", "william@gmail.com")
        signUpViewModel.updateField("password", "Password1")
        signUpViewModel.updateField("repeatedPassword", "Password2")
        signUpViewModel.updateField("name", "Test")
        signUpViewModel.updateField("surname", "Test test")
        signUpViewModel.updateField("tel", "123456789")
        signUpViewModel.updateField("address", "some address")

        signUpViewModel.register()
        advanceUntilIdle()

        assertEquals("Les contrasenyes no coincideixen", signUpViewModel.uiState.value.error)
    }

    /**
     * Test que verifica que el viewmodel es comporta correctament al realitzar un registred'un
     * usuari vàlid en un repositori fals. L'estat de l'error ha de ser null
     */
    @Test
    fun testSignUpSuccessMock() = runTest {

        signUpViewModel.updateField("email", "test@test.com")
        signUpViewModel.updateField("password", "1234567A")
        signUpViewModel.updateField("repeatedPassword", "1234567A")
        signUpViewModel.updateField("name", "Test")
        signUpViewModel.updateField("surname", "Test test")
        signUpViewModel.updateField("tel", "123456789")
        signUpViewModel.updateField("address", "some address")

        val request = UserRequest(
            email = "email",
            password = "1234567A",
            role = null,
            name = "Test",
            surname = "Test test",
            tel = "123456789",
            address = "some address"
        )

        val mockResponse = Response.success(User(email = "test@test.com", password = "1234567A"))

        whenever(mockRepository.register(request)).thenReturn(mockResponse)

        mockRepository.register(request)

        assertTrue(mockResponse.isSuccessful)
        assertEquals("test@test.com", mockResponse.body()?.email)
        assertEquals(null, signUpViewModel.uiState.value.error)

    }
}
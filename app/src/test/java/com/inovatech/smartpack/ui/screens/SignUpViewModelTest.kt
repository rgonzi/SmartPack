package com.inovatech.smartpack.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignUpViewModelTest {

    private lateinit var signUpViewModel: SignUpViewModel

    @Before
    fun setUp() {
        signUpViewModel = SignUpViewModel()
    }

    @Test
    fun testInvalidSignUp() {
        signUpViewModel.updateEmail("email")
        signUpViewModel.updatePassword("1234")
        signUpViewModel.updateRepeatedPassword("1234")

        signUpViewModel.register()

        assertEquals("Revisa les dades introduïdes", signUpViewModel.uiState.value.error)
    }

    @Test
    fun testValidSignUp() {
        signUpViewModel.updateEmail("valid@gmail.com")
        signUpViewModel.updatePassword("Password1")
        signUpViewModel.updateRepeatedPassword("Password1")

        signUpViewModel.register()

        assertEquals(null, signUpViewModel.uiState.value.error)
        assertEquals(true, signUpViewModel.uiState.value.hasTriedRegister)
        assertEquals(false, signUpViewModel.uiState.value.isLoading)

        //TODO Afegir més verificacions com un Success al ViewModel
    }
}
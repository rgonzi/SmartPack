package com.inovatech.smartpack.ui.screens

import com.inovatech.smartpack.data.TokenRepository
import com.inovatech.smartpack.ui.screens.userConfig.UserConfigViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var viewModel: UserConfigViewModel

    @Mock
    private lateinit var tokenRepository: TokenRepository

    @Before
    fun setUp() {
        viewModel = UserConfigViewModel(tokenRepository)
    }

    /**
     * Test que prova que al realitzar el logout del viewmodel s'esborri el token d'acces.
     */
    @Test
    fun testLogout() {
        viewModel.logout()

        verify(tokenRepository).clearAuthToken()

        assert(viewModel.uiState.value.isLogoutOrDeactivateSuccess)
    }
}
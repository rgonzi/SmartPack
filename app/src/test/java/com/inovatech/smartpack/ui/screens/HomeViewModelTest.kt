package com.inovatech.smartpack.ui.screens

import com.inovatech.smartpack.data.TokenRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var tokenRepository: TokenRepository

    @Before
    fun setUp() {
        viewModel = HomeViewModel(tokenRepository)
    }

    @Test
    fun testLogout() {
        viewModel.logout()

        verify(tokenRepository).clearAuthToken()

        assert(viewModel.uiState.value.isLogoutSuccess)
    }
}
package com.inovatech.smartpack.data

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthTokenServiceTest {

    private lateinit var authTokenService: AuthTokenService

    @Before
    fun setUp() {
        authTokenService = AuthTokenService(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testSaveAndGetAuthToken() {
        val token = "test_token"

        authTokenService.saveAuthToken(token)

        val savedToken = authTokenService.getAuthToken()

        assertEquals(token, savedToken)
    }

    @Test
    fun testClearAuthToken() {
        val token = "testToken"
        authTokenService.saveAuthToken(token)

        authTokenService.clearAuthToken()

        val savedToken = authTokenService.getAuthToken()

        assertEquals(null, savedToken)
    }
}
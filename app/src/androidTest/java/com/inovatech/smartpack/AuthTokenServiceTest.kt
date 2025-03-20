package com.inovatech.smartpack

import androidx.test.core.app.ApplicationProvider
import com.inovatech.smartpack.offline.AuthTokenService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthTokenServiceTest {

    private lateinit var authTokenService: AuthTokenService

    @Before
    fun setUp() {
        authTokenService = AuthTokenService(ApplicationProvider.getApplicationContext())
    }

    /**
     * Test d'instrumentació que verifica que donat un token aquest es pot desar correctament i
     * posteriorment recuperar des de la SharedPreferences
     */
    @Test
    fun testSaveAndGetAuthToken() {
        val token = "test_token"

        authTokenService.saveAuthToken(token)

        val savedToken = authTokenService.getAuthToken()

        Assert.assertEquals(token, savedToken)
    }

    /**
     * Test d'integració que verifica que donat un token ja desat es pot eliminar des de la
     * SharedPreferences
     */
    @Test
    fun testClearAuthToken() {
        authTokenService.clearAuthToken()

        val savedToken = authTokenService.getAuthToken()

        Assert.assertEquals(null, savedToken)
    }
}
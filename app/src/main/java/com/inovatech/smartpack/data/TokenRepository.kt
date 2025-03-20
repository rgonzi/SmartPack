package com.inovatech.smartpack.data

import com.inovatech.smartpack.offline.AuthTokenService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton que crea un TokenRepository per poder realitzar peticions al AuthTokenService i
 * gestionar el Token d'inici de sessió
 */
@Singleton
class TokenRepository @Inject constructor(
    private val authTokenService: AuthTokenService,
) {

    fun saveAuthToken(token: String) {
        authTokenService.saveAuthToken(token)
    }

    fun getAuthToken(): String? {
        return authTokenService.getAuthToken()
    }

    fun clearAuthToken() {
        authTokenService.clearAuthToken()
    }

    fun isTokenValid(): Boolean {
        return authTokenService.isTokenValid()
    }
}
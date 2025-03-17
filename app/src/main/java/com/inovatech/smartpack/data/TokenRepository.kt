package com.inovatech.smartpack.data

import javax.inject.Inject
import javax.inject.Singleton

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
package com.inovatech.smartpack.data

import android.content.Context

class TokenRepository(private val context: Context) {

    private val authTokenService = AuthTokenService(context)

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
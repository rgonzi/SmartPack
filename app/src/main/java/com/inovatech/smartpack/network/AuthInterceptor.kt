package com.inovatech.smartpack.network

import com.inovatech.smartpack.data.TokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Classe Interceptor que afageix el token dins del Header per fer les peticions d'aquelles
 * que no pertanyin al grup /auth
 */
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val token = tokenRepository.getAuthToken()

        return if(token != null && shouldAddAuthorizationHeader(originRequest)) {
            val newRequest = originRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originRequest)
        }
    }

    /**
     * Determina si la petició conté /auth o no per determinar si afegim el token o no
     */
    private fun shouldAddAuthorizationHeader(
        request: Request): Boolean {
        val url = request.url.toString()
        return !url.contains("/auth")
    }
}
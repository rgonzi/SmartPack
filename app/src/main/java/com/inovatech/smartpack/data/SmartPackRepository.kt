package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.model.LoginRequest
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.Usuari
import com.inovatech.smartpack.network.SmartPackApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface SmartPackRepository {
    suspend fun login(usuari: LoginRequest): Response<LoginResponse>

    suspend fun register(usuari: RegisterRequest): Response<Usuari>
}

/**
 * Repository principal que s'usa per fer peticions a l'hora d'iniciar sessió o registrar-se
 */
@Singleton
 class NetworkSmartPackRepository @Inject constructor(
    private val smartPackApiService: SmartPackApiService
 ) : SmartPackRepository {
    override suspend fun login(usuari: LoginRequest): Response<LoginResponse> {
        return smartPackApiService.login(usuari)
    }

    override suspend fun register(usuari: RegisterRequest): Response<Usuari> {
        return smartPackApiService.register(usuari)
    }
}


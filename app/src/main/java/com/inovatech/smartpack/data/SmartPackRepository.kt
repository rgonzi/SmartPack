package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.model.Usuari
import com.inovatech.smartpack.network.SmartPackApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface SmartPackRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse>

    suspend fun register(usuari: Usuari): Response<Usuari>
}

@Singleton
 class NetworkSmartPackRepository @Inject constructor(
    private val smartPackApiService: SmartPackApiService
 ) : SmartPackRepository {
    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return smartPackApiService.login(email, password)
    }

    override suspend fun register(usuari: Usuari): Response<Usuari> {
        return smartPackApiService.register(usuari)
    }
}


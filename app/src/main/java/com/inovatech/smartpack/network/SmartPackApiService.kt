package com.inovatech.smartpack.network

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.model.LoginRequest
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.Usuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SmartPackApiService {

    @POST("/auth/login")
    suspend fun login(@Body usuari: LoginRequest): Response<LoginResponse>

    @POST("/auth/registrar")
    suspend fun register(@Body usuari: RegisterRequest): Response<Usuari>
}
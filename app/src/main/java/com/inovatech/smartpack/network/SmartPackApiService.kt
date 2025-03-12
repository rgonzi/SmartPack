package com.inovatech.smartpack.network

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.model.Usuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SmartPackApiService {

    @POST("/auth/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body usuari: Usuari): Response<Usuari>
}
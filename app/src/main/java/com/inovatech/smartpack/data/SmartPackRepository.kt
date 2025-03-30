package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.auth.ForgotPasswordRequest
import com.inovatech.smartpack.model.auth.ForgotPasswordResponse
import com.inovatech.smartpack.model.auth.LoginResponse
import com.inovatech.smartpack.model.auth.LoginRequest
import com.inovatech.smartpack.model.UserRequest
import com.inovatech.smartpack.model.auth.ResetPasswordRequest
import com.inovatech.smartpack.model.UserResponse
import com.inovatech.smartpack.network.SmartPackApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface SmartPackRepository {
    suspend fun login(usuari: LoginRequest): Response<LoginResponse>

    suspend fun register(usuari: UserRequest): Response<UserResponse>

    suspend fun forgotPassword(email: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<Unit>

    suspend fun getUserDetails(): Response<UserResponse>

    suspend fun updateUser(id: Int, usuari: UserRequest): Response<UserResponse>
}

/**
 * Repository principal que s'usa per fer peticions a l'hora d'iniciar sessió o registrar-se
 */
@Singleton
 class NetworkSmartPackRepository @Inject constructor(
    private val smartPackApiService: SmartPackApiService
 ) : SmartPackRepository {

     //Peticions d'autenticació
    override suspend fun login(usuari: LoginRequest): Response<LoginResponse> {
        return smartPackApiService.login(usuari)
    }

    override suspend fun register(usuari: UserRequest): Response<UserResponse> {
        return smartPackApiService.register(usuari)
    }

    override suspend fun forgotPassword(email: ForgotPasswordRequest): Response<ForgotPasswordResponse> {
        return smartPackApiService.forgotPassword(email)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<Unit> {
        return smartPackApiService.resetPassword(resetPasswordRequest)
    }

    //Peticions d'usuari
    override suspend fun getUserDetails(): Response<UserResponse> {
        return smartPackApiService.getUserDetails()
    }

    override suspend fun updateUser(id: Int, usuari: UserRequest): Response<UserResponse> {
        return smartPackApiService.updateUser(id, usuari)
    }


}


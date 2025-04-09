package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.api.ApiResponse
import com.inovatech.smartpack.model.api.DeliverymanRequest
import com.inovatech.smartpack.model.api.DeliverymanResponse
import com.inovatech.smartpack.model.api.ForgotPasswordRequest
import com.inovatech.smartpack.model.api.ForgotPasswordResponse
import com.inovatech.smartpack.model.api.LoginResponse
import com.inovatech.smartpack.model.api.LoginRequest
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.model.api.ResetPasswordRequest
import com.inovatech.smartpack.model.api.UserResponse
import com.inovatech.smartpack.model.api.VehicleDTO
import com.inovatech.smartpack.network.SmartPackApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface SmartPackRepository {
    suspend fun login(usuari: LoginRequest): Response<LoginResponse>
    suspend fun register(usuari: UserRequest): Response<UserResponse>
    suspend fun forgotPassword(email: ForgotPasswordRequest): Response<ForgotPasswordResponse>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse>

    suspend fun getUserDetails(): Response<UserResponse>
    suspend fun loadUser(id: Int): Response<UserResponse>
    suspend fun updateUser(id: Int, usuari: UserRequest): Response<UserResponse>
    suspend fun deactivateUser(id:Int): Response<ApiResponse>

    suspend fun getDeliverymanByUserId(userId: Int): Response<DeliverymanResponse>
    suspend fun updateDeliveryman(id: Long, transportista: DeliverymanRequest): Response<DeliverymanResponse>
    suspend fun createDeliveryman(transportista: DeliverymanRequest): Response<DeliverymanResponse>
    suspend fun assignVehicleToDeliveryman(transportistaId: Long, vehicleId: Long): Response<ApiResponse>
    suspend fun desassignVehicleFromDeliveryman(deliverymanId: Long): Response<ApiResponse>

    suspend fun getVehicleById(vehicleId: Long): Response<VehicleDTO>
    suspend fun createVehicle(vehicle: VehicleDTO): Response<VehicleDTO>
    suspend fun updateVehicle(id: Long, vehicle: VehicleDTO): Response<VehicleDTO>
    suspend fun deactivateVehicle(id: Long): Response<ApiResponse>
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

    override suspend fun register(usuari: UserRequest): Response<UserResponse> {
        return smartPackApiService.register(usuari)
    }

    override suspend fun forgotPassword(email: ForgotPasswordRequest): Response<ForgotPasswordResponse> {
        return smartPackApiService.forgotPassword(email)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse> {
        return smartPackApiService.resetPassword(resetPasswordRequest)
    }

    override suspend fun getUserDetails(): Response<UserResponse> {
        return smartPackApiService.getUserDetails()
    }

    override suspend fun loadUser(id: Int): Response<UserResponse> {
        return smartPackApiService.loadUser(id)
    }

    override suspend fun updateUser(id: Int, usuari: UserRequest): Response<UserResponse> {
        return smartPackApiService.updateUser(id, usuari)
    }

    override suspend fun deactivateUser(id: Int): Response<ApiResponse> {
        return smartPackApiService.deactivateUser(id)
    }

    override suspend fun getDeliverymanByUserId(userId: Int): Response<DeliverymanResponse> {
        return smartPackApiService.getDeliverymanByUserId(userId)
    }

    override suspend fun updateDeliveryman(
        id: Long,
        transportista: DeliverymanRequest,
    ): Response<DeliverymanResponse> {
        return smartPackApiService.updateDeliveryman(id, transportista)
    }

    override suspend fun createDeliveryman(transportista: DeliverymanRequest): Response<DeliverymanResponse> {
        return smartPackApiService.createDeliveryman(transportista)
    }

    override suspend fun assignVehicleToDeliveryman(
        transportistaId: Long,
        vehicleId: Long,
    ): Response<ApiResponse> {
        return smartPackApiService.assignVehicleToDeliveryman(transportistaId, vehicleId)
    }

    override suspend fun desassignVehicleFromDeliveryman(deliverymanId: Long): Response<ApiResponse> {
        return smartPackApiService.desassignVehicleFromDeliveryman(deliverymanId)
    }

    override suspend fun getVehicleById(vehicleId: Long): Response<VehicleDTO> {
        return smartPackApiService.getVehicleById(vehicleId)
    }

    override suspend fun updateVehicle(id: Long, vehicle: VehicleDTO): Response<VehicleDTO> {
        return smartPackApiService.updateVehicle(id, vehicle)
    }

    override suspend fun deactivateVehicle(id: Long): Response<ApiResponse> {
        return smartPackApiService.deactivateVehicle(id)
    }

    override suspend fun createVehicle(vehicle: VehicleDTO): Response<VehicleDTO> {
        return smartPackApiService.createVehicle(vehicle)
    }

}


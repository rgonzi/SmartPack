package com.inovatech.smartpack.network

import com.inovatech.smartpack.model.api.ApiResponse
import com.inovatech.smartpack.model.api.DeliverymanRequest
import com.inovatech.smartpack.model.api.ForgotPasswordRequest
import com.inovatech.smartpack.model.api.ForgotPasswordResponse
import com.inovatech.smartpack.model.api.DeliverymanResponse
import com.inovatech.smartpack.model.api.LoginResponse
import com.inovatech.smartpack.model.api.LoginRequest
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.model.api.ResetPasswordRequest
import com.inovatech.smartpack.model.api.UserResponse
import com.inovatech.smartpack.model.api.VehicleDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfície necessària per Retrofit per definir els endpoints de l'API a l'hora de fer les
 * diferents peticions disponibles
 */
interface SmartPackApiService {

    //Autenticació
    /**
     * Petició de login
     * @param usuari: un LoginRequest que està compost d'un email i una contrasenya
     * @return LoginResponse: retorna una resposta de l'api del tipus LoginResponse,
     * compost d'un token vàlid i una data d'expiració
     */
    @POST("/auth/login")
    suspend fun login(@Body usuari: LoginRequest): Response<LoginResponse>


    /**
     * Petició de registre
     * @param usuari: RegisterRequest format per un email, una contrasenya i el rol USER
     * @return Usuari: retorna un usuari registrat amb tot de dades diferents com el correu,
     * la data de creació, si està habilitat, el rol, etc.
     */
    @POST("/auth/registrar")
    suspend fun register(@Body usuari: UserRequest): Response<UserResponse>

    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body email: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse>


    //Usuaris
    /**
     * Petició per obtenir les dades d'un usuari
     * @return Un usuari amb totes les seves dades
     */
    @GET("/usuari/me")
    suspend fun getUserDetails(): Response<UserResponse>

    @GET("/usuari/{id}")
    suspend fun loadUser(@Path("id") id: Int): Response<UserResponse>

    @PUT("/usuari/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body usuari: UserRequest): Response<UserResponse>

    @PATCH("/usuari/{id}/desactivate")
    suspend fun deactivateUser(@Path("id") id: Int): Response<ApiResponse>

    //Transportista
    @GET("/transportista/usuari/{userId}")
    suspend fun getDeliverymanByUserId(@Path("userId") userId: Int): Response<DeliverymanResponse>

    @POST("/transportista/crear")
    suspend fun createDeliveryman(@Body transportista: DeliverymanRequest): Response<DeliverymanResponse>

    @PUT("transportista/{id}")
    suspend fun updateDeliveryman(@Path("id") id: Long, @Body transportista: DeliverymanRequest): Response<DeliverymanResponse>

    @POST("/transportista/{transportistaId}/assignar-vehicle/{vehicleId}")
    suspend fun assignVehicleToDeliveryman(
        @Path("transportistaId") transportistaId: Long,
        @Path("vehicleId") vehicleId: Long
    ): Response<ApiResponse>

    @PATCH("/transportista/desassignar-vehicle/{id}")
    suspend fun desassignVehicleFromDeliveryman(@Path("id") deliverymanId: Long): Response<ApiResponse>

    //Vehicle
    @GET("/vehicle/{id}")
    suspend fun getVehicleById(@Path("id") id: Long): Response<VehicleDTO>

    @POST("/vehicle/crear")
    suspend fun createVehicle(@Body vehicle: VehicleDTO): Response<VehicleDTO>

    @PUT("/vehicle/{id}")
    suspend fun updateVehicle(@Path("id") id: Long, @Body vehicle: VehicleDTO): Response<VehicleDTO>

    @PATCH("/vehicle/{id}/desactivate")
    suspend fun deactivateVehicle(@Path("id") id: Long): Response<ApiResponse>
}
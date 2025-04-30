package com.inovatech.smartpack.network

import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.api.ApiResponse
import com.inovatech.smartpack.model.api.ChangeStatusRequest
import com.inovatech.smartpack.model.api.DeliverymanRequest
import com.inovatech.smartpack.model.api.ForgotPasswordRequest
import com.inovatech.smartpack.model.api.ForgotPasswordResponse
import com.inovatech.smartpack.model.api.DeliverymanResponse
import com.inovatech.smartpack.model.api.LoginResponse
import com.inovatech.smartpack.model.api.LoginRequest
import com.inovatech.smartpack.model.api.UserRequest
import com.inovatech.smartpack.model.api.ResetPasswordRequest
import com.inovatech.smartpack.model.api.ServiceDTO
import com.inovatech.smartpack.model.api.ServiceHistoricDTO
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

    /**
     * Petició per resetejar la contrasenya
     * @param email: email de l'usuari
     * @return ForgotPasswordResponse: retorna una resposta de l'api del tipus ForgotPasswordResponse
     */
    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body email: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    /**
     * Petició per canviar una contrasenya oblidada
     * @param resetPasswordRequest: petició de canvi de contrasenya amb el token i la nova contrasenya
     * @return ApiResponse: retorna una resposta de l'api del tipus ApiResponse
     */
    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ApiResponse>


    //Usuaris
    /**
     * Petició per obtenir les dades d'un usuari
     * @return Un usuari amb totes les seves dades
     */
    @GET("/usuari/me")
    suspend fun getUserDetails(): Response<UserResponse>

    /**
     * Petició per carregar les dades d'un usuari
     * @param id: id de l'usuari
     * @return Un usuari amb totes les seves dades
     */
    @GET("/usuari/{id}")
    suspend fun loadUser(@Path("id") id: Int): Response<UserResponse>

    /**
     * Petició per modificar les dades d'un usuari
     * @param id: id de l'usuari
     * @param usuari: UserRequest amb les dades de l'usuari. Es canviaran aquelles que siguin diferents
     */
    @PUT("/usuari/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body usuari: UserRequest): Response<UserResponse>

    /**
     * Petició per desactivar un usuari
     * @param id: id de l'usuari
     * @return Retorna una resposta de l'api del tipus ApiResponse
     */
    @PATCH("/usuari/{id}/desactivate")
    suspend fun deactivateUser(@Path("id") id: Long): Response<ApiResponse>

    //Transportista
    /**
     * Petició per obtenir totes les dades d'un transportista a partir del id d'usuari
     * @param userId: id de l'usuari
     * @return Les dades del transportista del tipus DeliverymanResponse
     */
    @GET("/transportista/usuari/{userId}")
    suspend fun getDeliverymanByUserId(@Path("userId") userId: Long): Response<DeliverymanResponse>

    /**
     * Petició per crear un nou transportista
     * @param transportista: Les dades del transportista a crear del tipus DeliverymanRequest
     * @return Les dades del transportista creat
     */
    @POST("/transportista/crear")
    suspend fun createDeliveryman(@Body transportista: DeliverymanRequest): Response<DeliverymanResponse>

    /**
     * Petició per modificar un transportista
     * @param id: id del transportista
     * @param transportista: les dades del transportista modificades del tipus DeliverymanRequest
     * @return Les dades modificades del transportista del tipus DeliverymanResponse
     */
    @PUT("transportista/{id}")
    suspend fun updateDeliveryman(
        @Path("id") id: Long,
        @Body transportista: DeliverymanRequest,
    ): Response<DeliverymanResponse>

    /**
     * Petició per assignar un vehicle a un transportista
     * @param transportistaId: id del transportista
     * @param vehicleId: id del vehicle
     * @return Retorna una resposta de l'api del tipus ApiResponse
     */
    @POST("/transportista/{transportistaId}/assignar-vehicle/{vehicleId}")
    suspend fun assignVehicleToDeliveryman(
        @Path("transportistaId") transportistaId: Long,
        @Path("vehicleId") vehicleId: Long,
    ): Response<ApiResponse>

    /**
     * Petició per desassignar un vehicle d'un transportista
     * @param deliverymanId: id del transportista
     * @return Retorna una resposta de l'api del tipus ApiResponse
     */
    @PATCH("/transportista/desassignar-vehicle/{id}")
    suspend fun desassignVehicleFromDeliveryman(@Path("id") deliverymanId: Long): Response<ApiResponse>

    //Vehicle
    /**
     * Petició per obtenir les dades d'un vehicle a partir del seu id
     * @param id: id del vehicle
     * @return Retorna les dades del vehicle del tipus VehicleDTO
     */
    @GET("/vehicle/{id}")
    suspend fun getVehicleById(@Path("id") id: Long): Response<VehicleDTO>

    /**
     * Petició per crear un vehicle nou
     * @param vehicle: les dades del vehicle a crear del tipus VehicleDTO
     * @return El vehicle creat amb les seves dades del tipus VehicleDTO
     */
    @POST("/vehicle/crear")
    suspend fun createVehicle(@Body vehicle: VehicleDTO): Response<VehicleDTO>

    /**
     * Petició per modificar un vehicle
     * @param id: id del vehicle
     * @param vehicle: Dades del vehicle a modificar del tipus VehicleDTO
     * @return Les dades del vehicle modificades del tipus VehicleDTO
     */
    @PUT("/vehicle/{id}")
    suspend fun updateVehicle(@Path("id") id: Long, @Body vehicle: VehicleDTO): Response<VehicleDTO>

    /**
     * Petició per desactivar un vehicle
     * @param id: id del vehicle
     * @return Retorna una resposta de l'api del tipus ApiResponse
     */
    @PATCH("/vehicle/{id}/desactivate")
    suspend fun deactivateVehicle(@Path("id") id: Long): Response<ApiResponse>


    //Serveis
    /**
     * Petició per crear un nou servei
     * @param service: El servei a crear amb les seves dades del tipus ServiceDTO
     * @return El servei creat del tipus ServiceDTO
     */
    @POST("/servei/crear")
    suspend fun createService(@Body service: ServiceDTO): Response<ServiceDTO>

    /**
     * Petició per modificar un servei existent
     * @param serviceId: La id del servei
     * @param newService: Les dades del servei a modificar del tipus ServiceDTO
     * @return Les dades modificades del tipus ServiceDTO
     */
    @PUT("/servei/{id}")
    suspend fun modifyService(
        @Path("id") serviceId: Long,
        @Body newService: ServiceDTO,
    ): Response<ServiceDTO>

    /**
     * Petició per modificar l'estat d'un servei
     * @param serviceId: La id del servei
     * @param statusRequest: El nou estat del servei
     * @return El servei modificat amb el nou estat del tipus ServiceDTO
     */
    @PATCH("/servei/{serveiId}/estat")
    suspend fun changeServiceStatus(
        @Path("serveiId") serviceId: Long,
        @Body statusRequest: ChangeStatusRequest,
    ): Response<ServiceDTO>

    /**
     * Petició per obtenir una llista de serveis creats per un usuari
     * @param userId: La id de l'usuari
     * @return La llista amb els serveis del tipus ServiceDTO
     */
    @GET("/servei/usuari/{usuariId}")
    suspend fun getServicesPerUser(@Path("usuariId") userId: Long): Response<List<ServiceDTO>>

    /**
     * Petició per obtenir una llista de serveis assignats a un transportista
     * @param deliverymanId: La id del transportista
     * @return La llista de serveis del tipus ServiceDTO
     */
    @GET("/servei/transportista/{transportistaId}")
    suspend fun getServicesPerDeliveryman(@Path("transportistaId") deliverymanId: Long): Response<List<ServiceDTO>>

    /**
     * Petició per obtenir tot l'històric d'estats d'un servei en concret
     * @param serviceId: La id del servei
     * @return Una llista de tots els canvis d'estat d'un servei
     */
    @GET("/servei/{serveiId}/historial")
    suspend fun getServiceHistoric(@Path("serveiId") serviceId: Long): Response<List<ServiceHistoricDTO>>

    /**
     * Petició per desactivar un servei
     * @param serviceId: La id del servei a esborrar
     * @return Retorna una resposta de l'API del tipus ApiResponse
     */
    @PATCH("/servei/{id}/desactivar")
    suspend fun deactivateService(@Path("id") serviceId: Long): Response<ApiResponse>
}
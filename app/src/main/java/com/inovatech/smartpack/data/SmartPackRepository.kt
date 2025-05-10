package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.api.*
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
    suspend fun updateUser(id: Long, usuari: UserRequest): Response<UserResponse>
    suspend fun deactivateUser(id: Long): Response<ApiResponse>
    suspend fun getAllUsers(): Response<List<UserResponse>>

    suspend fun getDeliverymanByUserId(userId: Long): Response<DeliverymanResponse>
    suspend fun getAllDeliverymen(): Response<List<DeliverymanResponse>>
    suspend fun updateDeliveryman(id: Long, transportista: DeliverymanRequest): Response<DeliverymanResponse>
    suspend fun createDeliveryman(transportista: DeliverymanRequest): Response<DeliverymanResponse>
    suspend fun assignVehicleToDeliveryman(transportistaId: Long, vehicleId: Long): Response<ApiResponse>
    suspend fun desassignVehicleFromDeliveryman(deliverymanId: Long): Response<ApiResponse>

    suspend fun getVehicleById(vehicleId: Long): Response<VehicleDTO>
    suspend fun getAllVehicles(): Response<List<VehicleDTO>>
    suspend fun createVehicle(vehicle: VehicleDTO): Response<VehicleDTO>
    suspend fun updateVehicle(id: Long, vehicle: VehicleDTO): Response<VehicleDTO>
    suspend fun deactivateVehicle(id: Long): Response<ApiResponse>

    suspend fun createService(service: ServiceDTO): Response<ServiceDTO>
    suspend fun updateService(serviceId: Long, newService: ServiceDTO): Response<ServiceDTO>
    suspend fun changeServiceStatus(serviceId: Long, statusRequest: ChangeStatusRequest): Response<ServiceDTO>
    suspend fun getServicesPerUser(userId: Long): Response<List<ServiceDTO>>
    suspend fun getServicesPerDeliveryman(deliverymanId: Long): Response<List<ServiceDTO>>
    suspend fun getServiceHistoric(serviceId: Long): Response<List<ServiceHistoricDTO>>
    suspend fun getAllServices(): Response<List<ServiceDTO>>
    suspend fun deactivateService(serviceId: Long): Response<ApiResponse>
    suspend fun deleteService(serviceId: Long): Response<ApiResponse>

    suspend fun createCompany(company: CompanyDTO): Response<CompanyDTO>
    suspend fun updateCompany(companyId: Long, newCompany: CompanyDTO): Response<CompanyDTO>
    suspend fun getAllCompanies(): Response<List<CompanyDTO>>
    suspend fun assignUserToCompany(assignUserToCompanyRequest: AssignUserToCompanyRequest): Response<ApiResponse>
    suspend fun desassignUserFromCompany(userId: Long): Response<ApiResponse>
    suspend fun getUsersAssignedToCompany(companyId: Long): Response<List<UserResponse>>
    suspend fun deactivateCompany(companyId: Long): Response<ApiResponse>
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

    override suspend fun updateUser(id: Long, usuari: UserRequest): Response<UserResponse> {
        return smartPackApiService.updateUser(id, usuari)
    }

    override suspend fun deactivateUser(id: Long): Response<ApiResponse> {
        return smartPackApiService.deactivateUser(id)
    }

    override suspend fun getAllUsers(): Response<List<UserResponse>> {
        return smartPackApiService.getAllUsers()
    }

    override suspend fun getDeliverymanByUserId(userId: Long): Response<DeliverymanResponse> {
        return smartPackApiService.getDeliverymanByUserId(userId)
    }

    override suspend fun getAllDeliverymen(): Response<List<DeliverymanResponse>> {
        return smartPackApiService.getAllDeliverymen()
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

    override suspend fun getAllVehicles(): Response<List<VehicleDTO>> {
        return smartPackApiService.getAllVehicles()
    }

    override suspend fun createVehicle(vehicle: VehicleDTO): Response<VehicleDTO> {
        return smartPackApiService.createVehicle(vehicle)
    }

    override suspend fun updateVehicle(id: Long, vehicle: VehicleDTO): Response<VehicleDTO> {
        return smartPackApiService.updateVehicle(id, vehicle)
    }

    override suspend fun deactivateVehicle(id: Long): Response<ApiResponse> {
        return smartPackApiService.deactivateVehicle(id)
    }

    override suspend fun createService(service: ServiceDTO): Response<ServiceDTO> {
        return smartPackApiService.createService(service)
    }

    override suspend fun updateService(
        serviceId: Long,
        newService: ServiceDTO,
    ): Response<ServiceDTO> {
        return smartPackApiService.updateService(serviceId, newService)
    }

    override suspend fun changeServiceStatus(
        serviceId: Long,
        statusRequest: ChangeStatusRequest,
    ): Response<ServiceDTO> {
        return smartPackApiService.changeServiceStatus(serviceId, statusRequest)
    }

    override suspend fun getServicesPerUser(userId: Long): Response<List<ServiceDTO>> {
        return smartPackApiService.getServicesPerUser(userId)
    }

    override suspend fun getServicesPerDeliveryman(deliverymanId: Long): Response<List<ServiceDTO>> {
        return smartPackApiService.getServicesPerDeliveryman(deliverymanId)
    }

    override suspend fun getServiceHistoric(serviceId: Long): Response<List<ServiceHistoricDTO>> {
        return smartPackApiService.getServiceHistoric(serviceId)
    }

    override suspend fun getAllServices(): Response<List<ServiceDTO>> {
        return smartPackApiService.getAllServices()
    }

    override suspend fun deactivateService(serviceId: Long): Response<ApiResponse> {
        return smartPackApiService.deactivateService(serviceId)
    }
    override suspend fun deleteService(serviceId: Long): Response<ApiResponse> {
        return smartPackApiService.deleteService(serviceId)
    }

    override suspend fun createCompany(company: CompanyDTO): Response<CompanyDTO> {
        return smartPackApiService.createCompany(company)
    }

    override suspend fun updateCompany(
        companyId: Long,
        newCompany: CompanyDTO,
    ): Response<CompanyDTO> {
        return smartPackApiService.updateCompany(companyId, newCompany)
    }

    override suspend fun getAllCompanies(): Response<List<CompanyDTO>> {
        return smartPackApiService.getAllCompanies()
    }

    override suspend fun assignUserToCompany(assignUserToCompanyRequest: AssignUserToCompanyRequest): Response<ApiResponse> {
        return smartPackApiService.assignUserToCompany(assignUserToCompanyRequest)
    }

    override suspend fun desassignUserFromCompany(userId: Long): Response<ApiResponse> {
        return smartPackApiService.desassignUserFromCompany(userId)
    }

    override suspend fun getUsersAssignedToCompany(companyId: Long): Response<List<UserResponse>> {
        return smartPackApiService.getUsersAssignedToCompany(companyId)
    }

    override suspend fun deactivateCompany(companyId: Long): Response<ApiResponse> {
        return smartPackApiService.deactivateCompany(companyId)
    }
}


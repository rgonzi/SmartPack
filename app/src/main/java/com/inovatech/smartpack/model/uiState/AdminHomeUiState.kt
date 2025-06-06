package com.inovatech.smartpack.model.uiState

import com.inovatech.smartpack.model.Company
import com.inovatech.smartpack.model.Deliveryman
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.model.UserFilter
import com.inovatech.smartpack.model.Vehicle

/**
 * Classe que defineix l'estat de la UI de la pantalla d'inici de l'usuari administrador.
 */
data class AdminHomeUiState(
    val selectedUser: User? = null,
    val selectedService: Service? = null,
    val selectedVehicle: Vehicle? = null,
    val selectedCompany: Company? = null,
//    val selectedInvoice: Invoice? = null,
    val usersList: List<User> = emptyList(),
    val servicesList: List<Service> = emptyList(),
    val companiesList: List<Company> = emptyList(),
    val deliverymenList: List<Deliveryman> = emptyList(),
//    val invoicesList: List<Invoice> = emptyList(),
    val vehiclesList: List<Vehicle> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val msg: String? = null,
) {
    //Creem una llista de les id d'usuari que corresponen a transportistes
    private val deliverymanIds = deliverymenList.map { it.userId }

    //Mapegem la llista d'usuaris per posar el rol de transportista als qui coincideixi la id amb la llista anterior
    val usersWithDeliverymanRole: List<User>
        get() = usersList.map { user ->
            val role = if (deliverymanIds.contains(user.id)) Role.ROLE_DELIVERYMAN else null
            user.copy(role = role)
        }

    //Combinem els filtres per rol amb chips i el filtre de la barra de cerca
    fun filteredUsers(
        searchQuery: String,
        filter: UserFilter,
    ): List<User> = usersWithDeliverymanRole
        .filter { user ->
            searchQuery.isBlank()
                    || user.name!!.contains(searchQuery, true)
                    || user.email!!.contains(searchQuery, true)
        }
        .filter { user ->
            when (filter) {
                UserFilter.ALL -> true
                UserFilter.USER -> user.role == null
                UserFilter.DELIVERYMAN -> user.role == Role.ROLE_DELIVERYMAN
            }
        }

    val filteredServices: List<Service>
        get() = servicesList.filter {
            (searchQuery.isBlank()
                    || it.id.toString().contains(searchQuery, true)
                    || it.packageToDeliver.recipientName.contains(searchQuery, true)
                    || it.packageToDeliver.recipientPhone.contains(searchQuery, true))
        }
    val filteredCompanies: List<Company>
        get() = companiesList.filter {
            (searchQuery.isBlank()
                    || it.nif.contains(searchQuery, true)
                    || it.email.contains(searchQuery, true)
                    || it.phone.contains(searchQuery, true))
        }

    val filteredVehicles: List<Vehicle>
        get() = vehiclesList.filter {
            (searchQuery.isBlank() || it.plate.contains(searchQuery, true))
        }

}

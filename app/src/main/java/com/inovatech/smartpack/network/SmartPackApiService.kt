package com.inovatech.smartpack.network

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.model.LoginRequest
import com.inovatech.smartpack.model.RegisterRequest
import com.inovatech.smartpack.model.Usuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfície necessària per Retrofit per definir els endpoints de l'API a l'hora de fer les
 * diferents peticions disponibles
 */
interface SmartPackApiService {

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
    suspend fun register(@Body usuari: RegisterRequest): Response<Usuari>
}
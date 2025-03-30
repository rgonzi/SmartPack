package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.auth.LoginRequest
import com.inovatech.smartpack.model.UserRequest
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.network.SmartPackApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Random

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkSmartPackRepositoryTest {

    private lateinit var repository: NetworkSmartPackRepository

    private lateinit var apiService: SmartPackApiService

    @Before
    fun setUp() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(SmartPackApiService::class.java)
        repository = NetworkSmartPackRepository(apiService)
    }

    /**
     * Test que verifica un registre correcte d'un usuari amb un email aleatori.
     */
    @Test
    fun testRegisterSuccess() = runTest {
        val randomEmail = Random().nextInt(1000).toString() + "@test.com"
        val request = UserRequest(
            email = randomEmail,
            password = "1234567A",
            role = Role.ROLE_USER,
            name = "Test",
            surname = "Test test",
            tel = "123456789",
            address = "some address"
        )
        val response = repository.register(request)

        assertTrue(response.isSuccessful)
        assertEquals(200, response.code())
        assertEquals(randomEmail, response.body()?.email)
    }

    /**
     * Test que verifica un inici de sessió d'un usuari existent.
     */
    @Test
    fun testLoginSuccess() = runTest {
        val request = LoginRequest("test@test.com", "1234567A")
        val response = repository.login(request)
        advanceUntilIdle()

        assertTrue(response.isSuccessful)
        assertEquals(200, response.code())
        assertNotNull(response.body()?.token)
    }

    /**
     * Test que verifica un inici de sessió no vàlid d'un usuari que no existeix a la BBDD.
     */
    @Test
    fun testUnauthorizedLogin() = runTest {
        val email = "william@gmail.com"
        val pass = "Password1"

        val response = repository.login(LoginRequest(email, pass))
        advanceUntilIdle()

        assertFalse(response.isSuccessful)
        assertEquals(403, response.code())
        assertNull(response.body())
    }
}

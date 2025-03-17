package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.network.SmartPackApiService
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import retrofit2.Response

class NetworkSmartPackRepositoryTest {

    private lateinit var repository: NetworkSmartPackRepository
    private val mockApiService: SmartPackApiService = mock(SmartPackApiService::class.java)

    @Before
    fun setUp() {
        repository = NetworkSmartPackRepository(mockApiService)
    }

    @Test
    fun testLoginSuccess() = runBlocking {
        val mockResponse = Response.success(LoginResponse("fake_token"))
        whenever(mockApiService.login("email", "password")).thenReturn(mockResponse)

        val result = repository.login("email", "password")

        assert(result.isSuccessful)
        assert(result.body()?.token == "fake_token")
    }

    @Test
    fun testLoginError() = runBlocking {
        val mockResponse = Response.error<LoginResponse>(401, "".toResponseBody(null))
        whenever(mockApiService.login("email", "password")).thenReturn(mockResponse)

        val result = repository.login("email", "password")

        assert(!result.isSuccessful)
    }
}

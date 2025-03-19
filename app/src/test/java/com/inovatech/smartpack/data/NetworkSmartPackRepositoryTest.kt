package com.inovatech.smartpack.data

import com.inovatech.smartpack.model.LoginRequest
import com.inovatech.smartpack.model.LoginResponse
import com.inovatech.smartpack.network.SmartPackApiService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.Date

class NetworkSmartPackRepositoryTest {

    private lateinit var repository: NetworkSmartPackRepository
    private val mockApiService: SmartPackApiService = mock(SmartPackApiService::class.java)

    @Before
    fun setUp() {
        repository = NetworkSmartPackRepository(mockApiService)
    }

    @Test
    fun testLoginSuccess() = runTest {
        val date: Date = Date()
        val mockResponse = Response.success(LoginResponse("fake_token", date))
        val request = LoginRequest("email", "password")

        whenever(mockApiService.login(request)).thenReturn(mockResponse)

        val result = repository.login(request)

        assert(result.isSuccessful)
        assert(result.body()?.token == "fake_token")
    }

    @Test
    fun testLoginError() = runTest {
        val mockResponse = Response.error<LoginResponse>(401, "".toResponseBody(null))
        val request = LoginRequest("email", "password")

        whenever(mockApiService.login(request)).thenReturn(mockResponse)

        val result = repository.login(request)

        assert(!result.isSuccessful)
    }
}

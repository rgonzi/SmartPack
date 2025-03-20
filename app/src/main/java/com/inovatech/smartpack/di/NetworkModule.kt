package com.inovatech.smartpack.di

import com.inovatech.smartpack.data.NetworkSmartPackRepository
import com.inovatech.smartpack.data.SmartPackRepository
import com.inovatech.smartpack.network.SmartPackApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Mòdul de Dagger Hilt necessari per poder injectar les dependències necessàries per a Retrofit i
 * el repositori principal
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://localhost:8080"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideSmartPackApiService(retrofit: Retrofit): SmartPackApiService {
        return retrofit.create(SmartPackApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSmartPackRepository(
        apiService: SmartPackApiService,
    ): SmartPackRepository {
        return NetworkSmartPackRepository(apiService)
    }
}
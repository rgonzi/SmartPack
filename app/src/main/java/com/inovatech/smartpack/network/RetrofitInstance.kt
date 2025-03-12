package com.inovatech.smartpack.network

import com.inovatech.smartpack.data.NetworkSmartPackRepository
import com.inovatech.smartpack.data.SmartPackRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val baseUrl = "" //TODO Base URL

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val apiService: SmartPackApiService by lazy {
        retrofit.create(SmartPackApiService::class.java)
    }

    val smartPackRepository: SmartPackRepository by lazy {
        NetworkSmartPackRepository(apiService)
    }

}
package com.yhezra.ayokelas.api

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientCovid {
    private const val BASE_URL = "https://api.kawalcorona.com/"

    val instance:CovidApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(CovidApi::class.java)
    }
}
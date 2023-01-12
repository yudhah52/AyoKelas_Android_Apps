package com.yhezra.ayokelas.api

import com.yhezra.ayokelas.model.IndonesiaResponse
import retrofit2.Call
import retrofit2.http.GET

interface CovidApi {
    @GET("indonesia")
    fun getIndonesia(): Call<ArrayList<IndonesiaResponse>> //array list digunakan untuk menangkap json array response https://api.kawalcorona.com/indonesia
    //IndonesiaResponse akan menangkap object response
}
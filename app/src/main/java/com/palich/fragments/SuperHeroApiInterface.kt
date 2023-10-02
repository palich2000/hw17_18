package com.palich.fragments

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SuperHeroApiInterface {
    @GET("superhero-api/api/all.json")
    suspend fun getSuperHero(): MutableList<SuperHero>
}

class SuperHeroApiClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val clientOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val client: Retrofit = Retrofit.Builder()
        .baseUrl("https://akabab.github.io/")
        .client(clientOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

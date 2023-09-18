package com.palich.fragments

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SuperHeroApiInterface {
    @GET("superhero-api/api/all.json")
    fun getSuperHero(): Single<MutableList<SuperHero>>
}

class SuperHeroApiClient {
    val client: Retrofit = Retrofit.Builder()
        .baseUrl("https://akabab.github.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okhttp3.OkHttpClient())
        .build()
}

package org.ktc2.cokaen.wouldyouin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerAPIRetrofitClient {
    val BASE_URL ="서버주소"

    val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()


    fun getInstance(): Retrofit {
        return client
    }
}
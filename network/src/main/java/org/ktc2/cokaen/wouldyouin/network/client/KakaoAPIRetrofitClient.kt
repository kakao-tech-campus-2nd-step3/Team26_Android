package org.ktc2.cokaen.wouldyouin.network.client

import okhttp3.OkHttpClient
import org.ktc2.cokaen.wouldyouin.network.AuthInterceptor
import org.ktc2.cokaen.wouldyouin.network.BuildConfig
import org.ktc2.cokaen.wouldyouin.network.service.KakaoAPIRetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoAPIRetrofitClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(BuildConfig.KAKAO_REST_API_KEY))
        .build()

    val retrofitService: KakaoAPIRetrofitService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoAPIRetrofitService::class.java)
    }
}
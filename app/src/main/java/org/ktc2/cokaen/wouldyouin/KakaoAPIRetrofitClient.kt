package org.ktc2.cokaen.wouldyouin

import okhttp3.OkHttpClient
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
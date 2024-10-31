package org.ktc2.cokaen.wouldyouin.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKakaoRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.KAKAO_REST_API_KEY))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideServerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("서버주소")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoService(retrofit: Retrofit): KakaoAPIRetrofitService {
        return retrofit.create(KakaoAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideServerService(retrofit: Retrofit): ServerAPIRetrofitService {
        return retrofit.create(ServerAPIRetrofitService::class.java)
    }
}


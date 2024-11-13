package org.ktc2.cokaen.wouldyouin.network

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.ktc2.cokaen.wouldyouin.network.service.CurationAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.EventAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.KakaoAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.ServerAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.MemberAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.ReservationAPIRetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("Kakao")
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
    @Named("Server")
    fun provideServerRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("OkHttp", "Request URL: ${request.url}")
                Log.d("OkHttp", "Request Method: ${request.method}")
                Log.d("OkHttp", "Request Headers: ${request.headers}")
                Log.d("OkHttp", "Request Body: ${request.body}")

                val response = chain.proceed(request)
                Log.d("OkHttp", "Response Code: ${response.code}")
                Log.d("OkHttp", "Response Message: ${response.message}")
                Log.d("OkHttp", "Response Headers: ${response.headers}")

                response
            }
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://wouldyouin.store")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoService(@Named("Kakao") retrofit: Retrofit): KakaoAPIRetrofitService {
        return retrofit.create(KakaoAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideServerService(@Named("Server") retrofit: Retrofit): ServerAPIRetrofitService {
        return retrofit.create(ServerAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurationAPIRetrofitService(@Named("Server") retrofit: Retrofit): CurationAPIRetrofitService {
        return retrofit.create(CurationAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideEventAPIRetrofitService(@Named("Server") retrofit: Retrofit): EventAPIRetrofitService {
        return retrofit.create(EventAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberAPIRetrofitService(@Named("Server") retrofit: Retrofit): MemberAPIRetrofitService {
        return retrofit.create(MemberAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideReservationAPIRetrofitService(@Named("Server") retrofit: Retrofit): ReservationAPIRetrofitService {
        return retrofit.create(ReservationAPIRetrofitService::class.java)
    }
}


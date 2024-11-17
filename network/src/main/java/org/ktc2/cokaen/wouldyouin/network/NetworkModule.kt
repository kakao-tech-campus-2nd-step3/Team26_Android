package org.ktc2.cokaen.wouldyouin.network

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.ktc2.cokaen.wouldyouin.network.service.AdAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.CurationAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.EventAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.KakaoAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.LikesAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.AuthAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.ServerAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.MemberAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.ReservationAPIRetrofitService
import org.ktc2.cokaen.wouldyouin.network.service.ReviewAPIRetrofitService
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
    @Named("mainClient")
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: ServerAuthInterceptor  // Hilt가 주입
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)  // 인증 인터셉터 추가
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()

                // 요청 로그
                Log.d("OkHttp", "Request URL: ${request.url}")
                Log.d("OkHttp", "Request Method: ${request.method}")
                Log.d("OkHttp", "Request Headers: ${request.headers}")
                request.body?.let {
                    val buffer = okio.Buffer()
                    it.writeTo(buffer)
                    Log.d("OkHttp", "Request Body: ${buffer.readUtf8()}")
                }

                // 응답 처리
                val response = chain.proceed(request)

                // 응답 Body 읽기
                val responseBodyString = response.body?.string() ?: "No Response Body"

                // 응답 로그
                Log.d("OkHttp", "Response Code: ${response.code}")
                Log.d("OkHttp", "Response Message: ${response.message}")
                Log.d("OkHttp", "Response Headers: ${response.headers}")
                Log.d("OkHttp", "Response Body: $responseBodyString")  // 응답 본문 로그 출력

                // 응답 본문을 다시 복원
                val contentType = response.body?.contentType()
                val restoredBody = responseBodyString.toResponseBody(contentType)

                // 복원된 응답 본문을 사용하여 응답을 반환
                return@addInterceptor response.newBuilder().body(restoredBody).build()  // 복원된 응답을 반환
            }


            .build()
    }

    @Provides
    @Singleton
    @Named("Server")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
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

    @Provides
    @Singleton
    fun provideLikesAPIRetrofitService(@Named("Server") retrofit: Retrofit): LikesAPIRetrofitService {
        return retrofit.create(LikesAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewAPIRetrofitService(@Named("Server") retrofit: Retrofit): ReviewAPIRetrofitService {
        return retrofit.create(ReviewAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdAPIRetrofitService(@Named("Server") retrofit: Retrofit): AdAPIRetrofitService {
        return retrofit.create(AdAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthAPIRetrofitService(@Named("Server") retrofit: Retrofit): AuthAPIRetrofitService {
        return retrofit.create(AuthAPIRetrofitService::class.java)
    }
}


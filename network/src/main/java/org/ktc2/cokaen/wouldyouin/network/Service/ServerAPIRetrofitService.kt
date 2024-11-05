package org.ktc2.cokaen.wouldyouin.network.Service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServerAPIRetrofitService {
    @Multipart
    @POST("api/images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageResponse>
}

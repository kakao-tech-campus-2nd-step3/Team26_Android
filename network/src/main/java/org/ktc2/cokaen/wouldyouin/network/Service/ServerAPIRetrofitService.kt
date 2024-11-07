package org.ktc2.cokaen.wouldyouin.network.Service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyVoid
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageUploadRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerAPIRetrofitService {
    @Multipart
    @POST("api/images")
    suspend fun uploadImages(
        @Body request: ImageUploadRequest
    ): Response<ApiResponseBodyListImageResponse>

    @DELETE("api/images/{id}")
    suspend fun deleteImage(
        @Path("id") id: Long,
        @Query("imageDomain") imageDomain: String
    ): Response<ApiResponseBodyVoid>
}

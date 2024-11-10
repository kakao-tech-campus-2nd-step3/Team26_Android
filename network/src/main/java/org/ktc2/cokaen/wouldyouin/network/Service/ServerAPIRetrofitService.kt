package org.ktc2.cokaen.wouldyouin.network.Service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyVoid
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageUploadRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

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

    @GET("images/{path}")
    @Streaming  // 큰 파일을 위한 스트리밍 지원
    suspend fun loadImage(
        @Path("path") path: String
    ): Response<ResponseBody>
}

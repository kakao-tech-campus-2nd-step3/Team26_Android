package org.ktc2.cokaen.wouldyouin.network.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyVoid
import retrofit2.Response
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
    @POST("/api/images")
    suspend fun uploadImageWithPart(
        @Query("type") type: String,
        @Part images: MultipartBody.Part
    ): Response<ApiResponseBodyListImageResponse>

    @DELETE("/api/images/{id}")
    suspend fun deleteImage(
        @Path("id") id: Long,
        @Query("type") type: String
    ): Response<ResponseBody>
}

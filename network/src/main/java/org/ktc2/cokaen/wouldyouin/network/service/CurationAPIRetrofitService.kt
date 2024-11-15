package org.ktc2.cokaen.wouldyouin.network.service

import okhttp3.ResponseBody
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyCurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyCurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CurationAPIRetrofitService {
    @POST("/api/curations")
    suspend fun createCuration(
        @Body request: CurationCreateRequest
    ): Response<ApiResponseBodyCurationResponse>

    @PUT("/api/curations/{curationId}")
    suspend fun updateCuration(
        @Body request: CurationEditRequest
    ): Response<ApiResponseBodyCurationResponse>

    @GET("/api/curations")
    suspend fun getCurationList(
        @Query("area") area: String = "전체",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyCurationSliceResponse>

    @GET("/api/curations/{curationId}")
    suspend fun getCurationDetail(
        @Path("curationId") curationId: Long
    ): Response<ApiResponseBodyCurationResponse>

    @GET("/api/curations/curators/{curatorId}")
    suspend fun getCurationsByCurator(
        @Path("curatorId") curatorId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("lastId") lastId: Long = Long.MAX_VALUE
    ): Response<ApiResponseBodyCurationSliceResponse>

    @DELETE("/api/curations/curators/{curationId}")
    suspend fun deleteCuration(
        @Path("curationId") curatorId: Long
    ): Response<ResponseBody>
}
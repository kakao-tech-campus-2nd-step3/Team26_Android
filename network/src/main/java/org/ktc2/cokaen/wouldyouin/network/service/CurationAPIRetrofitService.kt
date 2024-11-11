package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyCurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CurationAPIRetrofitService {
    @POST("/api/curations")
    suspend fun createCuration(
        @Body request: CurationCreateRequestWrapper
    ): Response<ApiResponseBodyCurationResponse>

    @PUT("/api/curations/{curationId}")
    suspend fun updateCuration(
        @Path("curationId") curationId: Long,
        @Body request: CurationEditRequestWrapper
    ): Response<ApiResponseBodyCurationResponse>
}
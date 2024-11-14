package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListAdvertisementResponse
import retrofit2.Response
import retrofit2.http.GET

interface AdAPIRetrofitService {

    //광고 목록 조회
    @GET("/api/ads")
    suspend fun getAdList(): Response<ApiResponseBodyListAdvertisementResponse>
}
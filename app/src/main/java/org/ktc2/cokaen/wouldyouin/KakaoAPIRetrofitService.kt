package org.ktc2.cokaen.wouldyouin

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoAPIRetrofitService {
    @GET("v2/local/search/keyword.json")

    suspend fun getSearchKeyword(
        @Query("query") query: String
    ): KakaoSearchData
}
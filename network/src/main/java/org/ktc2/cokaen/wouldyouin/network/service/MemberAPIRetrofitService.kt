package org.ktc2.cokaen.wouldyouin.network.service

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyMemberResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//멤버 정보 조회
interface MemberAPIRetrofitService {
    @GET("/api/members/{memberId}")
    suspend fun getMemberProfile(
        @Path("memberId") memberId: Long
    ): Response<ApiResponseBodyMemberResponse>
}
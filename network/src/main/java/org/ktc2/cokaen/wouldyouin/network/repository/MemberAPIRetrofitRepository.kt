package org.ktc2.cokaen.wouldyouin.network.repository

import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyMemberResponse
import org.ktc2.cokaen.wouldyouin.network.service.MemberAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberAPIRetrofitRepository @Inject constructor(
    private val retrofitService: MemberAPIRetrofitService
) {
    suspend fun getMemberProfile(memberId: Long): ApiResponseBodyMemberResponse? {
        return try {
            val response = retrofitService.getMemberProfile(memberId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
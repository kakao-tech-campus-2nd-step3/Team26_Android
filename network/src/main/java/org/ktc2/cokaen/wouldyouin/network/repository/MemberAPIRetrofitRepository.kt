package org.ktc2.cokaen.wouldyouin.network.repository

import android.content.Context
import android.util.Log
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyMemberResponse
import org.ktc2.cokaen.wouldyouin.network.service.MemberAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberAPIRetrofitRepository @Inject constructor(
    private val retrofitService: MemberAPIRetrofitService
) {
    suspend fun getMemberProfile(memberId: Long, context: Context): ApiResponseBodyMemberResponse? {
        return try {
            Log.d("MemberAPIRetrofitRepository", "Fetching member profile for memberId: $memberId")

            val response = retrofitService.getMemberProfile(memberId)
            Log.d("MemberAPIRetrofitRepository", "Received response with code: ${response.code()}")

            if (response.isSuccessful) {
                Log.d("MemberAPIRetrofitRepository", "Member profile fetched successfully. Body: ${response.body()}")
                response.body()
            } else {
                Log.e("MemberAPIRetrofitRepository", "Failed to fetch member profile. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                ToastUtils.showShortToast(context, "회원 프로필 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            Log.e("MemberAPIRetrofitRepository", "Exception occurred while fetching member profile: ${e.message}", e)
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }
}
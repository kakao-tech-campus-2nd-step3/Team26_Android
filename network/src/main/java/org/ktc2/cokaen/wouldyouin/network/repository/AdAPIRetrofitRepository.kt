package org.ktc2.cokaen.wouldyouin.network.repository

import android.content.Context
import android.util.Log
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyListAdvertisementResponse
import org.ktc2.cokaen.wouldyouin.network.service.AdAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdAPIRetrofitRepository @Inject constructor(
    private val retrofitService: AdAPIRetrofitService
) {
    //전체 광고 목록 조회
    suspend fun getAdList(context: Context): ApiResponseBodyListAdvertisementResponse? {
        return try {
            Log.d("AdAPIRetrofitRepository", "Fetching ad list")

            val response = retrofitService.getAdList()
            if (response.isSuccessful) {
                Log.d("AdAPIRetrofitRepository", "Ad list fetched successfully. Body: ${response.body()}")
                response.body()
            } else {
                Log.e("AdAPIRetrofitRepository", "Failed to fetch ad list. Code: ${response.code()}, Message: ${response.message()}, Error: ${response.errorBody()?.string()}")
                ToastUtils.showShortToast(context, "광고 목록 조회에 실패했습니다.")
                null
            }
        } catch (e: Exception) {
            Log.e("AdAPIRetrofitRepository", "Exception occurred in Ad list: ${e.message}", e)
            ToastUtils.showShortToast(context, "오류 발생: ${e.message}")
            null
        }
    }
}
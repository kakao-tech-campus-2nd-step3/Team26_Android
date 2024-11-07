package org.ktc2.cokaen.wouldyouin.network.Repository

import android.content.Context
import okhttp3.MultipartBody
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.network.Service.ServerAPIRetrofitService
import javax.inject.Inject

open class ServerCommonAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ServerAPIRetrofitService
) {
    // 공통으로 사용하는 기능 ex) 이미지 업로드, 이미지 로드 등 기능
    open suspend fun uploadImage(imageFile: MultipartBody.Part, context: Context): ImageResponse? {
        return try {
            val response = retrofitService.uploadImage(imageFile)
            if (response.isSuccessful) {
                response.body() // 성공적으로 응답 처리
            } else {
                ToastUtils.showShortToast(context, "이미지 업로드에 실패했습니다. 다시 시도해주세요.")
                null
            }
        } catch (e: Exception) {
            ToastUtils.showShortToast(context, "이미지 업로드 중 오류가 발생했습니다. 다시 시도해주세요.")
            null // 예외 처리
        }
    }
}
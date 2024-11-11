package org.ktc2.cokaen.wouldyouin.network.repository

import android.app.Application
import android.util.Log
import okhttp3.MultipartBody
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.network.service.ServerAPIRetrofitService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// 공통 기능 구현
@Singleton
open class ServerCommonAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ServerAPIRetrofitService,
    private val application: Application
) {
    suspend fun uploadImageWithPart(imagePart: MultipartBody.Part, imageDomain: String): ImageResponse {
        try {
            Log.d("ImageUpload", "==== Request Details ====")
            Log.d("ImageUpload", "Content-Disposition: ${imagePart.headers?.get("Content-Disposition")}")
            Log.d("ImageUpload", "Content-Type: ${imagePart.headers?.get("Content-Type")}")
            Log.d("ImageUpload", "Image Domain: $imageDomain")

            val response = retrofitService.uploadImageWithPart(
                type = imageDomain.uppercase(),
                images = imagePart
            )

            Log.d("ImageUpload", "Response Code: ${response.code()}")
            Log.d("ImageUpload", "Response Headers: ${response.headers()}")

            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data?.firstOrNull()
                                ?: throw CustomException("이미지 데이터가 없습니다")
                        } else {
                            throw CustomException(body.message ?: "이미지 업로드에 실패했습니다")
                        }
                    } ?: throw CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ImageUpload", "Error Response: $errorBody")
                    throw CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("ImageUpload", "Upload failed", e)
            throw when(e) {
                is IOException -> CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    class CustomException(message: String) : Exception(message)

    suspend fun deleteImage(imageId: Long, type: String): Boolean {
        return try {
            val response = retrofitService.deleteImage(imageId, type)
            if (response.isSuccessful && response.body()?.success == true) {
                true
            } else {
                throw Exception(response.body()?.message ?: "이미지 삭제에 실패했습니다.")
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다")
                else -> throw e
            }
        }
    }
}
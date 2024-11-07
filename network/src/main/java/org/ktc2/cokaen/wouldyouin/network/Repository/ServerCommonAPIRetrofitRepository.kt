package org.ktc2.cokaen.wouldyouin.network.Repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import okhttp3.MultipartBody
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageUploadRequest
import org.ktc2.cokaen.wouldyouin.network.Service.ServerAPIRetrofitService
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// 공통 기능 구현
@Singleton
open class ServerCommonAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ServerAPIRetrofitService,
    private val application: Application
) {
    private val context = application.applicationContext

    suspend fun uploadImage(uri: Uri, type: String): String {
        try {
            val file = getFileFromUri(uri)

            try {
                val base64Image = file.inputStream().use { input ->
                    Base64.encodeToString(input.readBytes(), Base64.DEFAULT)
                }

                val request = ImageUploadRequest(
                    type = type,
                    images = listOf(base64Image)
                )

                val response = retrofitService.uploadImages(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    return response.body()?.data?.firstOrNull()?.url
                        ?: throw Exception("이미지 URL을 받지 못했습니다")
                } else {
                    throw Exception(when(response.code()) {
                        413 -> "파일이 너무 큽니다"
                        401 -> "인증에 실패했습니다"
                        500 -> "서버 오류가 발생했습니다"
                        else -> response.body()?.message
                            ?: "업로드에 실패했습니다 (${response.code()})"
                    })
                }
            } finally {
                file.delete()
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다")
                is OutOfMemoryError -> throw Exception("이미지 크기가 너무 큽니다")
                else -> throw e
            }
        }
    }

    suspend fun uploadMultipleImages(uris: List<Uri>, type: String): List<String> {
        try {
            val base64Images = uris.map { uri ->
                val file = getFileFromUri(uri)
                try {
                    file.inputStream().use { input ->
                        Base64.encodeToString(input.readBytes(), Base64.DEFAULT)
                    }
                } finally {
                    file.delete()
                }
            }

            val request = ImageUploadRequest(
                type = type,
                images = base64Images
            )

            val response = retrofitService.uploadImages(request)
            if (response.isSuccessful && response.body()?.success == true) {
                return response.body()?.data?.map { it.url }
                    ?: throw Exception("이미지 URL을 받지 못했습니다")
            } else {
                throw Exception(when(response.code()) {
                    413 -> "파일이 너무 큽니다"
                    401 -> "인증에 실패했습니다"
                    500 -> "서버 오류가 발생했습니다"
                    else -> response.body()?.message
                        ?: "업로드에 실패했습니다 (${response.code()})"
                })
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다")
                is OutOfMemoryError -> throw Exception("이미지 크기가 너무 큽니다")
                else -> throw e
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open input stream for uri: $uri")

        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")

        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

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
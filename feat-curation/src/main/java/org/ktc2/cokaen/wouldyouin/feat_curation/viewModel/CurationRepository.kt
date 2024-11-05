package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import org.ktc2.cokaen.wouldyouin.network.Service.ServerAPIRetrofitService
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurationRepository @Inject constructor(
    private val apiService: ServerAPIRetrofitService,
    application: Application
) {
    private val context = application.applicationContext
    suspend fun uploadImage(uri: Uri): String {
        try {
            val file = getFileFromUri(uri)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            try {
                val response = apiService.uploadImage(imagePart)
                if (response.isSuccessful && response.body() != null) {
                    return response.body()!!.url
                } else {
                    throw Exception(when(response.code()) {
                        413 -> "파일이 너무 큽니다"
                        401 -> "인증에 실패했습니다"
                        500 -> "서버 오류가 발생했습니다"
                        else -> "업로드에 실패했습니다 (${response.code()})"
                    })
                }
            } finally {
                file.delete()
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다")
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

    suspend fun getCurationDetail(curationId: String): CurationRespond? {
       return null
    }

    fun getAllCurations(): List<CurationRespond>? {
        return null
    }

}
package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import org.ktc2.cokaen.wouldyouin.network.Repository.ServerCommonAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.Service.ServerAPIRetrofitService
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurationRepository @Inject constructor(
    private val commonRepository: ServerCommonAPIRetrofitRepository
) {
    suspend fun uploadImage(uri: Uri): String {
        return commonRepository.uploadImage(uri, "CURATION")
    }

    suspend fun uploadMultipleImages(uris: List<Uri>): List<String> {
        return commonRepository.uploadMultipleImages(uris, "CURATION")
    }

    suspend fun deleteImage(imageId: Long): Boolean {
        return commonRepository.deleteImage(imageId, "CURATION")
    }

}
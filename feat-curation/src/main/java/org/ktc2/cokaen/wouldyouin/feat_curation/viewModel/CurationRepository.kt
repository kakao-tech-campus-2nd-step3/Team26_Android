package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CreateCurationRequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.service.ServerAPIRetrofitService
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurationRepository @Inject constructor(
    private val commonRepository: ServerCommonAPIRetrofitRepository,
    private val curationRepository: CurationAPIRetrofitRepository
) {

    suspend fun uploadImageWithPart(imagePart: MultipartBody.Part): ImageResponse {
        return commonRepository.uploadImageWithPart(imagePart, "CURATION")
    }

    suspend fun createCuration(requestBody: CurationCreateRequestWrapper): CurationResponse {
        return curationRepository.createCuration(requestBody)
    }

    suspend fun  updateCuration(requestBody: CurationEditRequestWrapper): CurationResponse {
        return curationRepository.updateCuration(requestBody)
    }

    suspend fun deleteImage(imageId: Long): Boolean {
        return commonRepository.deleteImage(imageId, "CURATION")
    }

}
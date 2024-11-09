package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.net.Uri
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
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
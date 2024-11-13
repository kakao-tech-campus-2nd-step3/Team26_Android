package org.ktc2.cokaen.wouldyouin.feat_curation.repository

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CreateCurationRequestBody
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
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
    private val curationRepository: CurationAPIRetrofitRepository,
    private val eventRepository: EventAPIRetrofitRepository
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

   suspend fun getCurationList(
       area: String = "전체",
       page: Int = 0,
       size: Int = 10,
       lastId: Long = Long.MAX_VALUE
   ): CurationSliceResponse {
       return curationRepository.getCurationList(area, page, size, lastId)
   }

    suspend fun getCurationDetail(curatorId: Long): CurationResponse {
        return curationRepository.getCurationDetail(curatorId)
    }

    suspend fun searchEvents(
        query: String,
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        latitude: Double,
        longitude: Double,
        page: Int = 1,
        size: Int = 10,
        context: Context
    ): List<EventResponse>? {
        return eventRepository.searchEvents(query, startLatitude, startLongitude, endLatitude, endLongitude, latitude, longitude, page, size, context)
    }
}
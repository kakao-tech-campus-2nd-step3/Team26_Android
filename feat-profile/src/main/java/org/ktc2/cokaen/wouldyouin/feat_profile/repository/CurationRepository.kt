package org.ktc2.cokaen.wouldyouin.feat_profile.repository

import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurationRepository @Inject constructor(
    private val curationRepository: CurationAPIRetrofitRepository
) {
    suspend fun  getCurationsByCurator(
        curationId: Long,
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): CurationSliceResponse {
        return curationRepository.getCurationsByCurator(curationId, page, size, lastId)
    }
}
package org.ktc2.cokaen.wouldyouin.data.repository

import org.ktc2.cokaen.wouldyouin.data.dao.CurationDao
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurationLocalRepository @Inject constructor(private val curationDao: CurationDao) {

    suspend fun insertCuration(curation: CurationEntity) {
        curationDao.insert(curation)
    }

    suspend fun getCurationById(id: Long): CurationEntity? {
        return curationDao.getCurationById(id)
    }

    suspend fun getAllCurations(): List<CurationEntity> {
        return curationDao.getAllCurations()
    }

    suspend fun deleteCuration(curation: CurationEntity) {
        curationDao.deleteCuration(curation)
    }

    suspend fun updateCuration(curation: CurationEntity) {
        curationDao.updateCuration(curation)
    }
}

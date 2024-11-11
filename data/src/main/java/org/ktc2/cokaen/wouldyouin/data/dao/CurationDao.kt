package org.ktc2.cokaen.wouldyouin.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity

@Dao
interface CurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(curation: CurationEntity)

    @Query("SELECT * FROM curation WHERE id = :id")
    suspend fun getCurationById(id: Long): CurationEntity?

    @Query("SELECT * FROM curation")
    suspend fun getAllCurations(): List<CurationEntity>

    @Delete
    suspend fun deleteCuration(curation: CurationEntity)

    @Update
    suspend fun updateCuration(curation: CurationEntity)
}

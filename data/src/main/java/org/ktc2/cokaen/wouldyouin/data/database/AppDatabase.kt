package org.ktc2.cokaen.wouldyouin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ktc2.cokaen.wouldyouin.data.converters.Converters
import org.ktc2.cokaen.wouldyouin.data.dao.CurationDao
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity

@Database(entities = [CurationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun curationDao(): CurationDao
}
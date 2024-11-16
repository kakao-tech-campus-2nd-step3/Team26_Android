package org.ktc2.cokaen.wouldyouin.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ktc2.cokaen.wouldyouin.data.dao.CurationDao
import org.ktc2.cokaen.wouldyouin.data.database.AppDatabase
import org.ktc2.cokaen.wouldyouin.data.repository.CurationLocalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    fun provideCurationDao(db: AppDatabase): CurationDao {
        return db.curationDao()
    }

    @Provides
    fun provideCurationLocalRepository(dao: CurationDao): CurationLocalRepository {
        return CurationLocalRepository(dao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            "wouldyouin_prefs", // 앱의 프리퍼런스 이름
            Context.MODE_PRIVATE
        )
    }
}

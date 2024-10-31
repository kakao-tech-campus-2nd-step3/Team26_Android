package org.ktc2.cokaen.wouldyouin.core_navigation

import NavigationHandler
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideNavigationUtil(
        @ApplicationContext context: Context
    ): NavigationUtil = NavigationHandler(context)
}
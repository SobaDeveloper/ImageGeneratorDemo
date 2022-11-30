package com.example.imagegeneratordemo.di

import android.content.Context
import androidx.room.Room
import com.example.imagegeneratordemo.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "ImageGenerator.db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePromptDao(appDatabase: AppDatabase) = appDatabase.promptDao()
}
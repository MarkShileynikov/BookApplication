package com.example.mybookapplication.data.di

import android.content.Context
import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun providePrefsDataSource(@ApplicationContext appContext: Context): PrefsDataSource {
        return PrefsDataSourceImpl(appContext)
    }
}
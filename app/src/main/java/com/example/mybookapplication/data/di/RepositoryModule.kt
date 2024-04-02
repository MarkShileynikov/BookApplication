package com.example.mybookapplication.data.di

import android.content.Context
import com.example.mybookapplication.data.api.AuthApiService
import com.example.mybookapplication.data.api.BookApiService
import com.example.mybookapplication.data.api.ReviewApiService
import com.example.mybookapplication.data.api.UpdateUserApiService
import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.BookRepositoryImpl
import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.data.repository.UpdateUserRepositoryImpl
import com.example.mybookapplication.data.repository.UserRepositoryImpl
import com.example.mybookapplication.domain.repository.BookRepository
import com.example.mybookapplication.domain.repository.ReviewRepository
import com.example.mybookapplication.domain.repository.SessionRepository
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ContextModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context) : Context{
        return context
    }

    @Provides
    fun provideUserRepository(prefsDataSource: PrefsDataSource) : UserRepository {
        return UserRepositoryImpl(prefsDataSource)
    }

    @Provides
    fun provideBookRepository(bookApiService: BookApiService) : BookRepository {
        return BookRepositoryImpl(bookApiService)
    }

    @Provides
    fun provideReviewRepository(context: Context, reviewApiService: ReviewApiService) : ReviewRepository {
        return ReviewRepositoryImpl(context, reviewApiService)
    }

    @Provides
    fun provideSessionRepository(context: Context, authApiService: AuthApiService, prefsDataSource: PrefsDataSource) : SessionRepository {
        return SessionRepositoryImpl(context, authApiService, prefsDataSource)
    }

    @Provides
    fun provideUpdateUserRepository(context: Context, updateUserApiService: UpdateUserApiService) : UpdateUserRepository {
        return UpdateUserRepositoryImpl(context, updateUserApiService)
    }
}



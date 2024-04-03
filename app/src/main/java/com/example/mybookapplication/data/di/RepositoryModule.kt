package com.example.mybookapplication.data.di

import android.content.Context
import com.example.mybookapplication.data.api.AuthApiService
import com.example.mybookapplication.data.api.BookApiService
import com.example.mybookapplication.data.api.ReviewApiService
import com.example.mybookapplication.data.api.UpdateUserApiService
import com.example.mybookapplication.data.prefs.PrefsDataSource
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
interface RepositoryModuleModule {

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository

    @Binds
    fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository

    @Binds
    fun bindUpdateUserRepository(updateUserRepositoryImpl: UpdateUserRepositoryImpl): UpdateUserRepository
}




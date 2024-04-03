package com.example.mybookapplication.data.di

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
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


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




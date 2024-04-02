package com.example.mybookapplication.data.di

import android.content.Context
import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.domain.repository.BookRepository
import com.example.mybookapplication.domain.repository.ReviewRepository
import com.example.mybookapplication.domain.repository.SessionRepository
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.repository.UserRepository
import com.example.mybookapplication.domain.usecase.FetchBooksByGenreUseCase
import com.example.mybookapplication.domain.usecase.FetchBooksByTitleOrAuthorUseCase
import com.example.mybookapplication.domain.usecase.FetchReviewsUseCase
import com.example.mybookapplication.domain.usecase.FetchSessionUseCase
import com.example.mybookapplication.domain.usecase.FetchUserProfileUseCase
import com.example.mybookapplication.domain.usecase.PostReviewUseCase
import com.example.mybookapplication.domain.usecase.SignInUseCase
import com.example.mybookapplication.domain.usecase.SignOutUseCase
import com.example.mybookapplication.domain.usecase.SignUpUseCase
import com.example.mybookapplication.domain.usecase.UpdateUsernameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideFetchBooksByGenreUseCase(repository: BookRepository): FetchBooksByGenreUseCase {
        return FetchBooksByGenreUseCase(repository)
    }

    @Provides
    fun provideFetchReviewsUseCase(repository: ReviewRepository): FetchReviewsUseCase {
        return FetchReviewsUseCase(repository)
    }

    @Provides
    fun provideFetchUserProfileUseCase(repository: UserRepository): FetchUserProfileUseCase {
        return FetchUserProfileUseCase(repository)
    }

    @Provides
    fun provideFetchBooksByTitleOrAuthorUseCase(repository: BookRepository): FetchBooksByTitleOrAuthorUseCase {
        return FetchBooksByTitleOrAuthorUseCase(repository)
    }

    @Provides
    fun providePostReviewUseCase(repository: ReviewRepository): PostReviewUseCase {
        return PostReviewUseCase(repository)
    }

    @Provides
    fun provideSignInUseCase(@ApplicationContext context: Context, repository: SessionRepository): SignInUseCase {
        return SignInUseCase(context, repository)
    }

    @Provides
    fun provideSignOutUseCase(repository: UserRepository): SignOutUseCase {
        return SignOutUseCase(repository)
    }

    @Provides
    fun provideSignUpUseCase(@ApplicationContext context: Context, repository: SessionRepository): SignUpUseCase {
        return SignUpUseCase(context, repository)
    }

    @Provides
    fun provideUpdateUsernameUseCase(prefs: PrefsDataSource, repository: UpdateUserRepository): UpdateUsernameUseCase {
        return UpdateUsernameUseCase(prefs, repository)
    }

    @Provides
    fun provideFetchSessionUseCase(repository: SessionRepository): FetchSessionUseCase {
        return FetchSessionUseCase(repository)
    }

}
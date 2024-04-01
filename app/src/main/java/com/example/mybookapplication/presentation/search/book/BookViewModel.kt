package com.example.mybookapplication.presentation.search.book

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.R
import com.example.mybookapplication.data.api.NetworkClient
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.data.repository.UserRepositoryImpl
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.usecase.FetchReviewsUseCase
import com.example.mybookapplication.domain.usecase.FetchUserProfileUseCase
import com.example.mybookapplication.domain.util.Event
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class BookViewModel(
    context : Application,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val fetchReviewsUseCase: FetchReviewsUseCase,
): AndroidViewModel(context) {
    val userProfileViewState = MutableStateFlow<Event<UserProfile>>(Event.Failure("No profile found"))
    val reviewViewState = MutableStateFlow<ViewState<List<Review>>>(ViewState.Loading)
    private var bookId: String = ""
    init {
        fetchUserProfile()
    }

    fun setBookId(bookId: String) {
        this.bookId = bookId
    }

    fun fetchReviews() {
        viewModelScope.launch {
            fetchReviewsUseCase(bookId)
                .onStart { reviewViewState.value = ViewState.Loading }
                .catch { reviewViewState.value = ViewState.Failure(it.message ?: "No reviews found") }
                .collect {reviews ->
                    reviewViewState.value = ViewState.Success(reviews)
                }
        }
    }

    fun pluralizeReaders(context: Context, count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> context.resources.getQuantityString(R.plurals.readers_singular, count, count)
            count % 10 in 2..4 && count % 100 !in 12..14 -> context.resources.getQuantityString(R.plurals.readers_plural, count, count)
            else -> context.resources.getQuantityString(R.plurals.readers_plural, count, count)
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            fetchUserProfileUseCase()
                .catch {
                    userProfileViewState.value = Event.Failure("No profile found")
                }
                .collect { userProfile ->
                    userProfileViewState.value = userProfile
                }
        }
    }

    companion object {
        val bookViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val prefsDataSource = PrefsDataSourceImpl(context)
                val reviewApiService = NetworkClient.provideReviewApiService()
                val userRepository = UserRepositoryImpl(prefsDataSource)
                val reviewRepository = ReviewRepositoryImpl(context, reviewApiService)
                val fetchUserProfileUseCase = FetchUserProfileUseCase(userRepository)
                val fetchReviewsUseCase = FetchReviewsUseCase(reviewRepository)
                return@initializer BookViewModel(
                    context, fetchUserProfileUseCase, fetchReviewsUseCase
                )
            }
        }
    }
}
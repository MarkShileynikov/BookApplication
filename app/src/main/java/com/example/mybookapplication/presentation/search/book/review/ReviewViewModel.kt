package com.example.mybookapplication.presentation.search.book.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.data.api.NetworkClient
import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.usecase.PostReviewUseCase
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReviewViewModel(context: Application, private val postReviewUseCase: PostReviewUseCase) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<ViewState<Review>>(ViewState.Loading)

    fun postReview(userId: String, username: String, bookId: String, estimation: Int, review: String?) {
        viewModelScope.launch {
            postReviewUseCase(PostReviewUseCase.Params(
                userId = userId,
                username = username,
                bookId = bookId,
                estimation = estimation,
                review = review
            )
            )
                .onStart { viewState.value = ViewState.Loading }
                .catch { viewState.value = ViewState.Failure(it.message ?: "Something went wrong")}
                .collect {review ->
                    viewState.value = ViewState.Success(review)
                }
        }
    }

    companion object {
        val reviewModelFactory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val reviewApiService = NetworkClient.provideReviewApiService()
                val reviewRepository = ReviewRepositoryImpl(context, reviewApiService)
                val postReviewUseCase = PostReviewUseCase(reviewRepository)
                return@initializer ReviewViewModel(
                    context, postReviewUseCase
                )
            }
        }
    }
}
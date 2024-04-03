package com.example.mybookapplication.presentation.search.book.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.usecase.PostReviewUseCase
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    context: Application,
    private val postReviewUseCase: PostReviewUseCase
) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<ViewState<Review>>(ViewState.Loading)

    fun postReview(userId: String, username: String, bookId: String, estimation: Int, review: String?) {
        val formattedReview = review?.trim()?.replace("\\s+".toRegex(), " ") ?: ""
        viewModelScope.launch {
            postReviewUseCase(PostReviewUseCase.Params(
                userId = userId,
                username = username,
                bookId = bookId,
                estimation = estimation,
                review = formattedReview
            )
            )
                .onStart { viewState.value = ViewState.Loading }
                .catch { viewState.value = ViewState.Failure(it.message ?: "Something went wrong")}
                .collect {review ->
                    viewState.value = ViewState.Success(review)
                }
        }
    }
}
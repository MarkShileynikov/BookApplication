package com.example.mybookapplication.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.usecase.FetchBooksByTitleOrAuthorUseCase
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    context: Application, private val fetchBooksByTitleOrAuthorUseCase: FetchBooksByTitleOrAuthorUseCase) : AndroidViewModel(context){
    val viewState = MutableStateFlow<ViewState<List<Book>>>(ViewState.Loading)

    fun fetchBooksByTitleOrAuthor(query : String) {
        val titleOrAuthor = query.trim().replace("\\s+".toRegex(), " ")
        viewModelScope.launch {
            fetchBooksByTitleOrAuthorUseCase(titleOrAuthor)
                .onStart { viewState.value = ViewState.Loading}
                .catch { viewState.value =
                    ViewState.Failure("Книги не найдены") }
                .collect { books ->
                    viewState.value = ViewState.Success(books)
                }
        }
    }
}
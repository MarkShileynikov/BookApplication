package com.example.mybookapplication.presentation.search.booklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.usecase.FetchBooksByGenreUseCase
import com.example.mybookapplication.presentation.util.ViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BookListViewModel.BookListViewModelFactory::class)
class BookListViewModel @AssistedInject constructor(
    context: Application,
    private val fetchBooksByGenreUseCase: FetchBooksByGenreUseCase,
    @Assisted private val genre : String
) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<ViewState<List<Book>>>(ViewState.Loading)
    init {
        fetchBooksByGenre()
    }
    private fun fetchBooksByGenre() {
        viewModelScope.launch {
            fetchBooksByGenreUseCase(genre)
                .onStart { viewState.value = ViewState.Loading }
                .catch {
                    viewState.value =
                        ViewState.Failure("No books found")
                } 
                .collect { books ->
                    viewState.value = ViewState.Success(books)
                }
        }
    }

    @AssistedFactory
    interface BookListViewModelFactory {
        fun create(genre: String): BookListViewModel
    }
}
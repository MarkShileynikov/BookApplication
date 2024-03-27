package com.example.mybookapplication.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.data.api.NetworkClient
import com.example.mybookapplication.data.repository.BookRepositoryImpl
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.usecase.FetchBooksByTitleOrAuthorUseCase
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    context: Application, private val fetchBooksByTitleOrAuthorUseCase: FetchBooksByTitleOrAuthorUseCase) : AndroidViewModel(context){
    val viewState = MutableStateFlow<ViewState<List<Book>>>(ViewState.Loading)

    fun fetchBooksByTitleOrAuthor(titleOrAuthor : String) {
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

    companion object {
        val searchViewModelFactory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bookApiService = NetworkClient.provideBookApiService()
                val bookRepository = BookRepositoryImpl(bookApiService)
                val fetchBooksByTitleOrAuthorUseCase = FetchBooksByTitleOrAuthorUseCase(bookRepository)
                return@initializer SearchViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App,
                    fetchBooksByTitleOrAuthorUseCase
                )
            }
        }
    }
}
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
import com.example.mybookapplication.domain.usecase.FetchBooksByGenreUseCase
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class BookListViewModel(context: Application, private val fetchBooksByGenreUseCase: FetchBooksByGenreUseCase, private val genre : String) : AndroidViewModel(context) {
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
                        ViewState.Failure("Книги не найдены")
                } 
                .collect { books ->
                    viewState.value = ViewState.Success(books)
                }
        }
    }
    fun getGenre() : String = this.genre
    companion object {
        fun bookListModelFactory(genre: String) : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bookApiService = NetworkClient.provideBookApiService()
                val bookRepository = BookRepositoryImpl(bookApiService)
                val fetchBooksByGenreUseCase = FetchBooksByGenreUseCase(bookRepository)
                return@initializer BookListViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App,
                    fetchBooksByGenreUseCase, genre
                )
            }
        }
    }
}
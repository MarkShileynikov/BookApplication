package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.BookRepositoryImpl
import com.example.mybookapplication.domain.entity.Book
import kotlinx.coroutines.flow.Flow

class FetchBooksByTitleOrAuthorUseCase(private val bookRepository: BookRepositoryImpl) {
    suspend operator fun invoke(titleOrAuthor : String) : Flow<List<Book>> = bookRepository.fetchBooksByGenreOrAuthor(titleOrAuthor)
}
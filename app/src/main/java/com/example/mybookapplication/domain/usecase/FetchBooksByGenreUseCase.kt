package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.BookRepositoryImpl
import com.example.mybookapplication.domain.entity.Book
import kotlinx.coroutines.flow.Flow

class FetchBooksByGenreUseCase(private val bookRepository: BookRepositoryImpl) {
    suspend operator fun invoke(genre : String): Flow<List<Book>> = bookRepository.fetchBooksByGenre(genre)
}
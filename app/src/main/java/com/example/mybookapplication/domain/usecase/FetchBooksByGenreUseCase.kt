package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchBooksByGenreUseCase @Inject constructor(private val bookRepository: BookRepository) {

    suspend operator fun invoke(genre : String): Flow<List<Book>> = bookRepository.fetchBooksByGenre(genre)
}
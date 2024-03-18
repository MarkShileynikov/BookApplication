package com.example.mybookapplication.data.repository

import com.example.mybookapplication.data.api.BookApiService
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(private val bookApiService : BookApiService) : BookRepository {
    override suspend fun fetchBooks(): Flow<List<Book>> = flow {
        val response = bookApiService.fetchBooks()
        emit(response)
    }.map {responses ->
        responses.map {response ->
            Book(
                id = response.id,
                title = response.title,
                author = response.author,
                genre = response.genre,
                description = response.description,
                releaseYear = response.releaseYear,
                ageLimit = response.ageLimit,
                cover = response.cover,
                pages = response.pages
            )
        }
    }

    override suspend fun fetchBooksByGenre(genre: String): Flow<List<Book>> = flow {
        val response = bookApiService.fetchBooksByGenre("genre = '$genre'")
        emit(response)
    }.map { responses ->
        responses.map { response ->
            Book(
                id = response.id,
                title = response.title,
                author = response.author,
                genre = response.genre,
                description = response.description,
                releaseYear = response.releaseYear,
                ageLimit = response.ageLimit,
                cover = response.cover,
                pages = response.pages
            )
        }
    }


}
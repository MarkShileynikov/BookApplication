package com.example.mybookapplication.domain.repository

import com.example.mybookapplication.domain.entity.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    
    suspend fun fetchBooks() : Flow<List<Book>>

    suspend fun fetchBooksByGenre(genre : String) : Flow<List<Book>>

    suspend fun fetchBooksByGenreOrAuthor(titleOrAuthor : String) : Flow<List<Book>>
}
package com.example.mybookapplication.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mybookapplication.domain.usecase.FetchBooksByGenreUseCase

class SearchViewModel(context: Application, private val fetchBooksByGenreUseCase: FetchBooksByGenreUseCase) : AndroidViewModel(context){
}
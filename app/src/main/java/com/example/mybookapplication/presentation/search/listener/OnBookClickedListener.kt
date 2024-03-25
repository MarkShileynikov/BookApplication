package com.example.mybookapplication.presentation.search.listener

import com.example.mybookapplication.domain.entity.Book

interface OnBookClickedListener {
    fun bookClicked(book : Book)
}
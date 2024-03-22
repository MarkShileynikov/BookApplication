package com.example.mybookapplication.domain.util

import kotlinx.coroutines.flow.Flow

interface UseCase<in T, out R> {
    suspend operator fun invoke(params: T) : Flow<R>
    class None
}
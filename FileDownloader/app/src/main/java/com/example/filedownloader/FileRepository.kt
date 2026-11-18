package com.example.filedownloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FileRepository {

    fun downloadFile(): Flow<Int> = flow {
        for (progress in 0..100 step 5) {
            emit(progress)
            if (progress < 100) {
                delay(150L)
            }
        }
    }.flowOn(Dispatchers.IO)
}
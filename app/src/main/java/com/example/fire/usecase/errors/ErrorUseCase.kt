package com.example.fire.usecase.errors

import com.example.fire.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}

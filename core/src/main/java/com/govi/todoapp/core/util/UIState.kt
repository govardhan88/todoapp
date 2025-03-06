package com.govi.todoapp.core.util

/**
 * Created by Govi on 27,February,2025
 */


sealed class UIState<T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error<T>(val message: T) : UIState<T>()
}
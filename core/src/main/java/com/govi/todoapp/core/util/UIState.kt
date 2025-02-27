package com.govi.todoapp.core.util

/**
 * Created by Govi on 27,February,2025
 */


sealed class UIState {
    object Loading : UIState()
    data class Success<T>(val data: T) : UIState()
    data class Error<T>(val message: T) : UIState()
}
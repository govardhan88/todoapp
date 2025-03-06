package com.govi.todoapp.add_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.govi.todoapp.core.coroutines.DispatcherProvider
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.AddTodoUseCase
import com.govi.todoapp.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Govi on 26,February,2025
 */
@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val addTodoUseCase: AddTodoUseCase,
    private val navHostController: NavHostController,
    val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<*>>(UIState.Idle)
    val uiState = _uiState


    fun addTodo(eventTitle: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                _uiState.value = UIState.Loading
                if (eventTitle.isBlank() || eventTitle.equals("error", ignoreCase = true)) {
                    _uiState.value = UIState.Error("Failed to add TODO")
                } else {
                    addTodoUseCase(Todo(eventTitle = eventTitle, id = null))
                    delay(3000L)
                    _uiState.value = UIState.Success(true)
                }
            } catch (ex: Exception) {
                _uiState.value = UIState.Error("Failed to add TODO")
            }
        }
    }

    fun navigateBack() {
        navHostController.popBackStack()
    }

    fun resetUiState() {
        _uiState.value = UIState.Idle
    }
}
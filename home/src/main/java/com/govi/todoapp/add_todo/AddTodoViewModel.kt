package com.govi.todoapp.add_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.govi.todoapp.core.navigation.Routes
import com.govi.todoapp.domain.AddTodoUseCase
import com.govi.todoapp.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Govi on 27,February,2025
 */
@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val addTodoUseCase: AddTodoUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val navHostController: NavHostController,
) : ViewModel() {

    private val _todoText = MutableStateFlow("")
    val todoText: StateFlow<String> = _todoText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isTodoAdded = MutableStateFlow(false)
    val isTodoAdded: StateFlow<Boolean> = _isTodoAdded.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onTodoTextChange(newText: String) {
        _todoText.value = newText
    }

    fun addTodo() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.value = true
            try {
                if (_todoText.value.isBlank()) {
                    throw Exception("Todo text cannot be empty")
                }
                addTodoUseCase(Todo(eventTitle = _todoText.value, id = null))
                delay(3000)
                _isTodoAdded.value = true
            } catch (e: Exception) {
                _error.value = "Failed to add TODO"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun navigateToHome() {
        navHostController.navigate(Routes.Home.route){
            popUpTo(Routes.Home.route){inclusive = true}
        }
    }

    fun resetTodoAdded() {
        _isTodoAdded.value = false
    }

}
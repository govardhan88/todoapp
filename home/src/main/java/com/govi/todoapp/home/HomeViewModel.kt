package com.govi.todoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.govi.todoapp.core.navigation.Routes.AddTodo
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.GetTodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Govi on 26,February,2025
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val navHostController: NavHostController
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UIState.Loading
            try {
                getTodosUseCase().collect { todos ->
                    withContext(Dispatchers.Main) {
                        _uiState.value = UIState.Success(todos)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = UIState.Error(e.message ?: "An unknown error occurred")
                }
            }
        }
    }

    fun navigateToAddTodo() {
        navHostController.navigate(AddTodo.route)
    }
}

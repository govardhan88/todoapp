package com.govi.todoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.govi.todoapp.domain.GetTodosUseCase
import com.govi.todoapp.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Govi on 26,February,2025
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
): ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            getTodosUseCase().collect { todos ->
                withContext(Dispatchers.Main) {
                    _todos.value = todos
                }
            }
        }
    }

}

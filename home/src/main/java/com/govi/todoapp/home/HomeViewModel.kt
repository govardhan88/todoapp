package com.govi.todoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.govi.todoapp.core.coroutines.DispatcherProvider
import com.govi.todoapp.core.navigation.Routes
import com.govi.todoapp.core.navigation.Routes.AddTodo
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.GetTodosUseCase
import com.govi.todoapp.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * Created by Govi on 26,February,2025
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val navHostController: NavHostController,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos.asStateFlow()


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTodos: StateFlow<List<Todo>> = _searchQuery
        .debounce(2000.milliseconds) // Reduced debounce
        .distinctUntilChanged()
        .flatMapLatest { query ->
            getTodosUseCase(query).flowOn(dispatcherProvider.io)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun loadTodos() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = UIState.Loading
            try {
                getTodosUseCase().collect { todos ->
                    _allTodos.value = todos
                    _uiState.value = UIState.Success(todos)
                }
            } catch (e: Exception) {
                withContext(dispatcherProvider.main) {
                    _uiState.value = UIState.Error(e.message ?: "An unknown error occurred")
                }
            }
        }
    }

    fun navigateToAddTodo() {
        navHostController.navigate(AddTodo.route) {
            launchSingleTop = true
            popUpTo(Routes.Home.route) {
                inclusive = true
            }

        }
    }


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

}
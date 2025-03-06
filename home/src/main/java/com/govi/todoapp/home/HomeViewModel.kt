package com.govi.todoapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
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
import kotlinx.coroutines.flow.onEach
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

    private val _uiState = MutableStateFlow<UIState<*>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos.asStateFlow()

    private val _isDebouncing = MutableStateFlow(false)
    val isDebouncing: StateFlow<Boolean> = _isDebouncing.asStateFlow()

    init {
        loadTodos()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTodos: StateFlow<List<Todo>> = _searchQuery
        .debounce(2000.milliseconds)
        .onEach {
            _isDebouncing.value = true
        }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            getTodosUseCase(query).flowOn(dispatcherProvider.io)
        }
        .onEach {
            _isDebouncing.value = false
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
        navHostController.navigate(AddTodo.route, navOptions = getNavOptionsForAddTodo())
    }


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getNavOptionsForAddTodo(): NavOptions {
        return NavOptions.Builder().apply {
            setLaunchSingleTop(true)
            setPopUpTo(Routes.Home.route, inclusive = false)
        }.build()
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun setErrorState(errorMsg: String) {
        _uiState.value = UIState.Error(message = errorMsg)
    }
}
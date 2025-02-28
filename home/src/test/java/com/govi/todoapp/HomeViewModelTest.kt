package com.govi.todoapp

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import app.cash.turbine.test
import com.govi.todoapp.core.navigation.Routes
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.GetTodosUseCase
import com.govi.todoapp.domain.model.Todo
import com.govi.todoapp.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getTodosUseCase: GetTodosUseCase

    @Mock
    private lateinit var navHostController: NavHostController

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel =
            HomeViewModel(getTodosUseCase, navHostController, mainCoroutineRule.dispatcherProvider)
    }

    @Test
    fun `loadTodos success`() = runTest {
        val todos =
            listOf(Todo(eventTitle = "Todo 1", id = "1"), Todo(eventTitle = "Todo 2", id = "2"))
        `when`(getTodosUseCase()).thenReturn(flowOf(todos))

        viewModel.loadTodos()
        mainCoroutineRule.testDispatcher.scheduler.runCurrent()

        assertEquals(UIState.Success(todos), viewModel.uiState.first())
        assertEquals(todos, viewModel.allTodos.first())
    }

    @Test
    fun `loadTodos error`() = runTest {
        val errorMessage = "Test Error"
        `when`(getTodosUseCase()).thenThrow(RuntimeException(errorMessage))

        viewModel.loadTodos()
        mainCoroutineRule.testDispatcher.scheduler.runCurrent()

        viewModel.uiState.test {
            val uiState = awaitItem()
            assertEquals(true, uiState is UIState.Error<*>)
            assertEquals(errorMessage, (uiState as UIState.Error<*>).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigateToAddTodo navigates to AddTodo route`() = runTest {
        viewModel.navigateToAddTodo()
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(Routes.Home.route, true)
            .build()
        verify(navHostController).navigate(eq(Routes.AddTodo.route), eq(navOptions), eq(null))

    }

    @Test
    fun `onSearchQueryChange updates searchQuery`() = runTest {
        val query = "Test Query"
        viewModel.onSearchQueryChange(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `filteredTodos emits correct values`() = runTest {
        val query = "Test"
        val todos =
            listOf(Todo(eventTitle = "Todo 1", id = "1"), Todo(eventTitle = "Todo 2", id = "2"))
        `when`(getTodosUseCase(query)).thenReturn(flowOf(todos))

        val collectJob = launch { viewModel.filteredTodos.collect {} }
        viewModel.onSearchQueryChange(query)
        advanceTimeBy(2000)
        advanceUntilIdle()
        assertEquals(todos, viewModel.filteredTodos.first())
        collectJob.cancel()
    }

    @Test
    fun `filteredTodos emits empty list when no query`() = runTest {
        `when`(getTodosUseCase(any())).thenReturn(flowOf(emptyList()))
        viewModel.onSearchQueryChange("")
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(2000)
        mainCoroutineRule.testDispatcher.scheduler.runCurrent()
        assertEquals(emptyList<Todo>(), viewModel.filteredTodos.first())
    }

    @Test
    fun `loadTodos sets uiState to loading initially`() = runTest {
        `when`(getTodosUseCase()).thenReturn(flowOf(emptyList()))
        viewModel.loadTodos()
        assertEquals(UIState.Loading, viewModel.uiState.first())
    }
}
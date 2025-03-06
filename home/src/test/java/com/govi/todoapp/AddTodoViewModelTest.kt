package com.govi.todoapp

import androidx.navigation.NavHostController
import app.cash.turbine.test
import com.govi.todoapp.add_todo.AddTodoViewModel
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.AddTodoUseCase
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AddTodoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var addTodoUseCase: AddTodoUseCase

    @Mock
    private lateinit var navHostController: NavHostController

    private lateinit var viewModel: AddTodoViewModel

    private val todo = Todo(eventTitle = "Test Todo", id = null)


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = AddTodoViewModel(
            addTodoUseCase = addTodoUseCase,
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            navHostController = navHostController
        )
    }

    @Test
    fun addTodo_emptyEventTitle_setsErrorState() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.addTodo("")
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Error("Failed to add TODO"), awaitItem())
        }
    }

    @Test
    fun addTodo_errorEventTitle_setsErrorState() = runTest {
        viewModel.uiState.test {
            skipItems(1)
            viewModel.addTodo("error")
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Error("Failed to add TODO"), awaitItem())
        }
    }

    @Test
    fun addTodo_validEventTitle_setsSuccessState() = runTest {
        whenever(addTodoUseCase.invoke(todo = todo)).then { 1L }
        viewModel.uiState.test {
            skipItems(1)
            viewModel.addTodo("Test Todo")
            assertEquals(UIState.Loading, awaitItem())
            mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(3000L)
            assertEquals(UIState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun addTodo_validEventTitle_callsAddTodoUseCase() = runTest {
        whenever(addTodoUseCase.invoke(any())).doAnswer { 1L }
        viewModel.addTodo("Test Todo")
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(3000L)
        verify(addTodoUseCase, times(1)).invoke(todo)
    }

    @Test
    fun addTodo_exceptionThrown_setsErrorState() = runTest {
        `when`(
            addTodoUseCase.invoke(todo)
        ).then { throw Exception("Test Exception") }
        viewModel.uiState.test {
            skipItems(1)
            viewModel.addTodo("Test Todo")
            assertEquals(UIState.Loading, awaitItem())
            assertEquals(UIState.Error("Failed to add TODO"), awaitItem())
        }
    }

    @Test
    fun navigateBack_popsBackStack() = runTest {
        viewModel.navigateBack()
        verify(navHostController).popBackStack()
    }

    @Test
    fun resetUiState_setsIdleState() = runTest {
        viewModel.resetUiState()
        assertEquals(UIState.Idle, viewModel.uiState.first())
    }

    @Test
    fun addTodo_validEventTitle_setsLoadingState() = runTest {
        whenever(addTodoUseCase.invoke(any())).doAnswer { 1L }
        viewModel.uiState.test {
            skipItems(1)
            viewModel.addTodo("Test Todo")
            assertEquals(UIState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
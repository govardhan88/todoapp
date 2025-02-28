package com.govi.todoapp

import androidx.navigation.NavHostController
import app.cash.turbine.test
import com.govi.todoapp.add_todo.AddTodoViewModel
import com.govi.todoapp.core.navigation.Routes
import com.govi.todoapp.domain.AddTodoUseCase
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AddTodoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var addTodoUseCase: AddTodoUseCase

    @Mock
    private lateinit var navHostController: NavHostController

    private lateinit var viewModel: AddTodoViewModel


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = AddTodoViewModel(addTodoUseCase, mainCoroutineRule.dispatcherProvider, navHostController)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTodoTextChange updates todoText`() = runTest {
        val newText = "Test Todo"
        viewModel.onTodoTextChange(newText)
        viewModel.todoText.test {
            assertEquals(newText, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTodo succeeds and sets isTodoAdded to true`() = runTest {
        val todoText = "Test Todo"
        viewModel.onTodoTextChange(todoText)
        `when`(addTodoUseCase(Todo(eventTitle = todoText, id = null))).then {}

        viewModel.addTodo()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.isTodoAdded.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify(addTodoUseCase).invoke(Todo(eventTitle = todoText, id = null))
    }

    @Test
    fun `addTodo fails with blank todoText and sets error`() = runTest {
        viewModel.onTodoTextChange("")
        viewModel.addTodo()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.error.test {
            assertEquals("Failed to add TODO", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTodo fails with exception and sets error`() = runTest {
        val todoText = "Test Todo"
        viewModel.onTodoTextChange(todoText)
        `when`(addTodoUseCase(Todo(eventTitle = todoText, id = null))).thenThrow(RuntimeException("Test Exception"))

        viewModel.addTodo()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.error.test {
            assertEquals("Failed to add TODO", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigateToHome navigates to Home route`() = runTest {
        viewModel.navigateToHome()
        verify(navHostController).navigate(Routes.Home.route)
    }

    @Test
    fun `resetTodoAdded sets isTodoAdded to false`() = runTest {
        viewModel.resetTodoAdded()
        viewModel.isTodoAdded.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
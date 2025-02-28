package com.govi.todoapp

/**
 * Created by Govi on 28,February,2025
 */
import app.cash.turbine.test
import com.govi.todoapp.domain.GetTodosUseCase
import com.govi.todoapp.domain.TodoRepository
import com.govi.todoapp.domain.model.Todo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by Govi on 28,February,2025
 */
class GetTodosUseCaseTest {

    private lateinit var todoRepository: TodoRepository
    private lateinit var getTodosUseCase: GetTodosUseCase

    @Before
    fun setUp() {
        todoRepository = mock()
        getTodosUseCase = GetTodosUseCase(todoRepository)
    }

    @Test
    fun `invoke should return a flow of list of todos`() = runTest {
        val expectedTodos = listOf(
            Todo(id = "1", eventTitle = "Todo 1"),
            Todo(id = "2", eventTitle = "Todo 2")
        )
        whenever(todoRepository.getAllTodos()).thenReturn(flowOf(expectedTodos))

        val result = getTodosUseCase()

        result.test {
            val actualTodos = awaitItem()
            assertEquals(expectedTodos, actualTodos)
            awaitComplete()
        }
        verify(todoRepository).getAllTodos()
    }
}
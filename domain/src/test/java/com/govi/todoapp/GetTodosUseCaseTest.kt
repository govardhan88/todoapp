package com.govi.todoapp

import com.govi.todoapp.domain.GetTodosUseCase
import com.govi.todoapp.domain.TodoRepository
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.flow.toList

class GetTodosUseCaseTest {

    @Mock
    private lateinit var todoRepository: TodoRepository

    private lateinit var getTodosUseCase: GetTodosUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getTodosUseCase = GetTodosUseCase(todoRepository)
    }

    @Test
    fun `invoke with empty query returns all todos`() = runTest {
        val todos =
            listOf(Todo(id = "1", eventTitle = "Todo 1"), Todo(id = "2", eventTitle = "Todo 2"))
        `when`(todoRepository.getAllTodos()).thenReturn(flowOf(todos))

        val result = getTodosUseCase("").toList()
        assertEquals(listOf(todos), result)
    }

    @Test
    fun `invoke with query returns filtered todos`() = runTest {
        val query = "Test"
        val filteredTodos = listOf(Todo(id = "1", eventTitle = "Test Todo 1"))
        `when`(todoRepository.searchTodos(query)).thenReturn(flowOf(filteredTodos))

        val result = getTodosUseCase(query).toList()
        assertEquals(listOf(filteredTodos), result)
    }
}
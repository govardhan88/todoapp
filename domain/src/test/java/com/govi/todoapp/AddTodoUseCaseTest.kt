package com.govi.todoapp

import com.govi.todoapp.domain.AddTodoUseCase
import com.govi.todoapp.domain.TodoRepository
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Created by Govi on 28,February,2025
 */
class AddTodoUseCaseTest {

    private lateinit var todoRepository: TodoRepository
    private lateinit var addTodoUseCase: AddTodoUseCase

    @Before
    fun setUp() {
        todoRepository = mock()
        addTodoUseCase = AddTodoUseCase(todoRepository)
    }

    @Test
    fun `invoke should call addTodo on repository`() = runTest {
        val todo = Todo(id = null, eventTitle = "Test Todo")

        addTodoUseCase(todo)

        verify(todoRepository).addTodo(todo)
    }
}
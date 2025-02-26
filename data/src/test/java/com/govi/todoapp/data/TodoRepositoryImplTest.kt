package com.govi.todoapp.data

import app.cash.turbine.test
import com.govi.todoapp.data.model.TodoEntity
import com.govi.todoapp.domain.model.Todo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Created by Govi on 26,February,2025
 */
class TodoRepositoryImplTest {

    private val dao = mock(TodoDao::class.java)
    private val repository = TodoRepositoryImpl(dao)

    @Test
    fun `getAllTodos should return todos mapped to domain model`() = runTest {
        val todoEntities = listOf(
            TodoEntity(id = 1, eventTitle = "Task 1"),
            TodoEntity(id = 2, eventTitle = "Task 2")
        )
        val expectedTodos = listOf(
            Todo(id = "1", eventTitle = "Task 1"),
            Todo(id = "2", eventTitle = "Task 2")
        )
        `when`(dao.getAll()).thenReturn(flowOf(todoEntities))

        repository.getAllTodos().test {
            assertEquals(expectedTodos, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTodo should call dao insert with correct entity`() = runTest {
        val todo = Todo(eventTitle = "New Task", id = null)
        repository.addTodo(todo)

        verify(dao).insert(TodoEntity(eventTitle = "New Task"))
    }

    @Test
    fun `mapToDomain should correctly map TodoEntity to Todo`() {
        val todoEntity = TodoEntity(id = 1, eventTitle = "Test Task")
        val expectedTodo = Todo(id = "1", eventTitle = "Test Task")

        assertEquals(expectedTodo, todoEntity.mapToDomain())
    }
}
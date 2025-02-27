package com.govi.todoapp.data

import app.cash.turbine.test
import com.govi.todoapp.data.model.TodoEntity
import com.govi.todoapp.domain.model.Todo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

/**
 * Created by Govi on 26,February,2025
 */


/**
 * Created by Govi on 27,February,2025
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TodoRepositoryImplTest {

    private lateinit var todoDao: TodoDao
    private lateinit var todoRepository: TodoRepositoryImpl

    @Before
    fun setUp() {
        todoDao = mock()
        todoRepository = TodoRepositoryImpl(todoDao)
    }

    @Test
    fun `getAllTodos should return a flow of list of todos`() = runTest {
        // Arrange
        val todoEntities = listOf(
            TodoEntity(id = 1, eventTitle = "Todo 1"),
            TodoEntity(id = 2, eventTitle = "Todo 2")
        )
        val expectedTodos = listOf(
            Todo(id = "1", eventTitle = "Todo 1"),
            Todo(id = "2", eventTitle = "Todo 2")
        )
        whenever(todoDao.getAll()).thenReturn(flowOf(todoEntities))

        // Act
        val result = todoRepository.getAllTodos()

        // Assert
        result.test {
            val actualTodos = awaitItem()
            assertEquals(expectedTodos, actualTodos)
            awaitComplete()
        }
        verify(todoDao).getAll()
    }

    @Test
    fun `addTodo should insert a todo and return the row id`() = runTest {
        // Arrange
        val todo = Todo(eventTitle = "New Todo", id = "1")
        val todoEntity = TodoEntity(eventTitle = "New Todo")
        val expectedRowId = 1L
        whenever(todoDao.insert(todoEntity)).thenReturn(expectedRowId)

        // Act
        val result = todoRepository.addTodo(todo)

        // Assert
        assertEquals(expectedRowId, result)
        verify(todoDao).insert(todoEntity)
    }

    @Test
    fun `mapToDomain should correctly map TodoEntity to Todo`() {
        val todoEntity = TodoEntity(id = 1, eventTitle = "Test Task")
        val expectedTodo = Todo(id = "1", eventTitle = "Test Task")

        assertEquals(expectedTodo, todoEntity.mapToDomain())
    }
}
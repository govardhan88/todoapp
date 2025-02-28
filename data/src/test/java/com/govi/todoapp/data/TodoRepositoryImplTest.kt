package com.govi.todoapp.data

import com.govi.todoapp.data.model.TodoEntity
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times

class TodoRepositoryTest {

    @Mock
    private lateinit var todoDao: TodoDao

    private lateinit var todoRepository: TodoRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        todoRepository = TodoRepositoryImpl(todoDao)
    }

    @Test
    fun `getAllTodos returns mapped todos`() = runTest {
        val todoEntities = listOf(
            TodoEntity(id = 1, eventTitle = "Todo 1"),
            TodoEntity(id = 2, eventTitle = "Todo 2")
        )
        val todos =
            listOf(Todo(id = "1", eventTitle = "Todo 1"), Todo(id = "2", eventTitle = "Todo 2"))
        `when`(todoDao.getAll()).thenReturn(flowOf(todoEntities))

        val result = todoRepository.getAllTodos().toList()
        assertEquals(listOf(todos), result)
    }

    @Test
    fun `addTodo inserts todo entity`() = runTest {
        val todo = Todo(id = null, eventTitle = "Todo 1")
        val todoEntity = TodoEntity(eventTitle = "Todo 1")
        `when`(todoDao.insert(todoEntity)).thenReturn(1L)

        val result = todoRepository.addTodo(todo)
        assertEquals(1L, result)
        verify(todoDao, times(1)).insert(todoEntity)
    }

    @Test
    fun `searchTodos returns mapped todos`() = runTest {
        val query = "Test"
        val todoEntities = listOf(TodoEntity(id = 1, eventTitle = "Test Todo 1"))
        val todos = listOf(Todo(id = "1", eventTitle = "Test Todo 1"))
        `when`(todoDao.searchTodos(query)).thenReturn(flowOf(todoEntities))

        val result = todoRepository.searchTodos(query).toList()
        assertEquals(listOf(todos), result)
    }

    @Test
    fun `mapToDomain maps TodoEntity to Todo`() = runTest {
        val todoEntities = listOf(
            TodoEntity(id = 1, eventTitle = "Todo 1"),
            TodoEntity(id = 2, eventTitle = "Todo 2")
        )
        val expectedTodos = listOf(
            Todo(id = "1", eventTitle = "Todo 1"),
            Todo(id = "2", eventTitle = "Todo 2")
        )

        `when`(todoDao.getAll()).thenReturn(flowOf(todoEntities))
        val result = todoRepository.getAllTodos().toList()
        assertEquals(listOf(expectedTodos), result)
    }
}
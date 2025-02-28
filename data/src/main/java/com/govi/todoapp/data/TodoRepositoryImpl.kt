package com.govi.todoapp.data

import com.govi.todoapp.data.model.TodoEntity
import com.govi.todoapp.domain.TodoRepository
import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Govi on 25,February,2025
 */

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {
    override suspend fun getAllTodos(): Flow<List<Todo>> = dao.getAll().mapToDomain()

    override suspend fun addTodo(todo: Todo): Long {
        return dao.insert(TodoEntity(eventTitle = todo.eventTitle))
    }

    override suspend fun searchTodos(query: String): Flow<List<Todo>> =
        dao.searchTodos(query).mapToDomain()

    private fun Flow<List<TodoEntity>>.mapToDomain() = this.map { todoEntities ->
        todoEntities.map { it.mapToDomain() }
    }

}
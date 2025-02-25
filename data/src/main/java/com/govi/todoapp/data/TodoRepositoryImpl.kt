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
    override suspend fun getAllTodos(): Flow<List<Todo>> = dao.getAll().map { todoEntities ->
        todoEntities.map { it.mapToDomain() }
    }

    override suspend fun addTodo(todo: Todo) {
        dao.insert(TodoEntity(eventTitle = todo.eventTitle))
    }
}
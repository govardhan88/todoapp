package com.govi.todoapp.domain

import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Govi on 25,February,2025
 */
interface TodoRepository {
    suspend fun getAllTodos(): Flow<List<Todo>>
    suspend fun addTodo(todo: Todo)
}
package com.govi.todoapp.domain

import com.govi.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Govi on 26,February,2025
 */
class GetTodosUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(query: String = ""): Flow<List<Todo>> {
        return if (query.isBlank()) {
            repository.getAllTodos()
        } else {
            repository.searchTodos(query)
        }
    }
}
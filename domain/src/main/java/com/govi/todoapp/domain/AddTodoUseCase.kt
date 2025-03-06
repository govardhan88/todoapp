package com.govi.todoapp.domain

import com.govi.todoapp.domain.model.Todo
import javax.inject.Inject

/**
 * Created by Govi on 27,February,2025
 */
class AddTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo): Long = repository.addTodo(todo)
}
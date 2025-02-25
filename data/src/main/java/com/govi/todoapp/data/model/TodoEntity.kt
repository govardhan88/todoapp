package com.govi.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.govi.todoapp.domain.model.Todo

/**
 * Created by Govi on 25,February,2025
 */

@Entity(tableName = "Event")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventTitle: String
) {
    fun mapToDomain(): Todo = Todo(id = this.id.toString(), eventTitle = this.eventTitle)
}
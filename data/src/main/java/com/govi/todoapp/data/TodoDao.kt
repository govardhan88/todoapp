package com.govi.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.govi.todoapp.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Govi on 25,February,2025
 */

@Dao
interface TodoDao {
    @Query("SELECT * FROM Event")
    fun getAll(): Flow<List<TodoEntity>>

    @Insert
    suspend fun insert(todoEntity: TodoEntity)
}
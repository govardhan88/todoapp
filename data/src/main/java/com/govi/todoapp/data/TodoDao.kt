package com.govi.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoEntity: TodoEntity):Long

    @Query("SELECT * FROM Event WHERE eventTitle LIKE '%' || :query || '%'")
    fun searchTodos(query: String): Flow<List<TodoEntity>>
}
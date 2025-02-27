package com.govi.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.govi.todoapp.data.model.TodoEntity

/**
 * Created by Govi on 25,February,2025
 */

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
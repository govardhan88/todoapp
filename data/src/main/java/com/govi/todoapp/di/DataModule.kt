package com.govi.todoapp.di

import com.govi.todoapp.data.TodoDao
import com.govi.todoapp.data.TodoDatabase
import com.govi.todoapp.data.TodoRepositoryImpl
import com.govi.todoapp.domain.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Govi on 25,February,2025
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(dao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(dao)
    }

}
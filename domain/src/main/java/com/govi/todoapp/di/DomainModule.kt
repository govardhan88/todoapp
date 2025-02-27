package com.govi.todoapp.di

import com.govi.todoapp.domain.GetTodosUseCase
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
object DomainModule {

    @Provides
    @Singleton
    fun provideGetTodosUseCase(repository: TodoRepository): GetTodosUseCase =
        GetTodosUseCase(repository)

}
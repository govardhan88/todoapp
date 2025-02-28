package com.govi.todoapp.di

import com.govi.todoapp.core.coroutines.DefaultDispatcherProvider
import com.govi.todoapp.core.coroutines.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Govi on 28,February,2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutinesModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        defaultDispatcherProvider: DefaultDispatcherProvider
    ): DispatcherProvider
}
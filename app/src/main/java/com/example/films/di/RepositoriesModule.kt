package com.example.films.di

import com.example.films.data.films.FilmsRepository
import com.example.films.data.films.FilmsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    @Singleton
    fun bindsFilmsRepository(repositoryImpl: FilmsRepositoryImpl): FilmsRepository
}
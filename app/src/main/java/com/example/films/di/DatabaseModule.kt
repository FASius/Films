package com.example.films.di

import android.content.Context
import androidx.room.Room
import com.example.films.data.films.db.favirites.FavoritesDao
import com.example.films.data.films.db.favirites.entities.converters.Converters
import com.example.films.data.films.db.filmscache.FilmsCacheDao
import com.example.films.data.room.AppDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDb(typeConverter: Converters, @ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "films.db")
            .addTypeConverter(typeConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(db: AppDatabase): FavoritesDao {
        return db.favoritesDao()
    }

    @Provides
    @Singleton
    fun provideFilmsCacheDao(db: AppDatabase): FilmsCacheDao {
        return db.filmsCacheDao()
    }

    @Provides
    @Singleton
    fun provideTypeConverter(gson: Gson): Converters {
        return Converters(gson)
    }

}
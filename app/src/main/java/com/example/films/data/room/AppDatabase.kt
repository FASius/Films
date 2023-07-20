package com.example.films.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.films.data.films.db.favirites.FavoritesDao
import com.example.films.data.films.db.favirites.entities.FavoriteFilmsDbEntity
import com.example.films.data.films.db.favirites.entities.converters.Converters
import com.example.films.data.films.db.filmscache.FilmsCacheDao
import com.example.films.data.films.db.filmscache.entities.FilmCacheDbEntity

@Database(entities = [FavoriteFilmsDbEntity::class, FilmCacheDbEntity::class], version = 1)
@TypeConverters(value = [Converters::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
    abstract fun filmsCacheDao(): FilmsCacheDao
}
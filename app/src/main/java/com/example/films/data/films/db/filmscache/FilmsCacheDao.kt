package com.example.films.data.films.db.filmscache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.films.data.films.db.filmscache.entities.FilmCacheDbEntity

@Dao
interface FilmsCacheDao {

    @Query("SELECT * FROM films_cache_table " +
            "WHERE `offset` >= :startOffset AND `offset` < :endOffset AND `query` IS :query AND filter IS :filter " +
            "ORDER BY `offset` ")
    suspend fun getPage(startOffset: Int, endOffset: Int, query: String?, filter: String): List<FilmCacheDbEntity>

    @Insert(FilmCacheDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<FilmCacheDbEntity>)

    @Query("DELETE FROM films_cache_table WHERE created_at < :timeBefore")
    suspend fun clearAllBefore(timeBefore: Long)

}
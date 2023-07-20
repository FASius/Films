package com.example.films.data.films.db.favirites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.films.data.films.db.favirites.entities.FavoriteFilmTuple
import com.example.films.data.films.db.favirites.entities.FavoriteFilmsDbEntity

@Dao
interface FavoritesDao {

    @Query(
        "SELECT id, name, poster_url, rating, year FROM favorites_films " +
                "WHERE name LIKE :query ORDER BY " +
                "   CASE :filterBy WHEN 'name' THEN name END ASC, " +
                "   CASE :filterBy WHEN 'rating' THEN rating END DESC, " +
                "   CASE :filterBy WHEN 'votes' THEN votes END DESC " +
                "LIMIT :pageSize OFFSET :offset"
    )
    suspend fun getAll(
        pageSize: Int,
        offset: Int,
        filterBy: String,
        query: String?
    ): List<FavoriteFilmTuple>

    @Query(
        "SELECT id, name, poster_url, rating, year FROM favorites_films ORDER BY" +
                "   CASE :filterBy WHEN 'name' THEN name END DESC, " +
                "   CASE :filterBy WHEN 'rating' THEN rating END DESC, " +
                "   CASE :filterBy WHEN 'votes' THEN votes END DESC" +
                " LIMIT :pageSize OFFSET :offset"
    )
    suspend fun getAll(pageSize: Int, offset: Int, filterBy: String): List<FavoriteFilmTuple>

    @Query("SELECT * FROM favorites_films WHERE id = :filmId")
    suspend fun getDetails(filmId: Long): FavoriteFilmsDbEntity?

    @Insert(FavoriteFilmsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: FavoriteFilmsDbEntity)

    @Query("DELETE FROM favorites_films WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("DELETE FROM favorites_films")
    suspend fun removeAll()

    @Query("SELECT COUNT(*) FROM favorites_films WHERE id = :id")
    suspend fun countById(id: Long): Int

    @Query("SELECT COUNT(*) FROM favorites_films")
    suspend fun count(): Int

}
package ru.milovidov.android.films2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.milovidov.android.films2.entities.favoriteEntity


@Dao
interface FavoriteDao {

    @Insert
    fun insert(entity: favoriteEntity)

    @Query("DELETE FROM favorites where filmId = :filmId")
    fun delete(filmId: Int)

    @Query("DELETE FROM favorites")
    fun deleteAll()

    @Query("SELECT * FROM favorites")
    fun getAll(): List<favoriteEntity>
}
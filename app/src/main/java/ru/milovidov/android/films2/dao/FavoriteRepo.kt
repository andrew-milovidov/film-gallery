package ru.milovidov.android.films2.dao

import android.content.Context
import ru.milovidov.android.films2.entities.favoriteEntity

import ru.milovidov.android.films2.service.ExecutorService


class FavoriteRepo(val context: Context) {


    val database: FavoriteDatabase = FavoriteDatabase.getInstance(context)
    private var dao: FavoriteDao = database.dao()


    fun insert(filmId: Int) {
        ExecutorService.execute {
            dao.insert(favoriteEntity().apply { this.filmId = filmId })
        }
    }

    fun delete(filmId: Int) {
        ExecutorService.execute { dao.delete(filmId) }
    }

    fun deleteAll() {
        ExecutorService.execute { dao.deleteAll() }
    }

    fun getAll(): List<favoriteEntity> {
        return dao.getAll()
    }


}
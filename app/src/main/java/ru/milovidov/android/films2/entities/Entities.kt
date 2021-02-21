package ru.milovidov.android.films2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class favoriteEntity {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    var filmId:Int? = null
}
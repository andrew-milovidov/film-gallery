package ru.milovidov.android.films2.model

import android.graphics.Bitmap

data class GroupContract(
    val id: Int,
    val title: String,
    val films: MutableList<FilmContract>
)

data class FilmContract(
    val id: Int,
    val title: String,
    var favorite: Boolean,
    val imageUrl : String,
    var image : Bitmap
) {
    var status = ImageStatus.PENDIG
}

enum class ImageStatus {
    PENDIG, LOADED, ERROR
}
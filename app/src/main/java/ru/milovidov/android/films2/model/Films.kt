package ru.milovidov.android.films2.model

import com.squareup.moshi.Json

data class Films(
    val content: List<FilmGroup>

)

data class FilmGroup(
    val id: Int,
    val title: String,
    val cover: FilmCover,
    val content: List<FilmItem>

)

data class FilmItem(
    val id: Int,
    val title: String,
    val cover: FilmCover

)

data class FilmCover(
    val id: Int,
    @field:Json(name = "original_ext")
    val originalExt: String

)
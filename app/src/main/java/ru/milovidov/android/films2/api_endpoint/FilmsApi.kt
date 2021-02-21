package ru.milovidov.android.films2.api_endpoint

import io.reactivex.Single
import retrofit2.http.GET
import ru.milovidov.android.films2.model.Films

interface FilmsApi {

    @GET("api/main_page")
    fun getFilms(): Single<Films>
}
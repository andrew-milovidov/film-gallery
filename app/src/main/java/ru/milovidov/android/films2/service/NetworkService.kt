package ru.milovidov.android.films2.service

import io.reactivex.Single
import ru.milovidov.android.films2.api_endpoint.FilmsApi
import ru.milovidov.android.films2.di.DaggerApiComponent
import ru.milovidov.android.films2.model.Films
import javax.inject.Inject

class NetworkService {

    companion object {
        val BASE_URL = "https://signalmediacorp.com"
//        val BASE_URL = "http://192.168.31.152:8080"
    }

    init {
        DaggerApiComponent.create().inject(this)
    }

    @Inject
    lateinit var api: FilmsApi

    fun fetchFilms(): Single<Films> {
        return api.getFilms()
    }
}
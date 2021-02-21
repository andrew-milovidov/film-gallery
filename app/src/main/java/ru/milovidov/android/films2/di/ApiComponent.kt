package ru.milovidov.android.films2.di

import dagger.Component
import ru.milovidov.android.films2.service.NetworkService
import ru.milovidov.android.films2.viewmodel.FilmsViewModel

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(networkService: NetworkService)

    fun inject(filmsViewModel: FilmsViewModel)
}
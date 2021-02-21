package ru.milovidov.android.films2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ru.milovidov.android.films2.dao.FavoriteRepo
import ru.milovidov.android.films2.di.DaggerApiComponent
import ru.milovidov.android.films2.entities.favoriteEntity
import ru.milovidov.android.films2.model.FilmContract
import ru.milovidov.android.films2.model.GroupContract
import ru.milovidov.android.films2.service.ExecutorService
import ru.milovidov.android.films2.service.NetworkService
import ru.milovidov.android.films2.view.MainActivity
import javax.inject.Inject

class FilmsViewModel(val app: Application) : AndroidViewModel(app) {

    val favoriteRepo: FavoriteRepo = FavoriteRepo(app)

    init {
        DaggerApiComponent.create().inject(this)
        refresh()
    }

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var compositeDisposable: CompositeDisposable


    private val groups by lazy { MutableLiveData<List<GroupContract>>() }
    val groupsLD: LiveData<List<GroupContract>>
        get() = groups
    private val inProgress by lazy { MutableLiveData<Boolean>() }
    val inProgressLD: LiveData<Boolean>
        get() = inProgress
    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError


    private lateinit var favoriteMap: HashMap<Int, favoriteEntity>

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun refresh() {
        ExecutorService.execute { fetchFilms() }
    }

    fun setfavorite(groupPos: Int, filmPos: Int, favorite: Boolean) {
        val groupsVal = groups.value!!
        val fc = groupsVal[groupPos].films[filmPos]
        fc.favorite = favorite
        //groups.value = groupsVal
        processfavorite(fc.id, favorite)
    }

    private fun processfavorite(id: Int, favorite: Boolean) {
        if (favorite) {
            favoriteRepo.insert(id)
        } else {
            favoriteRepo.delete(id)
        }
    }

    private fun fetchFilms() {
        val dbTask = ExecutorService.executeWithFuture {
            favoriteMap = favoriteRepo.getAll().map { it.filmId!! to it }.toMap(HashMap())
            Unit
        }

        compositeDisposable.add(
            networkService.fetchFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    dbTask.get()
                    it.content
                }.map { list ->
                    list.map { g ->
                        GroupContract(g.id, g.title, g.content.map { fi ->
                            FilmContract(
                                fi.id,
                                fi.title,
                                favoriteMap.containsKey(fi.id),
                                NetworkService.BASE_URL + "/b/c/" + fi.cover.id + "." + fi.cover.originalExt,
                                MainActivity.Static.loadingBitmap
                            )
                        }.toMutableList())
                    }
                }
                .subscribeWith(createFilmsObserver())
        )
    }

    private fun createFilmsObserver(): DisposableSingleObserver<List<GroupContract>> {
        return object : DisposableSingleObserver<List<GroupContract>>() {

            override fun onSuccess(res: List<GroupContract>) {
                inProgress.value = true
                isError.value = false
                groups.value = res
                inProgress.value = false
            }

            override fun onError(e: Throwable) {
                inProgress.value = true
                isError.value = true
                Log.e("onError()", "Error: ${e.message}")
                inProgress.value = false
            }
        }
    }
}
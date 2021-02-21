package ru.milovidov.android.films2.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.milovidov.android.films2.R
import ru.milovidov.android.films2.adapter.GroupsAdapter
import ru.milovidov.android.films2.adapter.ItemClickGroupListener
import ru.milovidov.android.films2.viewmodel.FilmsViewModel

class MainActivity : AppCompatActivity() {

    lateinit var groupsAdapter: GroupsAdapter

    private val filmsViewModel: FilmsViewModel by viewModels()

    companion object Static {
        lateinit var warningBitmap: Bitmap
        lateinit var loadingBitmap: Bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        warningBitmap = (baseContext.resources.getDrawable(R.drawable.ic_warning, null) as VectorDrawable).toBitmap()
        loadingBitmap = (baseContext.resources.getDrawable(R.drawable.ic_downloading, null) as VectorDrawable).toBitmap()
        main_swipe_refresh_layout.setOnRefreshListener {
            main_swipe_refresh_layout.isRefreshing = false
            filmsViewModel.refresh()
        }
        groupsAdapter = GroupsAdapter(mutableListOf(), object : ItemClickGroupListener {
            override fun onItemClick(favorite: Boolean, groupPos: Int, filmPos: Int) {
                filmsViewModel.setfavorite(groupPos, filmPos, favorite)
            }
        })
        main_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupsAdapter
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeFilms()
    }

    private fun observeFilms() {
        filmsViewModel.groupsLD.observe(this, Observer { films ->
            films.let {
                main_recycler_view.visibility = View.VISIBLE
                groupsAdapter.setUpFilms(it)
            }
        })
    }

    private fun observeInProgress() {
        filmsViewModel.inProgressLD.observe(this, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    films_fetch_error.visibility = View.GONE
                    main_recycler_view.visibility = View.GONE
                    films_fetch_progress.visibility = View.VISIBLE
                } else {
                    films_fetch_progress.visibility = View.GONE
                }
            }
        })
    }

    private fun observeIsError() {
        filmsViewModel.isErrorLD.observe(this, Observer { isError ->
            isError.let {
                films_fetch_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}
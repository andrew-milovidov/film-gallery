package ru.milovidov.android.films2.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_film.view.*
import ru.milovidov.android.films2.R
import ru.milovidov.android.films2.databinding.FilmContractBinding
import ru.milovidov.android.films2.model.FilmContract
import ru.milovidov.android.films2.model.ImageStatus
import ru.milovidov.android.films2.service.ExecutorService
import ru.milovidov.android.films2.view.MainActivity
import java.net.URL
import kotlin.math.min

class FilmsViewHolder(
    val itemFilmBinding: FilmContractBinding,
    val groupPos: Int,
    val listener: ItemClickGroupListener
) :
    RecyclerView.ViewHolder(itemFilmBinding.root) {

    init {
        itemView.button_favor.setOnClickListener { v ->
            listener.onItemClick(true, groupPos, adapterPosition)
        }
        itemView.button_unfavor.setOnClickListener { v ->
            listener.onItemClick(false, groupPos, adapterPosition)
        }
    }
}

class FilmsAdapter(
    val groupPos: Int,
    val films: MutableList<FilmContract>,
    val listener: ItemClickGroupListener
) :
    RecyclerView.Adapter<FilmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val binding: FilmContractBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_film,
            parent,
            false
        )

        return FilmsViewHolder(binding, groupPos, object : ItemClickGroupListener {
            override fun onItemClick(favorite: Boolean, groupPos: Int, filmPos: Int) {
                listener.onItemClick(favorite, groupPos, filmPos)
                notifyItemChanged(filmPos)
            }
        })
    }

    override fun getItemCount(): Int = films.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        val filmContract = films[position]
        holder.itemFilmBinding.filmItem = filmContract
        holder.itemView.button_favor.visibility = if (filmContract.favorite) {
            View.GONE
        } else {
            View.VISIBLE
        }
        holder.itemView.button_unfavor.visibility = if (!filmContract.favorite) {
            View.GONE
        } else {
            View.VISIBLE
        }
        if (filmContract.status == ImageStatus.PENDIG) {
            ExecutorService.execute {
                try {
                    val b = BitmapFactory.decodeStream(
                        URL(filmContract.imageUrl).openConnection().getInputStream()
                    )
                    val w = holder.itemView.film_image.width
                    val h = holder.itemView.film_image.height
                    val dx = w.toFloat() / b.width
                    val dy = h.toFloat() / b.height
                    var scale = min(dx, dy)
                    if (scale == 0F) {
                        scale = 0.1F
                    }
                    filmContract.image = Bitmap.createScaledBitmap(
                        b,
                        (b.width * scale).toInt(),
                        (b.height * scale).toInt(),
                        false
                    )
                    holder.itemView.post {
                        holder.itemView.film_image.setImageBitmap(filmContract.image)
                    }
                    filmContract.status = ImageStatus.LOADED
                } catch (_: Exception) {
                    filmContract.status = ImageStatus.ERROR
                    filmContract.image = MainActivity.warningBitmap
                    holder.itemView.post {
                        holder.itemView.film_image.setImageBitmap(filmContract.image)
                    }
                }
            }
        }
        holder.itemView.post {
            holder.itemView.film_image.setImageBitmap(filmContract.image)
        }
    }
}
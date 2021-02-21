package ru.milovidov.android.films2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_group.view.*
import ru.milovidov.android.films2.R
import ru.milovidov.android.films2.databinding.GroupContractBinding
import ru.milovidov.android.films2.model.GroupContract

interface ItemClickGroupListener {
    fun onItemClick(favorite: Boolean, groupPos: Int, filmPos: Int)
}

class GroupsViewHolder(val itemGroupBinding: GroupContractBinding) :
    RecyclerView.ViewHolder(itemGroupBinding.root)

class GroupsAdapter(val groups: MutableList<GroupContract>, val listener: ItemClickGroupListener) :
    RecyclerView.Adapter<GroupsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        val binding: GroupContractBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_group,
            parent,
            false
        )


        return GroupsViewHolder(binding)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.itemGroupBinding.groupItem = groups[position]
        val childLayoutManager =
            LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        val view = holder.itemView.films_recycler_view
        view.layoutManager = childLayoutManager
        view.adapter = FilmsAdapter(
            position,
            ArrayList(groups[position].films), listener)
        view.setRecycledViewPool(RecyclerView.RecycledViewPool())
    }

    fun setUpFilms(otherFilms: List<GroupContract>) {
        groups.clear()
        groups.addAll(otherFilms)
        notifyDataSetChanged()
    }

}
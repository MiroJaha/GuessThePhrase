package com.example.guessthephrase

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class RecyclerViewAdapter(private val enters:List<String>,private var check : Boolean):RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val enter = enters[position]
        if (check) {
            holder.itemView.apply {
                tvResult.text = enter
                tvResult.setBackgroundColor(Color.GREEN)
            }
        } else {
            holder.itemView.apply {
                tvResult.text = enter
                tvResult.setBackgroundColor(Color.RED)
            }
        }
    }

    override fun getItemCount()=enters.size

}
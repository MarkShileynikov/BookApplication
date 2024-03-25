package com.example.mybookapplication.presentation.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.presentation.search.listener.OnGenreClickedListener

class GenreAdapter(private val genres: List<String>, private val itemClickedListener: OnGenreClickedListener) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_item, parent, false)
        return GenreViewHolder(view, itemClickedListener)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    class GenreViewHolder(itemView : View, private val itemClickedListener: OnGenreClickedListener): RecyclerView.ViewHolder(itemView) {
        val genre : TextView = itemView.findViewById(R.id.genre)

        fun bind(genre : String) {
            this.genre.text = genre
            itemView.setOnClickListener {
                itemClickedListener.genreClicked(genre)
            }
        }
    }
}
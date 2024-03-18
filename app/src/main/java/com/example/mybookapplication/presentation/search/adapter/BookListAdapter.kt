package com.example.mybookapplication.presentation.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Book

class BookListAdapter(private val books : List<Book>) : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(books[position])
    }

    class BookListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.bookTitle)
        private var author: TextView = itemView.findViewById(R.id.bookAuthor)
        fun bind(book : Book) {
            title.text = book.title
            author.text = book.author
        }
    }

}
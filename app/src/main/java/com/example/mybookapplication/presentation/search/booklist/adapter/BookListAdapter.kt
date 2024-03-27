package com.example.mybookapplication.presentation.search.booklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.presentation.search.listener.OnBookClickedListener

class BookListAdapter(private val books : List<Book>, private val bookClickedListener: OnBookClickedListener) : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookListViewHolder(view, bookClickedListener)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(books[position])
    }

    class BookListViewHolder(itemView : View, private val bookClickedListener: OnBookClickedListener) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.bookTitle)
        private var author: TextView = itemView.findViewById(R.id.bookAuthor)
        private var cover : ImageView = itemView.findViewById(R.id.bookCover)
        fun bind(book : Book) {
            title.text = book.title
            author.text = book.author
            cover.load(book.cover) {
                placeholder(R.drawable.default_cover)
                crossfade(true)
            }
            itemView.setOnClickListener {
                bookClickedListener.bookClicked(book)
            }
        }
    }

}
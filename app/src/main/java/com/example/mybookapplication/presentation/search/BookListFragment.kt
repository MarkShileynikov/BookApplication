package com.example.mybookapplication.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.presentation.search.adapter.BookListAdapter
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.launch

class BookListFragment : Fragment(){
    private lateinit var adapter: BookListAdapter
    private lateinit var genre : String
    private val viewModel : BookListViewModel by viewModels { BookListViewModel.bookListModelFactory(getGenre()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        genre = getGenre()
        view.findViewById<TextView>(R.id.genreHeader).text = genre

        observeBooks(view)
        return view
    }

    private fun setUpRecyclerView(view : View, books : List<Book>) {
        val recyclerView : RecyclerView = view.findViewById(R.id.bookList)
        adapter = BookListAdapter(books)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun getGenre() : String {
        return arguments?.getString(SearchFragment.GENRE_KEY) ?: ""
    }

    private fun observeBooks(view : View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is ViewState.Success -> {
                            Log.d("Запрос", "Success")
                            setUpRecyclerView(view, it.data)

                        }
                        is ViewState.Loading -> {
                            Log.d("Запрос", "Loading")
                        }
                        is ViewState.Failure -> {
                            Log.d("Запрос", "Failure")
                        }
                    }
                }
            }
        }
    }

}
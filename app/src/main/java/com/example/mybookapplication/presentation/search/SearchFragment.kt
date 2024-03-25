package com.example.mybookapplication.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.presentation.search.adapter.BookListAdapter
import com.example.mybookapplication.presentation.search.adapter.GenreAdapter
import com.example.mybookapplication.presentation.search.booklist.BookListFragment
import com.example.mybookapplication.presentation.search.listener.OnBookClickedListener
import com.example.mybookapplication.presentation.search.listener.OnGenreClickedListener
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search), OnGenreClickedListener, OnBookClickedListener {
    companion object {
        const val GENRE_KEY = "genre"
    }

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var searchView : EditText
    private lateinit var recyclerView : RecyclerView
    private val viewModel : SearchViewModel by viewModels { SearchViewModel.searchViewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindViews(view)
        super.onViewCreated(view, savedInstanceState)
    }
    private fun bindViews(view : View) {
        searchView = view.findViewById(R.id.bookSearchView)
        recyclerView = view.findViewById(R.id.genreList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        setUpGenreList()

        setUpSearch(view)

    }

    private fun setUpGenreList() {
        val genres : List<String> = listOf("Фэнтези", "Художественная литература", "Классика")
        genreAdapter = GenreAdapter(genres, this)
        recyclerView.adapter = genreAdapter
    }

    fun setUpSearch(view : View) {
        searchView.addTextChangedListener(object : TextWatcher {
            private var searchJob: Job? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                searchJob?.cancel()
                if (query.isNotBlank()) {
                    searchJob = lifecycleScope.launch {
                        delay(300)
                        viewModel.fetchBooksByTitleOrAuthor(query.trim().replace("\\s+".toRegex(), " "))
                        viewModel.viewState.collect {
                            when(it) {
                                is ViewState.Success -> {
                                    view.findViewById<TextView>(R.id.genresHeader).visibility = View.GONE
                                    setUpBookList(it.data)
                                }
                                is ViewState.Loading -> {
                                    //TODO
                                }
                                is ViewState.Failure -> {
                                    //TODO
                                }
                            }
                        }
                    }
                } else {
                    view.findViewById<TextView>(R.id.genresHeader).visibility = View.VISIBLE
                    setUpGenreList()
                }
            }
        })
    }

    private fun setUpBookList(books : List<Book>) {
        bookListAdapter = BookListAdapter(books, this)
        recyclerView.adapter = bookListAdapter
    }

    override fun genreClicked(genre : String) {
        val bundle = Bundle()
        bundle.putString(GENRE_KEY, genre)
        findNavController().navigate(R.id.to_book_list_screen, bundle)
    }

    override fun bookClicked(book: Book) {
        val bundle = Bundle()
        bundle.putParcelable(BookListFragment.BOOK_KEY, book)
        findNavController().navigate(R.id.action_searchFragment_to_bookFragment, bundle)
    }
}
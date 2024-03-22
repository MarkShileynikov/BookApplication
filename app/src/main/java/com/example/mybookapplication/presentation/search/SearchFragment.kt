package com.example.mybookapplication.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.presentation.search.adapter.BookListAdapter
import com.example.mybookapplication.presentation.search.adapter.GenreAdapter
import com.example.mybookapplication.presentation.util.ViewState
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), OnItemClickedListener {
    companion object {
        const val GENRE_KEY = "genre"
    }

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var searchView : EditText
    private lateinit var recyclerView : RecyclerView
    private val viewModel : SearchViewModel by viewModels { SearchViewModel.searchViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        setUpViews(view)

        return view
    }
    private fun setUpViews(view : View) {
        searchView = view.findViewById(R.id.bookSearchView)
        recyclerView = view.findViewById(R.id.genreList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        setUpGenreList()

        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                if (length == 0) {
                    setUpGenreList()
                    view.findViewById<TextView>(R.id.genresHeader).visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                if (length > 0) {
                    viewModel.fetchBooksByTitleOrAuthor(s.toString())
                    observeBooks(view)
                }
            }
        })
    }

    private fun setUpGenreList() {
        val genres : List<String> = listOf("Фэнтези", "Художественная литература", "Классика")
        genreAdapter = GenreAdapter(genres, this)
        recyclerView.adapter = genreAdapter
    }

    private fun setUpBookList(books : List<Book>) {
        bookListAdapter = BookListAdapter(books)
        recyclerView.adapter = bookListAdapter
    }

    override fun itemClicked(genre : String) {
        val bundle = Bundle()
        bundle.putString(GENRE_KEY, genre)
        findNavController().navigate(R.id.to_book_list_screen, bundle)
    }

    private fun observeBooks(view : View) {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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

        }
    }
}
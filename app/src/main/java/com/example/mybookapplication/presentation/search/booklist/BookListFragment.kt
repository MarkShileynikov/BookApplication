package com.example.mybookapplication.presentation.search.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.example.mybookapplication.presentation.search.SearchFragment
import com.example.mybookapplication.presentation.search.booklist.adapter.BookListAdapter
import com.example.mybookapplication.presentation.search.listener.OnBookClickedListener
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookListFragment : Fragment(), OnBookClickedListener{
    private lateinit var adapter: BookListAdapter
    private lateinit var genre : String
    private lateinit var backButton : ImageView
    private val viewModel : BookListViewModel by viewModels<BookListViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<BookListViewModel.BookListViewModelFactory> {factory ->
                factory.create(getGenre())
            }
        }
    )

    companion object {
        const val BOOK_KEY = "book"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)

        genre = getGenre()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)

        observeBooks(view)
    }

    private fun getGenre() : String {
        return arguments?.getString(SearchFragment.GENRE_KEY) ?: ""
    }

    private fun bindViews(view : View) {
        view.findViewById<TextView>(R.id.genreHeader).text = genre
        backButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeBooks(view : View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is ViewState.Success -> {
                            setUpRecyclerView(view, it.data)
                            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
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

    private fun setUpRecyclerView(view : View, books : List<Book>) {
        val recyclerView : RecyclerView = view.findViewById(R.id.bookList)
        adapter = BookListAdapter(books, this, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }


    override fun bookClicked(book: Book) {
        val bundle = Bundle()
        bundle.putParcelable(BOOK_KEY, book)
        findNavController().navigate(R.id.action_bookListFragment_to_bookFragment, bundle)
    }

}
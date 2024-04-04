package com.example.mybookapplication.presentation.search.book

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.FragmentBookBinding
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.util.Event
import com.example.mybookapplication.presentation.search.book.adapter.ReviewAdapter
import com.example.mybookapplication.presentation.search.book.review.ReviewActivity
import com.example.mybookapplication.presentation.search.booklist.BookListFragment
import com.example.mybookapplication.presentation.util.ViewState
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding: FragmentBookBinding get() = _binding!!
    private var isExpanded = false
    private val viewModel: BookViewModel by viewModels()
    private lateinit var userProfile: UserProfile
    private lateinit var book: Book
    private lateinit var adapter: ReviewAdapter

    companion object {
        const val BOOK_ID_KEY = "BOOK_ID_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
        const val USERNAME_KEY = "USERNAME_KEY"
        const val AVATAR_KEY = "AVATAR_KEY"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        bindViews()
        observeUserProfile()
        viewModel.fetchReviews()
        observeReviews()

    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun bindViews() {

        book = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BookListFragment.BOOK_KEY, Book::class.java) ?: provideEmptyBook()
        } else {
            arguments?.getParcelable(BookListFragment.BOOK_KEY) ?: provideEmptyBook()
        }
        viewModel.setBookId(book.id)

        binding.title.text = book.title
        binding.author.text = book.author
        binding.cover.load(book.cover) {
            placeholder(R.drawable.default_cover)
            crossfade(true)
        }
        binding.pagesCount.text = "${book.pages} ${getString(R.string.printed_pages)}"
        binding.ageLimit.text = "${book.ageLimit}+"
        binding.description.text = book.description
        binding.releaseYear.text = "${book.releaseYear} ${getString(R.string.year)}"

        binding.description.post {
            val linesCount = binding.description.lineCount
            val maxLines = 8
            binding.description.maxLines = maxLines
            if (linesCount <= maxLines) {
                binding.readMore.visibility = View.GONE
            } else {
                binding.readMore.setOnClickListener {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        binding.description.maxLines = Integer.MAX_VALUE
                        binding.readMore.text = requireContext().getString(R.string.hide)
                    } else {
                        binding.description.maxLines = 8
                        binding.readMore.text = requireContext().getString(R.string.read_more)
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.makeReview.setOnClickListener {
            moveToReviewScreen(book.id, userProfile.userId, userProfile.username, userProfile.avatar)
        }

        binding.readBook.setOnClickListener {
            launchPdfFromUrl(book.url, book.title)
        }

    }

    private fun launchPdfFromUrl(url: String?, title: String) {
        startActivity(
            PdfViewerActivity.launchPdfFromUrl(
                context = requireActivity(),
                pdfUrl = url,
                pdfTitle = title,
                saveTo = saveTo.ASK_EVERYTIME,
                enableDownload = false
            )
        )
    }

    private fun provideEmptyBook() = Book(
        id = "",
        title = "",
        author = "",
        genre = "",
        description = "",
        releaseYear = 0,
        ageLimit = 0,
        cover = "",
        pages = 0,
        url = ""
    )

    private fun observeUserProfile() {
        viewModel.viewModelScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileViewState.collect {
                    when(it) {
                        is Event.Success -> {
                            userProfile = it.data
                        }
                        is Event.Failure -> {

                        }
                    }
                }
            }
        }
    }

    private fun observeReviews() {
        viewModel.viewModelScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewViewState.collect {
                    when(it) {
                        is ViewState.Success -> {
                            val reviews = it.data
                            handleOnSuccess(reviews)
                        }
                        is ViewState.Failure -> {
                            binding.noReviews.visibility = View.VISIBLE
                            binding.noReviews.text = getString(R.string.check_internet_connection)
                        }
                        is ViewState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(reviews: List<Review>) {
        binding.progressBar.visibility = View.GONE
        binding.reviewList.visibility = View.VISIBLE
        binding.noReviews.visibility = View.GONE
        setUpRecyclerView(reviews)
        bindMark(reviews)
        if (isAlreadyReviewed(reviews)) {
            binding.makeReview.visibility = View.GONE
            binding.alreadyRead.visibility = View.VISIBLE
            binding.alreadyRead.text = getString(R.string.already_reviewed)
        }
    }

    private fun isAlreadyReviewed(reviews: List<Review>) : Boolean{
        for (review in reviews) {
            if (review.userId == userProfile.userId) {
                return true
            }
        }
        return false
    }

    private fun setUpRecyclerView(reviews: List<Review>) {
        if(reviews.isEmpty()) {
            binding.noReviews.visibility = View.VISIBLE
        } else {
            val recyclerView: RecyclerView = binding.reviewList
            adapter = ReviewAdapter(reviews)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
    }

    private val editReviewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchReviews()
            observeReviews()
        }
    }

    private fun moveToReviewScreen(bookId: String, userId: String, username: String, avatar: String?) {
        val intent = Intent(requireActivity(), ReviewActivity::class.java)
        intent.putExtra(BOOK_ID_KEY, bookId)
        intent.putExtra(USER_ID_KEY, userId)
        intent.putExtra(USERNAME_KEY, username)
        intent.putExtra(AVATAR_KEY, avatar)
        editReviewLauncher.launch(intent)
    }

    private fun bindMark(reviews: List<Review>) {
        if (reviews.isNotEmpty()) {
            var sum = 0.0
            for (review in reviews) {
                sum += review.estimation
            }
            val average = sum / reviews.size
            val mark = String.format("%.2f", average)
            binding.mark.text = mark
        }
        val count = reviews.size
        binding.marksCount.text = viewModel.pluralizeReaders(requireContext(), count)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
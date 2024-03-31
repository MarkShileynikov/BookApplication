package com.example.mybookapplication.presentation.search.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.FragmentBookBinding
import com.example.mybookapplication.domain.entity.Book
import com.example.mybookapplication.presentation.search.booklist.BookListFragment

class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!
    private var isExpanded = false
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
        bindViews()
    }

    private fun bindViews() {
        val book : Book? = arguments?.getParcelable(BookListFragment.BOOK_KEY)
        binding.title.text = book?.title
        binding.author.text = book?.author
        binding.cover.load(book?.cover) {
            placeholder(R.drawable.default_cover)
            crossfade(true)
        }
        binding.pagesCount.text = "${book?.pages} ${getString(R.string.printed_pages)}"
        binding.ageLimit.text = "${book?.ageLimit}+"
        binding.description.text = book?.description
        binding.releaseYear.text = "${book?.releaseYear} ${getString(R.string.year)}"

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

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
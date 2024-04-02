package com.example.mybookapplication.presentation.search.book.review

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.ActivityReviewBinding
import com.example.mybookapplication.presentation.search.book.BookFragment
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity(R.layout.activity_review) {
    private lateinit var binding: ActivityReviewBinding
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var userId: String
    private lateinit var bookId: String
    private lateinit var username: String
    private var estimation = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bindViews()
    }
    private fun bindViews() {
        binding.reviewInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                binding.characters.text = "$inputLength/500"
                if (inputLength > 500) {
                    binding.reviewInput.text?.delete(inputLength - 1, inputLength)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.close.setOnClickListener {
            finish()
        }

        binding.publish.setOnClickListener {
            userId = intent.getStringExtra(BookFragment.USER_ID_KEY) ?: ""
            bookId = intent.getStringExtra(BookFragment.BOOK_ID_KEY) ?: ""
            username = intent.getStringExtra(BookFragment.USERNAME_KEY) ?: ""

            val review = binding.reviewInput.text.toString()

            viewModel.postReview(userId = userId, username = username, bookId = bookId, estimation = estimation, review = review)
            observeEvents()
        }
    }

    fun onStarClick(view: View) {
        estimation = when(view.id) {
            R.id.star1 -> 1
            R.id.star2 -> 2
            R.id.star3 -> 3
            R.id.star4 -> 4
            R.id.star5 -> 5
            else -> 0
        }
        updateStars(estimation)
    }

    private fun updateStars(selectedStars: Int) {
        val maxStars = 5

        for (i in 1..maxStars) {
            val starId = resources.getIdentifier("star$i", "id", packageName)
            val starImageView = findViewById<ImageView>(starId)

            val isStarSelected = i <= selectedStars
            if (isStarSelected) {
                starImageView.setColorFilter(getColor(R.color.orange))
            }
            else {
                starImageView.setColorFilter(getColor(R.color.white))
            }
        }
    }

    private fun observeEvents() {
        viewModel.viewModelScope.launch {
            viewModel.viewState.collect {
                when(it) {
                    is ViewState.Success -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    is ViewState.Loading -> {
                        binding.publish.isEnabled = false
                    }
                    is ViewState.Failure -> {
                        binding.publish.isEnabled = true
                        Toast.makeText(this@ReviewActivity, getString(R.string.review_publish_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
package com.example.mybookapplication.presentation.search.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.entity.Review
import java.util.Calendar
import java.util.Locale

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    class ReviewViewHolder(item: View, private val context: Context) : RecyclerView.ViewHolder(item) {
        private var nickname: TextView = item.findViewById(R.id.nickName)
        private var mark: TextView = item.findViewById(R.id.mark)
        private var review: TextView = item.findViewById(R.id.review)
        private var date: TextView = item.findViewById(R.id.date)
        private var avatar: ImageView = item.findViewById(R.id.avatar)
        private var isExpanded = false

        fun bind(review: Review) {
            nickname.text = review.username
            mark.text = review.estimation.toString()
            this.review.text = review.review

            date.text = transformDate(review.publicationDate)

            if (review.avatar != "") {
                avatar.load(review.avatar) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }

            val linesCount = this.review.lineCount
            val maxLines = 8
            val readMore = itemView.findViewById<TextView>(R.id.readMore)
            this.review.maxLines = maxLines
            if (linesCount <= maxLines) {
                readMore.visibility = View.GONE
            } else {
                readMore.setOnClickListener {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        this.review.maxLines = Integer.MAX_VALUE
                        readMore.text = context.getString(R.string.hide)
                    } else {
                        this.review.maxLines = 8
                        readMore.text = context.getString(R.string.read_more)
                    }
                }
            }
        }

        private fun transformDate(millis: Long) : String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru"))
            val year = calendar.get(Calendar.YEAR)
            return "$dayOfMonth $month $year"
        }
    }
}
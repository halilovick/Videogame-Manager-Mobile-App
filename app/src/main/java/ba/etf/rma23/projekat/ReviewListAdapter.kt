package ba.etf.rma23.projekat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
class ReviewListAdapter(
    private var reviews: List<UserImpression>,
) : RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.reviewUser.text = reviews[position].username
        holder.reviewTimestamp.text = reviews[position].timestamp.toString()
        val userImpression = reviews[position]
        if (reviews[position] is UserRating) {
            val rating = userImpression as UserRating
            //holder.reviewRating.setRating(reviews[position].rating!!.toFloat())
            holder.reviewRating.rating = rating.rating.toFloat()
            holder.reviewRating.setIsIndicator(true)
            holder.reviewText.isVisible = false
        }
        if (reviews[position] is UserReview) {
            val review = userImpression as UserReview
            holder.reviewRating.isVisible = false
            //holder.reviewText.text = reviews[position].review
            holder.reviewText.text = review.review
        }
    }

    override fun getItemCount(): Int = reviews.size

    fun updateReviews(list: List<UserImpression>) {
        this.reviews = list
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewUser: TextView = itemView.findViewById(R.id.username_textview)
        val reviewRating: RatingBar = itemView.findViewById(R.id.rating_bar)
        val reviewText: TextView = itemView.findViewById(R.id.review_textview)
        val reviewTimestamp: TextView = itemView.findViewById(R.id.timestamp_textview)
    }
}
package com.example.videogameview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameDetailsFragment : Fragment() {
    private lateinit var game: Game
    private lateinit var title: TextView
    private lateinit var platform: TextView
    private lateinit var releaseDate: TextView
    private lateinit var esrbRating: TextView
    private lateinit var developer: TextView
    private lateinit var publisher: TextView
    private lateinit var genre: TextView
    private lateinit var description: TextView
    private lateinit var coverImage: ImageView
    private lateinit var homeButton: Button
    private lateinit var reviewView: RecyclerView
    private lateinit var reviewAdapter: ReviewListAdapter
    private lateinit var detailsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_details, container, false)
        title = view.findViewById(R.id.game_title_textview)
        platform = view.findViewById(R.id.platform_textview)
        releaseDate = view.findViewById(R.id.release_date_textview)
        esrbRating = view.findViewById(R.id.esrb_rating_textview)
        developer = view.findViewById(R.id.developer_textview)
        publisher = view.findViewById(R.id.publisher_textview)
        genre = view.findViewById(R.id.genre_textview)
        description = view.findViewById(R.id.description_textview)
        coverImage = view.findViewById(R.id.cover_imageview)
        detailsButton = view.findViewById(R.id.details_button)
        detailsButton.isEnabled = false
        game = GameData.getDetails(requireArguments().getString("game_title", ""))!!
        populateDetails()
        homeButton = view.findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java).apply {
                putExtra("DetailsButton", true)
                putExtra("GameName", game.title)
            }
            startActivity(intent)
        }
        reviewView = view.findViewById(R.id.reviews_recycler_view)
        reviewView.layoutManager = GridLayoutManager(activity, 1)
        reviewAdapter = ReviewListAdapter(game.userImpressions)
        reviewView.adapter = reviewAdapter
        reviewAdapter.updateReviews(game.userImpressions)
        return view
    }

    companion object {
        fun newInstance(): GameDetailsFragment = GameDetailsFragment()
    }

    private fun getGameByTitle(title: String): Game {
        var gameList = GameData.getAll()
        for (game in gameList) {
            if (game.title == title) {
                return game
            }
        }
        return Game("", "", "", 0.0, "", "", "", "", "", "", mutableListOf<UserImpression>())
    }

    private fun populateDetails() {
        title.text = game.title
        releaseDate.text = game.releaseDate
        platform.text = game.platform
        esrbRating.text = game.esrbRating
        developer.text = game.developer
        publisher.text = game.publisher
        genre.text = game.genre
        description.text = game.description
        //Glide.with(this).load(game.coverImage).placeholder(R.drawable.noimagefound).dontAnimate().into(coverImage)
        val context: Context = coverImage.context
        var id: Int = context.resources
            .getIdentifier(game.coverImage, "drawable", context.packageName)
        if (id===0) id=context.resources
            .getIdentifier("noimagefound", "drawable", context.packageName)
        coverImage.setImageResource(id)
    }
}
package com.example.videogameview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GameDetailsActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        title = findViewById(R.id.game_title_textview)
        platform = findViewById(R.id.platform_textview)
        releaseDate = findViewById(R.id.release_date_textview)
        esrbRating = findViewById(R.id.esrb_rating_textview)
        developer = findViewById(R.id.developer_textview)
        publisher = findViewById(R.id.publisher_textview)
        genre = findViewById(R.id.genre_textview)
        description = findViewById(R.id.description_textview)
        coverImage = findViewById(R.id.cover_imageview)
        detailsButton = findViewById(R.id.details_button)
        detailsButton.isEnabled = false
        val extras = intent.extras
        if (extras != null) {
            game = GameData.getDetails(extras.getString("game_title", ""))!!
            populateDetails()
        } else {
            finish()
        }
        homeButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("DetailsButton", true)
                putExtra("GameName", game.title)
            }
            startActivity(intent)
        }
        reviewView = findViewById(R.id.reviews_recycler_view)
        reviewView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        reviewAdapter = ReviewListAdapter(game.userImpressions)
        reviewView.adapter = reviewAdapter
        reviewAdapter.updateReviews(game.userImpressions)
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
        Glide.with(this).load(game.coverImage).placeholder(R.drawable.noimagefound).dontAnimate().into(coverImage)
    }
}
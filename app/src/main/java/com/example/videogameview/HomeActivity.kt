package com.example.videogameview

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var videoGames: RecyclerView
    private lateinit var videoGamesAdapter: VideogameListAdapter
    private var videoGamesList = GameData.getAll()
    private lateinit var detailsButton: Button
    private var accessed = false
    private lateinit var game: Game

    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val extras = intent.extras
        if (extras != null) {
            accessed = extras.getBoolean("DetailsButton")
            game = GameData.getDetails(extras.getString("GameName"))!!
        } else {
            accessed = false
            //finish()
        }
        videoGames = findViewById(R.id.game_list)
        videoGames.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        videoGamesAdapter = VideogameListAdapter(arrayListOf()) { game -> showGameDetails(game) }
        videoGames.adapter = videoGamesAdapter
        videoGamesAdapter.updateGames(videoGamesList)
        detailsButton = findViewById(R.id.details_button)
        detailsButton.isEnabled = accessed
        detailsButton.setOnClickListener {
            showGameDetails(game)
        }
        searchBar = findViewById(R.id.search_query_edittext)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase(Locale.getDefault())
                val filteredGames = videoGamesList.filter {
                    it.title.lowercase(Locale.getDefault()).contains(query)
                }
                videoGamesAdapter =
                    VideogameListAdapter(arrayListOf()) { game -> showGameDetails(game) }
                videoGames.adapter = videoGamesAdapter
                videoGamesAdapter.updateGames(filteredGames)
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
        })
    }

    private fun showGameDetails(game: Game) {
        val intent = Intent(this, GameDetailsActivity::class.java).apply {
            putExtra("game_title", game.title)
        }
        startActivity(intent)
    }
}

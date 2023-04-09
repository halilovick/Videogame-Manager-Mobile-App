package com.example.videogameview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var videoGames: RecyclerView
    private lateinit var videoGamesAdapter: VideogameListAdapter
    private var videoGamesList = GameData.getAll()
    private lateinit var detailsButton: Button
    private var accessed = false
    private lateinit var game: Game

    private lateinit var searchBar: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        val extras = activity?.intent?.extras
        if (extras != null) {
            accessed = extras.getBoolean("DetailsButton")
            game = GameData.getDetails(extras.getString("GameName"))!!
        } else {
            accessed = false
            //finish()
        }
        videoGames = view.findViewById(R.id.game_list)
        videoGames.layoutManager = GridLayoutManager(activity, 1)
        videoGamesAdapter = VideogameListAdapter(arrayListOf()) { game -> showGameDetails(game) }
        videoGames.adapter = videoGamesAdapter
        videoGamesAdapter.updateGames(videoGamesList)
        detailsButton = view.findViewById(R.id.details_button)
        detailsButton.isEnabled = accessed
        detailsButton.setOnClickListener {
            showGameDetails(game)
        }
        searchBar = view.findViewById(R.id.search_query_edittext)
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
        return view
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private fun showGameDetails(game: Game) {
        val fragment = GameDetailsFragment()
        val args = Bundle()
        args.putString("game_title", game.title)
        fragment.arguments = args
        findNavController().navigate(R.id.gameDetailsItem)
        parentFragmentManager.beginTransaction()
            .replace(((view as ViewGroup).parent as View).id, fragment)
            .addToBackStack(null)
            .commit()
    }
}
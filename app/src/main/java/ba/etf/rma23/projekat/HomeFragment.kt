package com.example.videogameview

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var videoGames: RecyclerView
    private lateinit var videoGamesAdapter: VideogameListAdapter
    private var videoGamesList = GameData.getAll()
    private var accessed = false
    private lateinit var game: Game

    private lateinit var searchBar: EditText
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                val args = HomeFragmentArgs.fromBundle(requireArguments())
                accessed = true
                game = GameData.getDetails(args.gameTitle)!!
            } catch (e: java.lang.IllegalArgumentException) {
            } catch (e: java.lang.IllegalStateException) {
            } catch (e: java.lang.NullPointerException) {
            }
        }

        videoGames = view.findViewById(R.id.game_list)
        videoGames.layoutManager = GridLayoutManager(activity, 1)
        videoGamesAdapter = VideogameListAdapter(arrayListOf()) { game -> showGameDetails(game) }
        videoGames.adapter = videoGamesAdapter
        videoGamesAdapter.updateGames(videoGamesList)

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                bottomNavView = requireActivity().findViewById(R.id.bottomNavigation)
                bottomNavView.menu.findItem(R.id.gameDetailsItem).isEnabled = accessed
                val detailsNavButton = bottomNavView.findViewById<View>(R.id.gameDetailsItem)
                val homeNavButton = bottomNavView.findViewById<View>(R.id.homeItem)
                if(::game.isInitialized) {
                    detailsNavButton.setOnClickListener {
                        findNavController().navigate(
                            GameDetailsFragmentDirections.actionHomeToGameDetails(
                                game.title
                            )
                        )
                    }
                } else{
                    detailsNavButton.isEnabled = false
                    homeNavButton.isEnabled = false
                }
            } catch (e: java.lang.NullPointerException) {
            }
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
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findNavController().navigate(HomeFragmentDirections.actionHomeToGameDetails(game.title))
        } else {
            val fragment = GameDetailsFragment()
            val args = Bundle()
            args.putString("gameTitle", game.title)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.details_fragment_container, fragment).addToBackStack(null).commit()
        }
    }
}
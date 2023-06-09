package ba.etf.rma23.projekat

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.data.repositories.GamesRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var videoGames: RecyclerView
    private lateinit var videoGamesAdapter: VideogameListAdapter
    private lateinit var videoGamesList: List<Game>
    private var accessed = false
    private lateinit var game: Game

    private lateinit var searchBar: EditText
    private lateinit var searchbutton: Button
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var sortButton: Button
    private lateinit var favoritesButton: Button
    private lateinit var allButton: Button

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
            } catch (_: java.lang.IllegalArgumentException) {
            } catch (_: java.lang.IllegalStateException) {
            } catch (_: java.lang.NullPointerException) {
            }
        }

        videoGamesList = GameData.getAll()
        videoGames = view.findViewById(R.id.game_list)
        videoGames.layoutManager = GridLayoutManager(activity, 1)
        videoGamesAdapter = VideogameListAdapter(arrayListOf()) { game -> showGameDetails(game) }
        videoGames.adapter = videoGamesAdapter
        //videoGamesAdapter.updateGames(videoGamesList)
        //AccountGamesRepository.setHash("f84654a6-73a4-45ac-ab4f-84fa58850896")
        getGamesByName("")
        sortButton = view.findViewById(R.id.sort_button)
        sortButton.setOnClickListener {
            sortGames()
        }
        allButton = view.findViewById(R.id.all_button)
        allButton.setOnClickListener {
            getGamesByName("")
        }
        favoritesButton = view.findViewById(R.id.favorites_button)
        favoritesButton.setOnClickListener {
            getSavedGames()
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                bottomNavView = requireActivity().findViewById(R.id.bottomNavigation)
                bottomNavView.menu.findItem(R.id.gameDetailsItem).isEnabled = accessed
                val detailsNavButton = bottomNavView.findViewById<View>(R.id.gameDetailsItem)
                val homeNavButton = bottomNavView.findViewById<View>(R.id.homeItem)
                if (::game.isInitialized) {
                    detailsNavButton.setOnClickListener {
                        findNavController().navigate(
                            GameDetailsFragmentDirections.actionHomeToGameDetails(
                                game.title
                            )
                        )
                    }
                } else {
                    detailsNavButton.isEnabled = false
                    homeNavButton.isEnabled = false
                }
            } catch (e: java.lang.NullPointerException) {
            }
        }

        searchBar = view.findViewById(R.id.search_query_edittext)
        searchbutton = view.findViewById(R.id.search_button)
        searchbutton.setOnClickListener {
            getGamesByName(searchBar.text.toString())
        }

        return view
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private fun showGameDetails(game: Game) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val t = game.title
            findNavController().navigate(HomeFragmentDirections.actionHomeToGameDetails(t))
        } else {
            val fragment = GameDetailsFragment()
            val args = Bundle()
            args.putString("gameTitle", game.title)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.details_fragment_container, fragment).addToBackStack(null).commit()
        }
        //print(game.toString()+ "\n")
    }

    fun getGamesByName(name: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = GamesRepository.getGamesByName(name)
            GamesRepository.games = result as MutableList<Game>
            videoGamesAdapter.updateGames(result)
            //print(result)
        }
    }

    fun getSavedGames() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = AccountGamesRepository.getSavedGames()
            videoGamesAdapter.updateGames(result)
        }
    }

    fun sortGames() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = GamesRepository.sortGames()
            videoGamesAdapter.updateGames(result)
            //print(result)
        }
    }

}
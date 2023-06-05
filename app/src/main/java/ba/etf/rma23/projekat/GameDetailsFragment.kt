package ba.etf.rma23.projekat

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.data.repositories.GamesRepository
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class GameDetailsFragment() : Fragment() {
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
    private lateinit var reviewView: RecyclerView
    private lateinit var reviewAdapter: ReviewListAdapter
    private lateinit var userImpressions: List<UserImpression>
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var reviewsHeader: TextView
    private lateinit var games: List<Game>

    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_details, container, false)
        val orientation = resources.configuration.orientation

        title = view.findViewById(R.id.game_title_textview)
        platform = view.findViewById(R.id.platform_textview)
        releaseDate = view.findViewById(R.id.release_date_textview)
        esrbRating = view.findViewById(R.id.esrb_rating_textview)
        developer = view.findViewById(R.id.developer_textview)
        publisher = view.findViewById(R.id.publisher_textview)
        genre = view.findViewById(R.id.genre_textview)
        description = view.findViewById(R.id.description_textview)
        coverImage = view.findViewById(R.id.cover_imageview)
        reviewsHeader = view.findViewById(R.id.reviewsHeaderTextView)

        addButton = view.findViewById(R.id.add_to_favorites_button)
        //games = getGamesByName("")

        try {
            val extras = GameDetailsFragmentArgs.fromBundle(requireArguments())
            //game = getGamesByName(extras.gameTitle)[0]
            //print(game.title + "\n")
            game = getGameByTitle(extras.gameTitle)
            if (::game.isInitialized) {
                populateDetails(game)
            }
        } catch (e: java.lang.IllegalStateException) {
            /*if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                game = games[0]
                populateDetails(game)
            }*/
        }

        CoroutineScope(Dispatchers.Main).launch {
            games = getSavedGames()

            if (games.contains(game)) {
                addButton.setText("Remove from favorites")
            } else {
                addButton.setText("Add to favorites")
            }

            addButton.setOnClickListener {
                if (games.contains(game)) {
                    addButton.setText("Remove from favorites")
                    removeGame(game)
                    addButton.setText("Add to favorites")
                } else {
                    addButton.setText("Add to favorites")
                    saveGame(game)
                    addButton.setText("Remove from favorites")
                }
            }
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                bottomNavView = requireActivity().findViewById(R.id.bottomNavigation)
                val homeNavButton = bottomNavView.findViewById<View>(R.id.homeItem)
                homeNavButton.setOnClickListener {

                    if (!::game.isInitialized) {
                        game = getGameByTitle(title.toString())
                    }
                    val action = HomeFragmentDirections.actionGameDetailsToHome(game.title)
                    findNavController().navigate(action)
                }
            } catch (_: java.lang.NullPointerException) {
            }
        }

        reviewView = view.findViewById(R.id.reviews_recycler_view)
        reviewView.layoutManager = GridLayoutManager(activity, 1)
        if (::userImpressions.isInitialized) {
            reviewAdapter = ReviewListAdapter(userImpressions)
            reviewView.adapter = reviewAdapter
            reviewAdapter.updateReviews(userImpressions)
        }
        return view
    }

    private fun getGameByTitle(title: String): Game {
        val gameList = GamesRepository.games
        for (game in gameList) {
            if (game.title == title) {
                return game
            }
        }
        return Game(0, "", "", "", 0.0, "", "", "", "", "", "", mutableListOf<UserImpression>())
    }

    @SuppressLint("SetTextI18n")
    private fun populateDetails(game: Game) {
        title.text = game.title
        releaseDate.text = "Release date: " + game.releaseDate
        platform.text = "Plaform: " + game.platform
        esrbRating.text = "ESRB rating: " + game.esrbRating
        developer.text = "Developer: " + game.developer
        publisher.text = "Publisher: " + game.publisher
        genre.text = "Genre: " + game.genre
        description.text = game.description
        userImpressions = game.userImpressions
        if (game.userImpressions.isEmpty()) reviewsHeader.text = "No reviews yet!"
        val context: Context = coverImage.context
        /*var id: Int = context.resources
            .getIdentifier(game.coverImage, "drawable", context.packageName)
        if (id === 0) id = context.resources
            .getIdentifier("noimagefound", "drawable", context.packageName)
        coverImage.setImageResource(id)*/
        Glide.with(coverImage.context).load("https:/" + game.coverImage).into(coverImage)
    }

    fun getGamesByName(name: String): List<Game> {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var games: MutableList<Game> = mutableListOf()
        scope.launch {
            val result = GamesRepository.getGamesByName(name)
            games = result as MutableList<Game>
        }
        return games
    }

    fun saveGame(game: Game) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = AccountGamesRepository.saveGame(game)
        }
    }

    fun removeGame(game: Game) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = AccountGamesRepository.removeGame(game)
        }
    }

    suspend fun getSavedGames(): List<Game> {
        val deferredResult = CompletableDeferred<List<Game>>()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            try {
                val result = AccountGamesRepository.getSavedGames()
                deferredResult.complete(result)
            } catch (e: Exception) {
                deferredResult.completeExceptionally(e)
            }
        }
        return deferredResult.await()
    }
}
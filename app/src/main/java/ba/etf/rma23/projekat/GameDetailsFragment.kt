package ba.etf.rma23.projekat

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma23.projekat.data.repositories.AccountGamesRepository
import ba.etf.rma23.projekat.data.repositories.GameReview
import ba.etf.rma23.projekat.data.repositories.GameReviewsRepository
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
    var userImpressions: MutableList<UserImpression> = mutableListOf()
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var reviewsHeader: TextView
    private lateinit var games: List<Game>

    private lateinit var addButton: Button
    private lateinit var submitReviewButton: Button
    private lateinit var review_text: EditText
    private lateinit var rating_text: EditText

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
        submitReviewButton = view.findViewById(R.id.submit_review_button)
        review_text = view.findViewById(R.id.review_edittext)
        rating_text = view.findViewById(R.id.rating_edittext)

        CoroutineScope(Dispatchers.Main).launch {
            GamesRepository.GamesRepository()

            try {
                val extras = GameDetailsFragmentArgs.fromBundle(requireArguments())
                game = getGameByTitle(extras.gameTitle)
                if (::game.isInitialized) {
                    populateDetails(game)
                    getReviewsForGame(game.id)
                }
            } catch (e: java.lang.IllegalStateException) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    game = GamesRepository.games[0]
                    populateDetails(game)
                    getReviewsForGame(game.id)
                }
            }

            games = getSavedGames()

            if (games.contains(game)) {
                addButton.setText("Remove from favorites")
            } else {
                addButton.setText("Add to favorites")
            }

            addButton.setOnClickListener {
                if (games.contains(game)) {
                    addButton.setText("Remove from favorites")
                    removeGame(game.id)
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


        submitReviewButton.setOnClickListener {
            context?.let {
                try {
                    val rating = rating_text.text.toString().toInt()
                    val review = review_text.text.toString()
                    if (review == "") {
                        sendReview(
                            GameReview(
                                rating,
                                null,
                                game.id
                            ), it
                        )
                    } else {
                        sendReview(
                            GameReview(
                                rating,
                                review_text.text.toString(),
                                game.id
                            ), it
                        )
                    }
                } catch (exception: java.lang.NumberFormatException) {
                    val review = review_text.text.toString()
                    if (review == "") {
                        sendReview(
                            GameReview(
                                null,
                                null,
                                game.id
                            ), it
                        )
                    } else {
                        sendReview(
                            GameReview(
                                null,
                                review_text.text.toString(),
                                game.id
                            ), it
                        )
                    }
                }
                reviewAdapter = ReviewListAdapter(userImpressions)
                reviewView.adapter = reviewAdapter
                reviewAdapter.updateReviews(userImpressions)
            }
        }
        return view
    }

    private fun getGameByTitle(title: String): Game {
        val gameList = GamesRepository.games
        //val gameList = getGamesByName(title)
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
        Glide.with(coverImage.context).load("https:/" + game.coverImage).into(coverImage)
    }

    fun saveGame(game: Game) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = AccountGamesRepository.saveGame(game)
        }
    }

    fun removeGame(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = AccountGamesRepository.removeGame(id)
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

    fun sendReview(review: GameReview, context: Context) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = GameReviewsRepository.sendReview(context, review)
            println(result)
        }
    }

    fun getReviewsForGame(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val result = GameReviewsRepository.getReviewsForGame(id)

            for (gameReview in result) {

                if (gameReview.review != null && gameReview.review != "") {
                    userImpressions.add(
                        UserReview(
                            gameReview.student!!,
                            gameReview.timestamp!!,
                            gameReview.review!!
                        )
                    )
                } else if (gameReview.rating != null) {
                    userImpressions.add(
                        UserRating(
                            gameReview.student!!,
                            gameReview.timestamp!!,
                            gameReview.rating!!.toDouble()
                        )
                    )
                }
            }
            reviewAdapter = ReviewListAdapter(userImpressions)
            reviewView.adapter = reviewAdapter
            reviewAdapter.updateReviews(userImpressions)
        }
    }
}
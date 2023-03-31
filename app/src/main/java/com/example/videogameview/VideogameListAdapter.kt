package com.example.videogameview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideogameListAdapter(
    private var videogames: List<Game>,
    private val onItemClicked: (game: Game) -> Unit
) : RecyclerView.Adapter<VideogameListAdapter.GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int = videogames.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.gameTitle.text = videogames[position].title;
        holder.gameRating.text = videogames[position].rating.toString()
        holder.gamePlatform.text = videogames[position].platform
        holder.gameReleaseDate.text = videogames[position].releaseDate
        holder.itemView.setOnClickListener { onItemClicked(videogames[position]) }
    }

    fun updateGames(game: List<Game>) {
        this.videogames = game
        notifyDataSetChanged()
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameTitle: TextView = itemView.findViewById(R.id.game_title_textview)
        val gameRating: TextView = itemView.findViewById(R.id.game_rating_textview)
        val gameReleaseDate: TextView = itemView.findViewById(R.id.game_release_date_textview)
        val gamePlatform: TextView = itemView.findViewById(R.id.game_platform_textview)
    }
}
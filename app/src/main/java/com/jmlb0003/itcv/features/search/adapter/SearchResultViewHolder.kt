package com.jmlb0003.itcv.features.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R

class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val usernameView = view.findViewById<TextView>(R.id.github_username)
    private val favoriteIcon = view.findViewById<ImageView>(R.id.is_favorite_icon)

    fun bindView(
        searchResult: SearchResult,
        onResultClicked: (SearchResult) -> Unit
    ) {
        usernameView.text = searchResult.username
        setFavoriteIcon(searchResult.isFavorite)

        itemView.setOnClickListener { onResultClicked(searchResult) }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        favoriteIcon.visibility = View.GONE
        return
        // TODO Implement is-favorite feature
//        val isFavoriteIcon = if (isFavorite) {
//            R.drawable.ic_hearth
//        } else {
//            R.drawable.ic_hearth_line
//        }
//        favoriteIcon.setImageResource(isFavoriteIcon)
    }
}

package com.jmlb0003.itcv.features.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R

class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val profileNameView = view.findViewById<TextView>(R.id.profile_name)
    val favoriteIcon = view.findViewById<ImageView>(R.id.is_favorite_icon)

    fun bindView(searchResult: SearchResult) {
        profileNameView.text = searchResult.profileName
        val isFavoriteIcon = if (searchResult.isFavorite) {
            R.drawable.ic_hearth
        } else {
            R.drawable.ic_hearth_line
        }
        favoriteIcon.setImageResource(isFavoriteIcon)
    }
}

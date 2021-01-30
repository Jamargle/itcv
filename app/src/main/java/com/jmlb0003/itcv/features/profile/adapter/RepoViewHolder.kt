package com.jmlb0003.itcv.features.profile.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.jmlb0003.itcv.R

class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val nameView = view.findViewById<TextView>(R.id.repository_name)
    private val descriptionView = view.findViewById<TextView>(R.id.repository_description)
    private val webSiteButton = view.findViewById<MaterialButton>(R.id.repository_website_button)
    private val repositoryButton = view.findViewById<MaterialButton>(R.id.repository_link_button)

    fun bindView(
        repoListItem: RepoListItem,
        onWebsiteButtonClick: (String) -> Unit,
        onRepositoryButtonClick: (String) -> Unit
    ) {
        nameView.text = repoListItem.name
        descriptionView.text = repoListItem.description

        webSiteButton.visibility = if (repoListItem.website.isEmpty()) {
            View.INVISIBLE
        } else {
            webSiteButton.setOnClickListener { onWebsiteButtonClick(repoListItem.website) }
            View.VISIBLE
        }

        repositoryButton.visibility = if (repoListItem.repoUrl.isEmpty()) {
            View.INVISIBLE
        } else {
            repositoryButton.setOnClickListener { onRepositoryButtonClick(repoListItem.repoUrl) }
            View.VISIBLE
        }
    }
}

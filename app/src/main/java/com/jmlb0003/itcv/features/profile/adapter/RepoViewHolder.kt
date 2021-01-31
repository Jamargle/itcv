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
    private val watchersView = view.findViewById<TextView>(R.id.repo_watchers_count)
    private val starsView = view.findViewById<TextView>(R.id.repo_stars_count)
    private val forksView = view.findViewById<TextView>(R.id.repo_forks_count)

    fun bindView(
        repoListItem: RepoListItem,
        onWebsiteButtonClick: (String) -> Unit,
        onRepositoryButtonClick: (String) -> Unit
    ) {
        with(repoListItem) {

            bindNameView(name)
            bindDescriptionView(description)
            bindWatchersView(watchersCount)
            bindStarsView(starsCount)
            bindForksView(forksCount)
            bindWebsiteButton(website, onWebsiteButtonClick)
            bindGithubRepositoryButton(repoUrl, onRepositoryButtonClick)
        }
    }

    private fun bindNameView(name: String) {
        nameView.let {
            it.text = name
            it.contentDescription =
                itemView.resources.getString(R.string.profile_details_repository_name_content_description, name)
        }
    }

    private fun bindDescriptionView(description: String) {
        descriptionView.visibility = if (description.isBlank()) {
            View.GONE
        } else {
            descriptionView.text = description
            View.VISIBLE
        }
    }

    private fun bindWatchersView(watchersCount: Int) {
        watchersView.let {
            it.text = watchersCount.toString()
            it.contentDescription =
                if (watchersCount > 0) {
                    String.format(
                        itemView.resources.getQuantityString(
                            R.plurals.profile_details_repository_watchers_content_description,
                            watchersCount
                        ),
                        watchersCount
                    )
                } else {
                    itemView.resources.getString(R.string.no_watchers)
                }
        }
    }

    private fun bindStarsView(starsCount: Int) {
        starsView.let {
            it.text = starsCount.toString()
            it.contentDescription =
                if (starsCount > 0) {
                    String.format(
                        itemView.resources.getQuantityString(
                            R.plurals.profile_details_repository_stars_content_description,
                            starsCount
                        ),
                        starsCount
                    )
                } else {
                    itemView.resources.getString(R.string.no_stars)
                }
        }
    }

    private fun bindForksView(forksCount: Int) {
        forksView.let {
            it.text = forksCount.toString()
            it.contentDescription =
                if (forksCount > 0) {
                    String.format(
                        itemView.resources.getQuantityString(
                            R.plurals.profile_details_repository_forks_content_description,
                            forksCount
                        ),
                        forksCount
                    )
                } else {
                    itemView.resources.getString(R.string.no_forks)
                }
        }
    }

    private fun bindWebsiteButton(website: String, onWebsiteButtonClick: (String) -> Unit) {
        webSiteButton.visibility = if (website.isEmpty()) {
            View.INVISIBLE
        } else {
            webSiteButton.setOnClickListener { onWebsiteButtonClick(website) }
            webSiteButton.contentDescription =
                itemView.resources.getString(R.string.profile_details_repository_website_link_button_content_description)
            View.VISIBLE
        }
    }

    private fun bindGithubRepositoryButton(repoUrl: String, onRepositoryButtonClick: (String) -> Unit) {
        repositoryButton.visibility = if (repoUrl.isEmpty()) {
            View.INVISIBLE
        } else {
            repositoryButton.setOnClickListener { onRepositoryButtonClick(repoUrl) }
            repositoryButton.contentDescription =
                itemView.resources.getString(R.string.profile_details_repository_link_button_content_description)
            View.VISIBLE
        }
    }
}

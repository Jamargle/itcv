package com.jmlb0003.itcv.features.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.features.profile.di.REPO_URL_BUTTON_CLICKED
import com.jmlb0003.itcv.features.profile.di.WEBSITE_BUTTON_CLICKED
import javax.inject.Inject
import javax.inject.Named

class ReposAdapter
@Inject constructor(
    @Named(WEBSITE_BUTTON_CLICKED) private val onWebSiteButtonClicked: (String) -> Unit,
    @Named(REPO_URL_BUTTON_CLICKED) private val onRepoUrlButtonClicked: (String) -> Unit,
) : ListAdapter<RepoListItem, RepoViewHolder>(ReposDiffCallback) {

    fun setRepositories(repositories: List<RepoListItem>) {
        submitList(repositories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_repository, parent, false))

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let { repoListItem ->
                bindView(repoListItem, onWebSiteButtonClicked, onRepoUrlButtonClicked)
            }
        }
    }
}

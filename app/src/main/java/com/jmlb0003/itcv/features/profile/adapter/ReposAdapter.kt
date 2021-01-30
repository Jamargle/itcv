package com.jmlb0003.itcv.features.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jmlb0003.itcv.R

class ReposAdapter(
    private val onWebSiteButtonClicked: (String) -> Unit,
    private val onRepoUrlButtonClicked: (String) -> Unit,
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

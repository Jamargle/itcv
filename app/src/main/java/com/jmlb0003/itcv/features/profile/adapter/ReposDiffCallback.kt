package com.jmlb0003.itcv.features.profile.adapter

import androidx.recyclerview.widget.DiffUtil

object ReposDiffCallback : DiffUtil.ItemCallback<RepoListItem>() {
    override fun areItemsTheSame(oldItem: RepoListItem, newItem: RepoListItem) =
        oldItem.name == newItem.name && oldItem.repoUrl == newItem.repoUrl

    override fun areContentsTheSame(oldItem: RepoListItem, newItem: RepoListItem) =
        oldItem == newItem
}

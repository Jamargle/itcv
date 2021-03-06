package com.jmlb0003.itcv.features.profile.adapter

data class RepoListItem(
    val name: String,
    val description: String,
    val website: String,
    val repoUrl: String,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int
)

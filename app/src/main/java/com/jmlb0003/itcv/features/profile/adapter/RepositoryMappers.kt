package com.jmlb0003.itcv.features.profile.adapter

import com.jmlb0003.itcv.domain.model.Repo

object RepositoryMappers {

    fun Repo.toRepositoryListItem() =
        RepoListItem(
            name = name,
            description = description,
            website = website,
            repoUrl = repoUrl,
            starsCount = starsCount,
            watchersCount = watchersCount,
            forksCount = forksCount
        )
}

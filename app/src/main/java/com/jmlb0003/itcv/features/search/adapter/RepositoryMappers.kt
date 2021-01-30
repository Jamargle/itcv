package com.jmlb0003.itcv.features.search.adapter

import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.features.profile.adapter.RepoListItem

object RepositoryMappers {

    fun Repo.toRepositoryListItem() =
        RepoListItem(
            name = name,
            description = description,
            website = website,
            repoUrl = repoUrl
        )
}

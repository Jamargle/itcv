package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import com.jmlb0003.itcv.domain.model.Repo

object ReposMappers {

    fun mapToDomain(repoResponse: RepoResponse) =
        with(repoResponse) {
            Repo(
                name = name,
                description = description ?: "",
                website = websiteUrl ?: "",
                repoUrl = repoUrl ?: ""
            )
        }
}

package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import com.jmlb0003.itcv.domain.model.Repo
import java.util.Date
import com.jmlb0003.itcv.data.model.Repo as DataRepo

object ReposMappers {

    fun mapToDomain(repoResponse: RepoResponse) =
        with(repoResponse) {
            Repo(
                id = id,
                name = name,
                description = description ?: "",
                website = websiteUrl ?: "",
                repoUrl = repoUrl ?: "",
                starsCount = starsCount,
                watchersCount = watchersCount,
                forksCount = forksCount
            )
        }

    fun mapToDomain(dataRepo: DataRepo) =
        with(dataRepo) {
            Repo(
                id = id,
                name = name,
                description = description,
                website = website,
                repoUrl = repoUrl,
                starsCount = starsCount,
                watchersCount = watchersCount,
                forksCount = forksCount
            )
        }

    fun mapToData(domainRepo: Repo, repoOwner: String, lastCacheUpdate: Date) =
        with(domainRepo) {
            DataRepo(
                id = id,
                name = name,
                description = description,
                website = website,
                repoUrl = repoUrl,
                starsCount = starsCount,
                watchersCount = watchersCount,
                forksCount = forksCount,
                owner = repoOwner,
                lastCacheUpdate = lastCacheUpdate.time
            )
        }
}

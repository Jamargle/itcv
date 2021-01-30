package com.jmlb0003.itcv.features.search.adapter

import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.features.search.adapter.RepositoryMappers.toRepositoryListItem
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryMappersTest {

    @Test
    fun `on Repo_toRepositoryListItem converts the domain item to the adapter type`() {
        val expectedName = "Some name"
        val expectedDescription = "Some description"
        val expectedWeb = "Some website"
        val expectedRepoUrl = "Some repositoryUrl"
        val domainRepo = getFakeRepo().copy(
            name = expectedName,
            description = expectedDescription,
            website = expectedWeb,
            repoUrl = expectedRepoUrl,
        )

        with(domainRepo.toRepositoryListItem()) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedWeb, website)
            assertEquals(expectedRepoUrl, repoUrl)
        }
    }

    private fun getFakeRepo() =
        Repo(
            name = "nnn",
            description = "ddd",
            website = "www",
            repoUrl = "rrr"
        )
}

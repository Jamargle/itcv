package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.features.profile.adapter.RepoListItem

sealed class RepositoriesStateList {
    object Loading : RepositoriesStateList()
    class Ready(val repositories: List<RepoListItem>) : RepositoriesStateList()
    object Hidden : RepositoriesStateList()
}

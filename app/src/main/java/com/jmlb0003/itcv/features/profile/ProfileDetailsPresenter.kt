package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.domain.model.ProfileDetails
import com.jmlb0003.itcv.domain.usecases.GetProfileDetailsUseCase
import com.jmlb0003.itcv.features.search.adapter.RepositoryMappers.toRepositoryListItem
import com.jmlb0003.itcv.utils.DateExtensions.toShortDateString

class ProfileDetailsPresenter(
    private val viewState: ProfileDetailsViewState,
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase,
    private val dispatchers: Dispatchers
) : Presenter(dispatchers) {

    fun onViewReady(profileDetailsArguments: ProfileDetailsArgs) {
        getProfileDetailsUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetProfileDetailsUseCase.Input(
                username = profileDetailsArguments.userName
            )
        ) {
            it.either(::handleGetDetailsError) { profileDetails -> handleGetDetailsSuccess(profileDetails) }
        }

        viewState.displayProfileName(profileDetailsArguments.userName)
        viewState.displayMemberSince(profileDetailsArguments.memberSince.toShortDateString())
    }

    private fun handleGetDetailsSuccess(profileDetails: ProfileDetails) {
        // TODO if success display rest of details
        viewState.displayReposInformation(profileDetails.repositories.map { it.toRepositoryListItem() })
    }

    private fun handleGetDetailsError(failure: Failure) {
        // TODO on error display some error messvage
    }
}

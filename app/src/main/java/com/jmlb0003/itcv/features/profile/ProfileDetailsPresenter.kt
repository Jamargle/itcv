package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.utils.DateExtensions.toShortDateString

class ProfileDetailsPresenter(
    private val viewState: ProfileDetailsViewState,
    dispatchers: Dispatchers
) : Presenter(dispatchers) {

    fun onViewReady(profileDetailsArguments: ProfileDetailsArgs) {
        viewState.displayProfileName(profileDetailsArguments.userName)

        // TODO display rest of details

        viewState.displayMemberSince(profileDetailsArguments.memberSince.toShortDateString())
    }
}

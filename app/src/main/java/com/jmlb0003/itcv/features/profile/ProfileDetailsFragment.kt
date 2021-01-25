package com.jmlb0003.itcv.features.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.MainToolbarController
import kotlinx.android.synthetic.main.fragment_profile_details.*

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    // region view state observers
    private val onUserNameChange = Observer<ProfileDetailsStateList> {
        handleUserNameChange(it)
    }
    private val onMemberSinceChange = Observer<ProfileDetailsStateList> {
        handleMemberSinceChange(it)
    }
    // endregion

    private val viewModel by viewModels<ProfileDetailsViewModel> { getProfileDetailsViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewStateObservers()

        viewModel.presenter.onViewReady(getProfileDetailsArguments())
    }

    private fun initToolbar() {
        activityViewModels<MainToolbarController>().value.setNewTitle(
            getString(
                R.string.profile_details_title_profile_details_with_username,
                getProfileDetailsArguments().userName
            )
        )
    }

    private fun initViewStateObservers() {
        with(viewModel.nonNullViewState) {
            profileNameState.observe(viewLifecycleOwner, onUserNameChange)
            memberSinceState.observe(viewLifecycleOwner, onMemberSinceChange)
        }
    }

    private fun getProfileDetailsArguments() = arguments?.getParcelable<ProfileDetailsArgs>(ARG_PROFILE_DETAILS)
        ?: throw IllegalStateException("There needs to be a ProfileDetailsArgs input")

    private fun handleUserNameChange(state: ProfileDetailsStateList) {
        user_name?.updateLabelViewState(state)
    }

    private fun handleMemberSinceChange(state: ProfileDetailsStateList) {
        member_since?.updateLabelViewState(state)
    }

    private fun TextView.updateLabelViewState(state: ProfileDetailsStateList) {
        when (state) {
            is ProfileDetailsStateList.Ready -> {
                visibility = View.VISIBLE
                text = state.value
            }
            ProfileDetailsStateList.Hidden -> visibility = View.GONE
        }
    }

    companion object {
        private const val ARG_PROFILE_DETAILS = "Args:ProfileDetailsArgs"

        fun getProfileDetailsBundle(baseProfile: User) = bundleOf(
            ARG_PROFILE_DETAILS to ProfileDetailsArgs(
                userName = baseProfile.username,
                memberSince = baseProfile.memberSince
            )
        )
    }
}

package com.jmlb0003.itcv.features.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.MainToolbarController
import com.jmlb0003.itcv.features.profile.adapter.ReposAdapter
import com.jmlb0003.itcv.utils.showErrorPopup
import kotlinx.android.synthetic.main.fragment_profile_details.*

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    // region view state observers
    private val onUserNameChange = Observer<ProfileDetailsStateList> { handleUserNameChange(it) }
    private val onMemberSinceChange = Observer<ProfileDetailsStateList> { handleMemberSinceChange(it) }
    private val onRepositoriesChange = Observer<RepositoriesStateList> { handleReposChange(it) }
    private val onUserBioChange = Observer<ProfileDetailsStateList> { handleUserBioChange(it) }
    private val onEmailChange = Observer<ProfileDetailsStateList> { handleEmailChange(it) }
    private val onLocationChange = Observer<ProfileDetailsStateList> { handleLocationChange(it) }
    private val onFollowerCountChange = Observer<ProfileDetailsStateList> { handleFollowerCountChange(it) }
    private val onUserWebsiteChange = Observer<ProfileDetailsStateList> { handleWebsiteChange(it) }
    private val onTwitterAccountChange = Observer<ProfileDetailsStateList> { handleTwitterAccountChange(it) }
    private val onErrorTrigger = Observer<ProfileDetailsStateList> { displayError(it) }
    // endregion

    private val viewModel by viewModels<ProfileDetailsViewModel> { getProfileDetailsViewModelFactory() }
    private var reposAdapter: ReposAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewListeners(view)
        initRepositoryListView(view)
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

    private fun initViewListeners(rootView: View) {
        rootView.findViewById<View>(R.id.user_web)?.setOnClickListener {
            viewModel.presenter.onUserWebsiteClicked(getUserWebsite())
        }
    }

    private fun getUserWebsite() =
        when (val website = viewModel.nonNullViewState.userWebsiteState.value) {
            is ProfileDetailsStateList.Ready -> website.value
            else -> null
        }

    private fun initRepositoryListView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.repository_list)?.let { recyclerView ->
            recyclerView.adapter = ReposAdapter(
                onWebSiteButtonClicked = { viewModel.presenter.onRepoWebsiteClicked(it) },
                onRepoUrlButtonClicked = { viewModel.presenter.onRepoGithubUrlClicked(it) }
            ).also { reposAdapter = it }
        }
    }

    private fun initViewStateObservers() {
        with(viewModel.nonNullViewState) {
            profileRepositories.observe(viewLifecycleOwner, onRepositoriesChange)
            profileNameState.observe(viewLifecycleOwner, onUserNameChange)
            profileBioState.observe(viewLifecycleOwner, onUserBioChange)
            memberSinceState.observe(viewLifecycleOwner, onMemberSinceChange)
            emailState.observe(viewLifecycleOwner, onEmailChange)
            locationState.observe(viewLifecycleOwner, onLocationChange)
            followerCountState.observe(viewLifecycleOwner, onFollowerCountChange)
            userWebsiteState.observe(viewLifecycleOwner, onUserWebsiteChange)
            twitterAccountState.observe(viewLifecycleOwner, onTwitterAccountChange)
            errorState.observe(viewLifecycleOwner, onErrorTrigger)
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

    private fun handleUserBioChange(state: ProfileDetailsStateList) {
        user_bio?.updateLabelViewState(state)
    }

    private fun handleEmailChange(state: ProfileDetailsStateList) {
        user_email?.updateLabelViewState(state)
    }

    private fun handleLocationChange(state: ProfileDetailsStateList) {
        user_location?.updateLabelViewState(state) { R.string.profile_details_location }
    }

    private fun handleFollowerCountChange(state: ProfileDetailsStateList) {
        followers_count?.updateLabelViewState(state) { R.string.profile_details_followers_count }
    }

    private fun handleWebsiteChange(state: ProfileDetailsStateList) {
        user_web?.updateLabelViewState(state)
    }

    private fun handleTwitterAccountChange(state: ProfileDetailsStateList) {
        twitter_username?.updateLabelViewState(state)
    }

    private fun TextView.updateLabelViewState(
        state: ProfileDetailsStateList,
        onLabelReady: (() -> Int)? = null
    ) {
        when (state) {
            is ProfileDetailsStateList.Ready -> {
                visibility = View.VISIBLE
                text = onLabelReady?.invoke()?.let { getString(it, state.value) } ?: state.value
            }
            else -> visibility = View.GONE
        }
    }

    private fun displayError(state: ProfileDetailsStateList?) {
        when (state) {
            is ProfileDetailsStateList.Error -> activity?.showErrorPopup(errorMessage = getErrorMessage(state.errorMessage))
            else -> {
                // NO-OP
            }
        }
    }

    private fun getErrorMessage(errorMessage: String?) =
        if (errorMessage.isNullOrBlank()) {
            getString(R.string.error_dialog_generic_message)
        } else {
            errorMessage
        }

    private fun handleReposChange(state: RepositoriesStateList) {
        when (state) {
            RepositoriesStateList.Loading -> displayRepositoriesLoading()
            is RepositoriesStateList.Ready -> reposAdapter?.setRepositories(state.repositories)
            RepositoriesStateList.Hidden -> hideRepositoriesView()
        }
        if (state != RepositoriesStateList.Loading) {
            hideRepositoriesLoadingView()
        }
    }

    private fun displayRepositoriesLoading() {
        loading_repository_list_view?.visibility = View.VISIBLE
    }

    private fun hideRepositoriesLoadingView() {
        loading_repository_list_view?.visibility = View.GONE
    }

    private fun hideRepositoriesView() {
        repository_list?.visibility = View.GONE
    }

    companion object {
        private const val ARG_PROFILE_DETAILS = "Args:ProfileDetailsArgs"

        fun getProfileDetailsBundle(baseProfile: User) = bundleOf(
            ARG_PROFILE_DETAILS to ProfileDetailsArgs(user = baseProfile)
        )

        fun getProfileDetailsBundle(profileName: String) = bundleOf(
            ARG_PROFILE_DETAILS to ProfileDetailsArgs(userName = profileName)
        )
    }
}

package com.jmlb0003.itcv.features.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.MainToolbarController
import com.jmlb0003.itcv.features.profile.adapter.RepoListItem
import com.jmlb0003.itcv.features.profile.adapter.ReposAdapter
import kotlinx.android.synthetic.main.fragment_profile_details.*

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    // region view state observers
    private val onUserNameChange = Observer<ProfileDetailsStateList> { handleUserNameChange(it) }
    private val onMemberSinceChange = Observer<ProfileDetailsStateList> { handleMemberSinceChange(it) }
    private val onRepositoriesChange = Observer<List<RepoListItem>> { handleReposChange(it) }
    // endregion

    private val viewModel by viewModels<ProfileDetailsViewModel> { getProfileDetailsViewModelFactory() }
    private var reposAdapter: ReposAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
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

    private fun initRepositoryListView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.repository_list)?.let { recyclerView ->
            recyclerView.adapter = ReposAdapter(
                onWebSiteButtonClicked = { openUrl(it) },
                onRepoUrlButtonClicked = { openUrl(it) }
            ).also { reposAdapter = it }
        }
    }

    private fun openUrl(it: String) {
        // TODO open url
        Toast.makeText(requireContext(), "TODO: Open url: $it", Toast.LENGTH_SHORT).show()
    }

    private fun initViewStateObservers() {
        with(viewModel.nonNullViewState) {
            profileNameState.observe(viewLifecycleOwner, onUserNameChange)
            memberSinceState.observe(viewLifecycleOwner, onMemberSinceChange)
            profileRepositories.observe(viewLifecycleOwner, onRepositoriesChange)
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

    private fun handleReposChange(repositories: List<RepoListItem>) {
        reposAdapter?.setRepositories(repositories)
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

package com.jmlb0003.itcv.features.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jmlb0003.itcv.CustomApplication
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.MainToolbarController
import com.jmlb0003.itcv.features.profile.adapter.ReposAdapter
import com.jmlb0003.itcv.features.profile.adapter.TopicListItem
import com.jmlb0003.itcv.utils.showErrorPopup
import javax.inject.Inject

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    @Inject
    lateinit var viewModel: ProfileDetailsViewModel

    @Inject
    lateinit var reposAdapter: ReposAdapter

    // region view state observers
    private val onUserNameChange = Observer<ProfileDetailsStateList> { handleUserNameChange(it) }
    private val onMemberSinceChange = Observer<ProfileDetailsStateList> { handleMemberSinceChange(it) }
    private val onTopicsChange = Observer<TopicsStateList> { handleTopicsChange(it) }
    private val onRepositoriesChange = Observer<RepositoriesStateList> { handleReposChange(it) }
    private val onUserBioChange = Observer<ProfileDetailsStateList> { handleUserBioChange(it) }
    private val onEmailChange = Observer<ProfileDetailsStateList> { handleEmailChange(it) }
    private val onLocationChange = Observer<ProfileDetailsStateList> { handleLocationChange(it) }
    private val onFollowerCountChange = Observer<ProfileDetailsStateList> { handleFollowerCountChange(it) }
    private val onUserWebsiteChange = Observer<ProfileDetailsStateList> { handleWebsiteChange(it) }
    private val onTwitterAccountChange = Observer<ProfileDetailsStateList> { handleTwitterAccountChange(it) }
    private val onErrorTrigger = Observer<ProfileDetailsStateList> { displayError(it) }
    // endregion

    // region view fields
    private var userName: TextView? = null
    private var memberSince: TextView? = null
    private var userBio: TextView? = null
    private var userEmail: TextView? = null
    private var userLocation: TextView? = null
    private var userWeb: TextView? = null
    private var followersCount: TextView? = null
    private var twitterUserName: TextView? = null
    private var loadingTopicsView: ProgressBar? = null
    private var topicList: ChipGroup? = null
    private var loadingView: ProgressBar? = null
    private var repositoryList: RecyclerView? = null
    // endregion

    override fun onAttach(context: Context) {
        initProfileDetailsComponent(context)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews(view)
        initRepositoryListView(view)
        initViewStateObservers()

        viewModel.presenter.onViewReady(getProfileDetailsArguments())
    }

    override fun onDestroyView() {
        userName = null
        memberSince = null
        userBio = null
        userEmail = null
        userLocation = null
        userWeb = null
        followersCount = null
        twitterUserName = null
        loadingTopicsView = null
        topicList = null
        loadingView = null
        repositoryList = null
        super.onDestroyView()
    }

    private fun initToolbar() {
        activityViewModels<MainToolbarController>().value.setNewTitle(
            getString(
                R.string.profile_details_title_profile_details_with_username,
                getProfileDetailsArguments().userName
            )
        )
    }

    private fun initViews(rootView: View) {
        userName = rootView.findViewById(R.id.user_name)
        memberSince = rootView.findViewById(R.id.member_since)
        userBio = rootView.findViewById(R.id.user_bio)
        userEmail = rootView.findViewById(R.id.user_email)
        userLocation = rootView.findViewById(R.id.user_location)
        userWeb = rootView.findViewById(R.id.user_web)
        followersCount = rootView.findViewById(R.id.followers_count)
        twitterUserName = rootView.findViewById(R.id.twitter_username)
        loadingTopicsView = rootView.findViewById(R.id.loading_topics_list_view)
        topicList = rootView.findViewById(R.id.topics_list)
        loadingView = rootView.findViewById(R.id.loading_repository_list_view)
        repositoryList = rootView.findViewById(R.id.repository_list)

        initViewListeners()
    }

    private fun initViewListeners() {
        userWeb?.setOnClickListener {
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
            recyclerView.adapter = reposAdapter
        }
    }

    private fun initViewStateObservers() {
        with(viewModel.nonNullViewState) {
            profileTopics.observe(viewLifecycleOwner, onTopicsChange)
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
        userName.updateLabelViewState(state)
    }

    private fun handleMemberSinceChange(state: ProfileDetailsStateList) {
        memberSince.updateLabelViewState(state)
    }

    private fun handleUserBioChange(state: ProfileDetailsStateList) {
        userBio.updateLabelViewState(state)
    }

    private fun handleEmailChange(state: ProfileDetailsStateList) {
        userEmail.updateLabelViewState(state)
    }

    private fun handleLocationChange(state: ProfileDetailsStateList) {
        userLocation.updateLabelViewState(state) { R.string.profile_details_location }
    }

    private fun handleFollowerCountChange(state: ProfileDetailsStateList) {
        followersCount.updateLabelViewState(state) { R.string.profile_details_followers_count }
    }

    private fun handleWebsiteChange(state: ProfileDetailsStateList) {
        userWeb.updateLabelViewState(state)
    }

    private fun handleTwitterAccountChange(state: ProfileDetailsStateList) {
        twitterUserName.updateLabelViewState(state)
    }

    private fun TextView?.updateLabelViewState(
        state: ProfileDetailsStateList,
        onLabelReady: (() -> Int)? = null
    ) {
        if (this == null) {
            return
        }
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
            is RepositoriesStateList.Ready -> reposAdapter.setRepositories(state.repositories)
            RepositoriesStateList.Hidden -> hideRepositoriesView()
        }
        if (state != RepositoriesStateList.Loading) {
            hideRepositoriesLoadingView()
        }
    }

    private fun displayRepositoriesLoading() {
        loadingView?.visibility = View.VISIBLE
    }

    private fun hideRepositoriesLoadingView() {
        loadingView?.visibility = View.GONE
    }

    private fun hideRepositoriesView() {
        repositoryList?.visibility = View.GONE
    }

    private fun handleTopicsChange(state: TopicsStateList) {
        when (state) {
            TopicsStateList.Loading -> displayTopicsLoading()
            is TopicsStateList.Ready -> displayTopics(state.topics)
            TopicsStateList.Hidden -> hideTopicsView()
        }
        if (state != TopicsStateList.Loading) {
            hideTopicsLoadingView()
        }
    }

    private fun displayTopics(topics: List<TopicListItem>) {
        topicList?.apply {
            visibility = View.VISIBLE

            if (childCount > 0) {
                removeAllViews()
            }
            addTopics(topics)
        }
    }

    private fun ChipGroup.addTopics(topics: List<TopicListItem>) {
        topics.forEach { topic ->
            val chip = Chip(requireContext())
            chip.text = topic.name

            addView(chip)
        }
    }

    private fun displayTopicsLoading() {
        loadingTopicsView?.visibility = View.VISIBLE
    }

    private fun hideTopicsLoadingView() {
        loadingTopicsView?.visibility = View.GONE
    }

    private fun hideTopicsView() {
        topicList?.visibility = View.GONE
    }

    private fun initProfileDetailsComponent(context: Context) {
        (context.applicationContext as CustomApplication)
            .appComponent
            .profileDetailsComponentFactory.create(
                this,
                onWebSiteButtonClicked = { viewModel.presenter.onRepoWebsiteClicked(it) },
                onRepoUrlButtonClicked = { viewModel.presenter.onRepoGithubUrlClicked(it) }
            ).also {
                it.inject(this)
            }
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

package com.jmlb0003.itcv.features.home

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentOnAttachListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.features.input.InsertUserDialog
import com.jmlb0003.itcv.utils.showErrorPopup

class HomeFragment : Fragment(R.layout.fragment_home) {

    // region view state observers
    private val onLoadingStateChange = Observer<Boolean> { isToBeShown ->
        view?.findViewById<ProgressBar>(R.id.loading_view)?.let {
            if (isToBeShown) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
    }
    private val onUserNameChange = Observer<HomeViewStateList> { handleUserNameChange(it) }
    private val onUserBioChange = Observer<HomeViewStateList> { handleUserBioChange(it) }
    private val onEmailChange = Observer<HomeViewStateList> { handleEmailChange(it) }
    private val onLocationChange = Observer<HomeViewStateList> { handleLocationChange(it) }
    private val onRepositoryCountChange = Observer<HomeViewStateList> { handleRepositoryCountChange(it) }
    private val onFollowerCountChange = Observer<HomeViewStateList> { handleFollowerCountChange(it) }
    private val onUserWebsiteChange = Observer<HomeViewStateList> { handleWebsiteChange(it) }
    private val onTwitterAccountChange = Observer<HomeViewStateList> { handleTwitterAccountChange(it) }
    private val onErrorTrigger = Observer<HomeViewErrorState> { handleErrorState(it) }
    // endregion

    // region view fields
    private var userName: TextView? = null
    private var userBio: TextView? = null
    private var userEmail: TextView? = null
    private var userLocation: TextView? = null
    private var userWeb: TextView? = null
    private var repositoryCount: TextView? = null
    private var followersCount: TextView? = null
    private var twitterUserName: TextView? = null
    // endregion

    private val viewModel by viewModels<HomeViewModel> { getHomeViewModelFactory(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        initViewStateObservers()
    }

    override fun onDestroyView() {
        userName = null
        userBio = null
        userEmail = null
        userLocation = null
        userWeb = null
        repositoryCount = null
        followersCount = null
        twitterUserName = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        parentFragmentManager.removeFragmentOnAttachListener(insertUserDialogListener)
        super.onDestroy()
    }

    private fun initViews(rootView: View) {
        with(rootView) {
            userName = findViewById(R.id.user_name)
            userBio = findViewById(R.id.user_bio)
            userEmail = findViewById(R.id.user_email)
            userLocation = findViewById(R.id.user_location)
            userWeb = findViewById(R.id.user_web)
            repositoryCount = findViewById(R.id.public_repos_count)
            followersCount = findViewById(R.id.followers_count)
            twitterUserName = findViewById(R.id.twitter_username)

            initViewClickListeners(this)
        }
    }

    private fun initViewClickListeners(rootView: View) {
        userWeb?.setOnClickListener {
            viewModel.presenter.onUserWebsiteClicked()
        }
        rootView.findViewById<FloatingActionButton>(R.id.home_fab)?.let {
            it.setOnClickListener {
                findNavController().navigate(R.id.navigation_home_to_search_by_fab)
            }
        }
        rootView.findViewById<MaterialButton>(R.id.see_all_details_button)?.let {
            it.setOnClickListener { viewModel.presenter.onSeeAllClicked() }
        }
    }

    // region LiveData stuff to get the update of the default username from the dialog
    private val observableUsername by lazy { MutableLiveData<String>() }
    private val insertUserDialogListener by lazy {
        FragmentOnAttachListener { _, fragment ->
            if (fragment is InsertUserDialog) {
                fragment.setUsernameChangeTrigger(observableUsername)
            }
        }
    }

    private fun setupUsernameChangeListener() {
        parentFragmentManager.addFragmentOnAttachListener(insertUserDialogListener)
        observableUsername.observe(viewLifecycleOwner, { viewModel.presenter.onDefaultUsernameChange() })
    }
    // endregion

    private fun initViewStateObservers() {
        with(viewModel.nonNullViewState) {
            loadingState.observe(viewLifecycleOwner, onLoadingStateChange)
            profileNameState.observe(viewLifecycleOwner, onUserNameChange)
            profileBioState.observe(viewLifecycleOwner, onUserBioChange)
            emailState.observe(viewLifecycleOwner, onEmailChange)
            locationState.observe(viewLifecycleOwner, onLocationChange)
            repositoryCountState.observe(viewLifecycleOwner, onRepositoryCountChange)
            followerCountState.observe(viewLifecycleOwner, onFollowerCountChange)
            userWebsiteState.observe(viewLifecycleOwner, onUserWebsiteChange)
            twitterAccountState.observe(viewLifecycleOwner, onTwitterAccountChange)
            errorState.observe(viewLifecycleOwner, onErrorTrigger)
        }
    }

    // region view observers handlers
    private fun handleErrorState(newErrorState: HomeViewErrorState) {
        when (newErrorState) {
            is HomeViewErrorState.ErrorMessage -> displayErrorScreen(newErrorState.errorMessage)
            is HomeViewErrorState.ErrorStringRes -> displayErrorScreen(getString(newErrorState.errorStringRes))
            HomeViewErrorState.ErrorMissingDefaultUser -> {
                if (parentFragmentManager.fragments.find { it is InsertUserDialog } != null) {
                    // TODO Fix error here when rotating the screen:
                    //  When no username, the dialog to insert one is displayed
                    //  Rotate device once it is displayed
                    //  it crashes here
                    return
                }
                setupUsernameChangeListener()
                findNavController().navigate(R.id.action_navigation_home_to_enter_user_dialog)
            }
        }
    }

    private fun displayErrorScreen(errorMessage: String) {
        activity?.showErrorPopup(
            errorMessage = errorMessage,
            onNegativeButtonClicked = { /* NO-OP */ }
        )
    }

    private fun handleUserNameChange(state: HomeViewStateList) {
        userName.updateLabelViewState(state)
    }

    private fun handleUserBioChange(state: HomeViewStateList) {
        userBio.updateLabelViewState(state)
    }

    private fun handleEmailChange(state: HomeViewStateList) {
        userEmail.updateLabelViewState(state)
    }

    private fun handleLocationChange(state: HomeViewStateList) {
        userLocation.updateLabelViewState(state) { R.string.profile_details_location }
    }

    private fun handleRepositoryCountChange(state: HomeViewStateList) {
        repositoryCount.updateLabelViewState(state) { R.string.home_repository_count_label }
    }

    private fun handleFollowerCountChange(state: HomeViewStateList) {
        followersCount.updateLabelViewState(state) { R.string.profile_details_followers_count }
    }

    private fun handleWebsiteChange(state: HomeViewStateList) {
        userWeb.updateLabelViewState(state)
    }

    private fun handleTwitterAccountChange(state: HomeViewStateList) {
        twitterUserName.updateLabelViewState(state)
    }

    private fun TextView?.updateLabelViewState(
        state: HomeViewStateList,
        onLabelReady: (() -> Int)? = null
    ) {
        if (this == null) {
            return
        }
        when (state) {
            is HomeViewStateList.Ready -> {
                visibility = View.VISIBLE
                text = onLabelReady?.invoke()?.let { getString(it, state.label) } ?: state.label
            }
            else -> visibility = View.GONE
        }
    }
    // endregion
}

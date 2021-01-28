package com.jmlb0003.itcv.features.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.livedata.ConsumingObserver
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.MainToolbarController
import com.jmlb0003.itcv.features.profile.ProfileDetailsFragment
import com.jmlb0003.itcv.utils.showErrorPopup

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val onNavigateToProfileDetailsRequest = ConsumingObserver<User> { goToProfileDetails(it) }

    private val navigationTriggers by activityViewModels<NavigationTriggers>()

    private val onViewStateChange = Observer<HomeViewStateList> { handleViewChange(it) }

    private val viewModel by viewModels<HomeViewModel> { getHomeViewModelFactory(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        initViewObservers()
        initNavigationObservers()
    }

    private fun initViews(rootView: View) {
        rootView.findViewById<FloatingActionButton>(R.id.home_fab)?.let {
            it.setOnClickListener {
                findNavController().navigate(R.id.navigation_home_to_search_by_fab)
            }
        }
        rootView.findViewById<MaterialButton>(R.id.see_all_details_button)?.let {
            it.setOnClickListener { viewModel.presenter.onSeeAllClicked() }
        }
    }

    private fun initNavigationObservers() {
        navigationTriggers.getGoToProfileDetailsTrigger().observe(viewLifecycleOwner, onNavigateToProfileDetailsRequest)
    }

    private fun initViewObservers() {
        with(viewModel.nonNullViewState) {
            viewState.observe(viewLifecycleOwner, onViewStateChange)
        }
    }

    private fun handleViewChange(newState: HomeViewStateList) {
        when (newState) {
            is HomeViewStateList.Ready -> displayProfileInfo(newState.user)
            is HomeViewStateList.Error -> displayErrorScreen(newState.errorStringRes)
        }
    }

    private fun displayProfileInfo(user: User) {
        with(user) {
            activityViewModels<MainToolbarController>().value.setNewTitle(username)

            view?.findViewById<TextView>(R.id.user_name)?.text = name
            view?.findViewById<TextView>(R.id.user_location)?.text = location
//            view?.findViewById<TextView>(R.id.user_bio)?.text = user_bio
            view?.findViewById<TextView>(R.id.user_email)?.text = email
//            view?.findViewById<TextView>(R.id.user_web)?.text = user_web
//            view?.findViewById<TextView>(R.id.twitter_username)?.text = twitter_username
            view?.findViewById<TextView>(R.id.public_repos_count)?.text =
                getString(R.string.home_repository_count_label, repositoryCount)
//            view?.findViewById<TextView>(R.id.followers_count)?.text = followers_count
//            view?.findViewById<TextView>(R.id.member_since)?.text = member_since

            // TODO add missing information
        }
    }

    private fun displayErrorScreen(errorStringRes: Int) {
        activity?.showErrorPopup(
            errorMessage = getErrorMessage(errorStringRes),
            onNegativeButtonClicked = { /* NO-OP */ }
        )
    }

    private fun getErrorMessage(error: Int) = getString(error)

    private fun goToProfileDetails(user: User) {
        findNavController().navigate(
            R.id.navigation_profile_to_details,
            ProfileDetailsFragment.getProfileDetailsBundle(user)
        )
    }
}

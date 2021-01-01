package com.jmlb0003.itcv.features.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.livedata.ConsumingObserver
import com.jmlb0003.itcv.features.settings.adapter.SettingsAdapter
import com.jmlb0003.itcv.features.settings.adapter.SettingsItem

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    // region navigation
    private val onNavigateToAboutTriggered = ConsumingObserver<Unit> {
        navigateWithAction(R.id.navigation_settings_to_about)
    }

    private val settingsNavigationTriggers by viewModels<SettingsNavigationTriggers>()
    // endregion

    private val onViewChange = Observer<List<SettingsItem>> {
        handleViewChange(it)
    }

    private val settingsViewModel by viewModels<SettingsViewModel> { getSettingsViewModelFactory(this) }

    private var settingsAdapter: SettingsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSettingsItemList(view)
        initViewObservers()
        initNavigationTriggers()
        settingsViewModel.presenter.onViewPrepared()
    }

    private fun initSettingsItemList(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.settings_list_view)?.let {
            it.setHasFixedSize(true)
            settingsAdapter = SettingsAdapter(
                onSettingsItemClicked = { item ->
                    settingsViewModel.presenter.onSettingsItemClicked(item)
                }
            )
            it.adapter = settingsAdapter

            it.addItemDecoration(
                DividerItemDecoration(it.context, (it.layoutManager as LinearLayoutManager).orientation)
            )
        }
    }

    private fun initViewObservers() {
        with(settingsViewModel.nonNullViewState) {
            settingsViewState.observe(viewLifecycleOwner, onViewChange)
        }
    }

    private fun initNavigationTriggers() {
        settingsNavigationTriggers.run {
            getAboutScreenNavigationTrigger().observe(viewLifecycleOwner, onNavigateToAboutTriggered)
        }
    }

    private fun handleViewChange(items: List<SettingsItem>) {
        settingsAdapter?.setSettingsItems(items)
    }

    private fun navigateWithAction(navigationActionId: Int) {
        view?.findNavController()?.navigate(navigationActionId)
    }
}

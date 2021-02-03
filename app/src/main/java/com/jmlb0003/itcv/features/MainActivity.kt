package com.jmlb0003.itcv.features

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.livedata.ConsumingObserver
import com.jmlb0003.itcv.core.tryToOpenUrl
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.features.home.NavigationTriggers
import com.jmlb0003.itcv.features.profile.ProfileDetailsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarConfiguration: AppBarConfiguration

    private val toolbarController by viewModels<MainToolbarController>()

    private val onNavigateToProfileDetailsRequest = ConsumingObserver<User> { goToProfileDetails(it) }
    private val onNavigateToProfileDetailsByUsernameRequest = ConsumingObserver<String> {
        goToProfileDetailsByUsername(it)
    }
    private val onOpenUrlRequest = ConsumingObserver<String> { openUrl(it) }
    private val navigationTriggers by viewModels<NavigationTriggers>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        setupNavigation()
        initNavigationTriggerObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(toolbarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initToolbar() {
        findViewById<Toolbar>(R.id.main_toolbar)?.let { setSupportActionBar(it) }
        toolbarController.getTitleChangeTrigger().observe(this, Observer { supportActionBar?.title = it })
        toolbarController.getTitleResChangeTrigger().observe(this, Observer { supportActionBar?.setTitle(it) })
    }

    private fun setupNavigation() {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController?.let { navigationController ->
            setupToolbarNavigation(navigationController)
            setupBottomBarNavigation(navigationController)
        }
    }

    private fun setupToolbarNavigation(navigationController: NavController) {
        toolbarConfiguration = AppBarConfiguration(navigationController.graph)
        setupActionBarWithNavController(navigationController, toolbarConfiguration)
    }

    private fun setupBottomBarNavigation(navigationController: NavController) {
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.let { bottomNavigationView ->
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_favorites,
                    R.id.navigation_settings
                )
            )
            NavigationUI.setupActionBarWithNavController(this, navigationController, appBarConfiguration)
            NavigationUI.setupWithNavController(bottomNavigationView, navigationController)
        }
    }

    private fun initNavigationTriggerObservers() {
        navigationTriggers.run {
            getOpenUrlTrigger().observe(this@MainActivity, onOpenUrlRequest)
            getGoToProfileDetailsTrigger().observe(this@MainActivity, onNavigateToProfileDetailsRequest)
            getGoToProfileDetailsByUsernameTrigger()
                .observe(this@MainActivity, onNavigateToProfileDetailsByUsernameRequest)
        }
    }

    private fun goToProfileDetails(user: User) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.navigation_profile_to_details,
            ProfileDetailsFragment.getProfileDetailsBundle(user)
        )
    }

    private fun goToProfileDetailsByUsername(username: String) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.navigation_search_to_details,
            ProfileDetailsFragment.getProfileDetailsBundle(
                profileName = username
            )
        )
    }

    private fun openUrl(url: String) {
        tryToOpenUrl(url)
    }
}

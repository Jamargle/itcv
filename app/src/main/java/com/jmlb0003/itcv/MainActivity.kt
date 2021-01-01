package com.jmlb0003.itcv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(toolbarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initToolbar() {
        findViewById<Toolbar>(R.id.main_toolbar)?.let { setSupportActionBar(it) }
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
}
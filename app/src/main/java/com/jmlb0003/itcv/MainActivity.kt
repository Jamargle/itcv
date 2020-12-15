package com.jmlb0003.itcv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


//    poner CI lo primero de todo


//    https://coletiv.com/blog/android-github-actions-setup/


//    https://developer.github.com/v3/
//
//
//    https://skydoves.medium.com/android-mvvm-architecture-components-using-github-api-f0ab9c2a67a0
//
//    https://youtu.be/TnQUb-ACqWs
//
//
//
    Pantalla inicial:
        - Pedir el usuario propio/inicial (en el futuro puede ser hacer login o algo)
            -> navegar a la pantalla principal

    * Pantalla principal:
        - detalles del perfil seleccionado
        - lupa para buscar otro perfil
            -> buscador que pueda muestrar detalles de perfiles y marcarlos como favoritos

    * Pantalla favoritos:
        - lista de perfiles favoritos (pantalla con repositorios favoritos quizas...pero eso fuera de MVP)
            -> navegar a detalles de perfil cuando se seleccione uno

    * Settings
    * Privacidad...



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.let { bottomNavigationView ->
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController?.let { navigationController ->
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
}
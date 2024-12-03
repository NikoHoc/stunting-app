package com.dicoding.stunting.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityMainBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationActivity
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val authenticationViewModel  by viewModels<AuthenticationViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_history, R.id.navigation_profile
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id
            val navOptions = when (currentDestination to item.itemId) {
                // Home to History and Profile: Slide kanan
                R.id.navigation_home to R.id.navigation_history,
                R.id.navigation_home to R.id.navigation_profile -> androidx.navigation.navOptions {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }

                }

                // History to Home: Slide kiri
                R.id.navigation_history to R.id.navigation_home -> androidx.navigation.navOptions {
                    anim {
                        enter = R.anim.slide_in_left
                        exit = R.anim.slide_out_right
                        popEnter = R.anim.slide_in_right
                        popExit = R.anim.slide_out_left
                    }
                    popUpTo(R.id.navigation_history) { inclusive = true }
                }
                // History to Profile: Slide kanan
                R.id.navigation_history to R.id.navigation_profile -> androidx.navigation.navOptions {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }
                    popUpTo(R.id.navigation_history) { inclusive = true }
                }
                // Profile to History and home: Slide kiri
                R.id.navigation_profile to R.id.navigation_history,
                R.id.navigation_profile to R.id.navigation_home -> androidx.navigation.navOptions {
                    anim {
                        enter = R.anim.slide_in_left
                        exit = R.anim.slide_out_right
                        popEnter = R.anim.slide_in_right
                        popExit = R.anim.slide_out_left
                    }
                    popUpTo(R.id.navigation_profile) { inclusive = true }
                }
                else -> null
            }

            navOptions?.let {
                navController.navigate(item.itemId, null, it)
                true
            } ?: false
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newsFragment -> binding.navView.visibility = View.GONE
                else -> binding.navView.visibility = View.VISIBLE
            }
        }

        authenticationViewModel.getSession().observe(this@MainActivity) { userModel ->
            if (!userModel.isLogin) {
                startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                finish()
                return@observe
            }
        }
    }
}
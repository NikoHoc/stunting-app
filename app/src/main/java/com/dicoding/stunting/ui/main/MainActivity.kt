package com.dicoding.stunting.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.pref.NotificationPreference
import com.dicoding.stunting.databinding.ActivityMainBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationActivity
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.worker.NotificationWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var notificationPreference: NotificationPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        notificationPreference = NotificationPreference(this)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_history, R.id.navigation_profile)
        )

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

        authenticationViewModel.getSession().observe(this@MainActivity) { userModel ->
            if (!userModel.isLogin) {
                startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                finish()
            }
        }
    }

    fun setupDailyNotification(isEnabled: Boolean) {
        val workManager = WorkManager.getInstance(this)

        if (isEnabled) {
            val monthlyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                30,
                TimeUnit.DAYS
            )
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "monthly_notification_work",
                ExistingPeriodicWorkPolicy.REPLACE,
                monthlyWorkRequest
            )
        } else {
            workManager.cancelUniqueWork("monthly_notification_work")
        }
    }

    private fun calculateInitialDelay(): Long {
        val now = Calendar.getInstance()
        val nextMidnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        return nextMidnight.timeInMillis - now.timeInMillis
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            } else {
                notificationPreference.setNotificationEnabled(true)
                setupDailyNotification(true)
            }
        } else {
            notificationPreference.setNotificationEnabled(true)
            setupDailyNotification(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationPreference.setNotificationEnabled(true)
                setupDailyNotification(true)
            } else {
                findViewById<Switch>(R.id.switchToggle).isChecked = false
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    }
}

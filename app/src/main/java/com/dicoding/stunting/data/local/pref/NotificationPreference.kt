package com.dicoding.stunting.data.local.pref

import android.content.Context
import android.content.SharedPreferences

class NotificationPreference(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    fun isNotificationEnabled(): Boolean {
        return preferences.getBoolean("notification_enabled", false)
    }

    fun setNotificationEnabled(isEnabled: Boolean) {
        preferences.edit().putBoolean("notification_enabled", isEnabled).apply()
    }
}
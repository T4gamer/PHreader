package com.asas.phapp

import android.content.Context

class SharedPrefs(context: Context) {
    private val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    fun saveTitle(title: String) {
        with(sharedPref.edit()) {
            putString("widget_title", title)
            apply()
        }
    }

    fun getTitle(): String? {
        return sharedPref.getString("widget_title", "")
    }
}
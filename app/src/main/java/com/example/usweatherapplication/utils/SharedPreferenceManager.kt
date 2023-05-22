package com.example.usweatherapplication.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Shared Preference Helper Class
 */
class SharedPreferencesManager private constructor(private val context: Context) {

    companion object {
        private const val PREF_NAME = "CityPreferences"
        private const val DEFAULT_STRING_VALUE = "Boston"

        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager =
            instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getString(key: String): String =
        sharedPreferences.getString(key, DEFAULT_STRING_VALUE) ?: DEFAULT_STRING_VALUE

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}

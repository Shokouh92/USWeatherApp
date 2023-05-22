package com.example.usweatherapplication.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is application class
 */
@HiltAndroidApp
class USWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Add any initialization code here
    }

    override fun onTerminate() {
        // Add any termination code here
        super.onTerminate()
    }

    // Add any other necessary methods or code here

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // The main method is not typically used in Android applications
            // It is provided here for demonstration purposes only
            val app = USWeatherApplication()
            app.onCreate()
            // Perform application tasks here
            app.onTerminate()
        }
    }
}

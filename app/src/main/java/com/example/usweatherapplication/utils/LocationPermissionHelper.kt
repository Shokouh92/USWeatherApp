package com.example.usweatherapplication.utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * LocationPermission Helper
 */
class LocationPermissionHelper(private val activity: AppCompatActivity) {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    fun checkAndRequestLocationPermission() {
        if (isLocationPermissionGranted()) {
            performLocationAction()
        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(activity, locationPermission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(activity, arrayOf(locationPermission), LOCATION_PERMISSION_REQUEST_CODE)
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performLocationAction()
            } else {
                showToast("Permission Denied")
            }
        }
    }

    private fun performLocationAction() {
        showToast("Permission Granted")
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}


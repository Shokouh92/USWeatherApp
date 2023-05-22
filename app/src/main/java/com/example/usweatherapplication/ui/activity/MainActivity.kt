package com.example.usweatherapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usweatherapplication.R
import com.example.usweatherapplication.databinding.ActivityMainBinding
import com.example.usweatherapplication.ui.viewModels.WeatherViewModel
import com.example.usweatherapplication.utils.Constants.Companion.ICON_BASE_URL
import com.example.usweatherapplication.utils.Constants.Companion.SHARED_PREF_CITY_KEY
import com.example.usweatherapplication.utils.LocationPermissionHelper
import com.example.usweatherapplication.utils.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionHelper = LocationPermissionHelper(this)
        locationPermissionHelper.checkAndRequestLocationPermission()

        setUpViewModel()

        setupObservers()

        binding.theSearchButton.setOnClickListener {
            val searchText = binding.theSearchBar.text
            if (searchText.isEmpty()) {
                showToast("Please Enter a valid city!")
            } else {
                weatherViewModel.getWeatherFromAPI(searchText.toString())
            }
        }

        loadLastEnteredCity()
    }

    override fun onPause() {
        super.onPause()
        saveLastEnteredCity()
    }

    private fun setUpViewModel() {
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    private fun setupObservers() {
        weatherViewModel.isWeatherDataAvailable.observe(this) { isAvailable ->
            binding.weatherDataAvailable.visibility = if (isAvailable) View.VISIBLE else View.GONE
            binding.weatherDataNotAvailable.visibility = if (isAvailable) View.GONE else View.VISIBLE
        }

        weatherViewModel.isShowProgress.observe(this) { isShowProgress ->
            binding.mainProgressBar.visibility = if (isShowProgress) View.VISIBLE else View.GONE
        }

        weatherViewModel.errorMessage.observe(this) { message ->
            showToast(message)
        }

        weatherViewModel.responseContainer.observe(this) { response ->
            response?.let {
                with(binding) {
                    setLabelText(labelLatitude, "Latitude : ${it.coord?.lat}")
                    setLabelText(labelLongitude, "Longitude : ${it.coord?.lon}")
                    setLabelText(labelWeather, "Weather : ${it.weather?.getOrNull(0)?.description}")
                    setLabelText(labelWindSpeed, "Wind Speed : ${it.wind?.speed}")
                    setLabelText(labelWindDegree, "Wind Degree : ${it.wind?.deg}")
                    setLabelText(labelWindTemperature, "Temperature : ${it.main?.temp}")
                    setLabelText(labelWindPressure, "Pressure : ${it.main?.pressure}")
                    setLabelText(labelWindHumidity, "Humidity : ${it.main?.humidity}")
                    loadImageWeather(it.weather?.get(0)?.icon)
                }
            }
        }
    }

    private fun setLabelText(label: TextView, value: String?) {
        label.text = value.orEmpty()
    }

    private fun loadLastEnteredCity() {
        val sharedPreferencesUtil = SharedPreferencesManager.getInstance(this)
        val myLastEnteredCity = sharedPreferencesUtil.getString(SHARED_PREF_CITY_KEY)
        if (myLastEnteredCity != null) {
            binding.theSearchBar.text = Editable.Factory.getInstance().newEditable(myLastEnteredCity)
            weatherViewModel.getWeatherFromAPI(myLastEnteredCity)
        } else {
            showToast("Please enter a US city name to search its weather")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveLastEnteredCity() {
        val sharedPreferencesUtil = SharedPreferencesManager.getInstance(this)
        sharedPreferencesUtil.putString(SHARED_PREF_CITY_KEY, binding.theSearchBar.text.toString())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.handlePermissionsResult(requestCode, grantResults)
    }

    private fun loadImageWeather(icon: String?) {
        Glide.with(this)
            .load(ICON_BASE_URL + icon + ".png")
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.imageWeather)
    }
}

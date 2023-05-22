package com.example.usweatherapplication.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usweatherapplication.network.WeatherResponse
import com.example.usweatherapplication.network.NetworkAPIService
import com.example.usweatherapplication.utils.Constants.Companion.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View model class with one function for API calling getWeatherFromAPI()
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(private val retrofitInstance: NetworkAPIService) : ViewModel() {
    private val _responseContainer = MutableLiveData<WeatherResponse>()
    val responseContainer: LiveData<WeatherResponse> get() = _responseContainer

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isShowProgress = MutableLiveData<Boolean>()
    val isShowProgress: LiveData<Boolean> get() = _isShowProgress

    private val _isWeatherDataAvailable = MutableLiveData<Boolean>()
    val isWeatherDataAvailable: LiveData<Boolean> get() = _isWeatherDataAvailable

    private var job: Job? = null

    fun getWeatherFromAPI(expression: String) {
        _isShowProgress.value = true
        _isWeatherDataAvailable.value = false
        job = viewModelScope.launch {
            try {
                val response = retrofitInstance.getWeatherByCity(expression, API_KEY)
                if (response.isSuccessful) {
                    _responseContainer.postValue(response.body())
                    _isWeatherDataAvailable.value = true
                } else {
                    val errorMessage = "Error: ${response.message()}"
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Error: ${e.localizedMessage}"
                onError(errorMessage)
            } finally {
                _isShowProgress.value = false
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _isShowProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

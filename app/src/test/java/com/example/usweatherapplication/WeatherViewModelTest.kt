package com.example.usweatherapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.usweatherapplication.network.Clouds
import com.example.usweatherapplication.network.Coord
import com.example.usweatherapplication.network.Main
import com.example.usweatherapplication.network.Sys
import com.example.usweatherapplication.network.Weather
import com.example.usweatherapplication.network.WeatherResponse
import com.example.usweatherapplication.network.Wind
import com.example.usweatherapplication.network.NetworkAPIService
import com.example.usweatherapplication.ui.viewModels.WeatherViewModel
import com.example.usweatherapplication.utils.Constants.Companion.API_KEY
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response


class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var retrofitInstance: NetworkAPIService

    private lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        weatherViewModel = WeatherViewModel(retrofitInstance)
    }

    @Test
    fun testWeatherAPI_Success() = runBlocking {
        // Create a mock weather response object
        val weatherResponse = WeatherResponse(
            Coord(-87.65, 41.85),
            listOf(Weather(802, "Clouds", "scattered clouds", "03d")),
            "stations",
            Main(290.41, 289.13, 288.2, 292.35, 1020, 36),
            10000,
            Wind(1.34, 187),
            Clouds(26),
            1684425271,
            Sys(2, 2005153, "US", 1684405680, 1684458377),
            -18000,
            4887398,
            "Chicago",
            200,
            "Success Response"
        )

        // Mock the API service response
        `when`(retrofitInstance.getWeatherByCity("Chicago", API_KEY))
            .thenReturn(
                Response.success(weatherResponse)
            )

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("Chicago")

        // Verify the expected behavior or assertions
        assertEquals(weatherResponse, weatherViewModel.responseContainer.value)
        assertEquals(false, weatherViewModel.isShowProgress.value)
        assertEquals(true, weatherViewModel.isWeatherDataAvailable.value)
        assertNull(weatherViewModel.errorMessage.value)
    }

    @Test
    fun testWeatherAPI_Error() = runBlocking {
        val errorMessage = "Error: City not found"

        // Mock the API service response with an error
        `when`(retrofitInstance.getWeatherByCity("InvalidCity", API_KEY))
            .thenReturn(
                Response.error(404, ResponseBody.create("application/json".toMediaTypeOrNull(), errorMessage))
            )

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("InvalidCity")

        // Verify the expected behavior or assertions
        assertNull(weatherViewModel.responseContainer.value)
        assertEquals(false, weatherViewModel.isShowProgress.value)
        assertEquals(false, weatherViewModel.isWeatherDataAvailable.value)
        assertEquals(errorMessage, weatherViewModel.errorMessage.value)
    }

    @Test(expected = Exception::class)
    fun testWeatherAPI_Exception() = runBlocking {
        // Mock the API service response with an exception
        `when`(retrofitInstance.getWeatherByCity("ExceptionCity", API_KEY))
            .thenThrow(Exception("Network Exception"))

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("ExceptionCity")

        // Verify the expected behavior or assertions
        // Exception is expected to be thrown
    }
}

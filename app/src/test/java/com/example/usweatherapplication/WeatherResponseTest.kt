package com.example.usweatherapplication

import com.example.usweatherapplication.network.Clouds
import com.example.usweatherapplication.network.Coord
import com.example.usweatherapplication.network.Main
import com.example.usweatherapplication.network.Sys
import com.example.usweatherapplication.network.Weather
import com.example.usweatherapplication.network.WeatherResponse
import com.example.usweatherapplication.network.Wind
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherResponseTest {

    @Test
    fun weatherResponseToReturnCorrectValues() {
        // Create sample data for testing
        val coord = Coord(40.7128, -74.0060) // New York City coordinates
        val weather = listOf(Weather(800, "Clear", "Clear sky", "01d")) // Clear sky weather
        val base = "stations"
        val main = Main(25.0, 23.5, 22.0, 26.0, 1016, 65) // Sample temperature and pressure values
        val visibility = 10000
        val wind = Wind(5.0, 180) // Sample wind speed and degree
        val clouds = Clouds(10) // 10% cloudiness
        val dt = 1635451200L // Unix timestamp
        val sys = Sys(1, 123, "US", 1635424851L, 1635464281L) // Sample sys data
        val timezone = -14400 // Eastern Daylight Time (EDT) offset
        val id = 98765 // Sample city ID
        val name = "New York City"
        val cod = 200
        val message = "Success"

        // Create WeatherResponse object
        val weatherResponse = WeatherResponse(
            coord, weather, base, main, visibility, wind,
            clouds, dt, sys, timezone, id, name, cod, message
        )

        // Verify the values
        assertEquals(coord, weatherResponse.coord)
        assertEquals(weather, weatherResponse.weather)
        assertEquals(base, weatherResponse.base)
        assertEquals(main, weatherResponse.main)
        assertEquals(visibility, weatherResponse.visibility)
        assertEquals(wind, weatherResponse.wind)
        assertEquals(clouds, weatherResponse.clouds)
        assertEquals(dt, weatherResponse.dt)
        assertEquals(sys, weatherResponse.sys)
        assertEquals(timezone, weatherResponse.timezone)
        assertEquals(id, weatherResponse.id)
        assertEquals(name, weatherResponse.name)
        assertEquals(cod, weatherResponse.cod)
        assertEquals(message, weatherResponse.message)
    }
}


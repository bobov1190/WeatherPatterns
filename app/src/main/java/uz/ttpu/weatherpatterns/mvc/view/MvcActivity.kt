package uz.ttpu.weatherpatterns.mvc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvc.controller.WeatherController
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.ui.WeatherScreen

class MvcActivity : ComponentActivity(), WeatherView {

    private val model = WeatherModel(WeatherModule.repository)
    private val controller = WeatherController(model, view = this)
    private var status by mutableStateOf("Pick a city")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WeatherScreen(status = status, onCitySelected = controller::onCitySelected)
            }
        }
    }

    // render() has no parameters because in MVC the View can read the Model itself.
    // The Controller only says "the Model changed" and the View takes the data from the Model.
    // If the Controller passed the Weather into the View, the View would not read the Model anymore,
    // it would just show what it gets, and the Controller would have to handle loading and error too.
    // That is already MVP, not MVC.
    override fun render() {
        val loading = model.isLoading
        val error = model.error
        val weather = model.weather
        status = when {
            loading -> "Loading…"
            error != null -> "Error: $error"
            weather != null -> "${weather.city}: ${weather.temperatureC}°C, ${weather.condition}"
            else -> "Pick a city"
        }
    }
}

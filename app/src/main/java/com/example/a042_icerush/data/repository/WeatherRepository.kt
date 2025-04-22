package com.example.a042_icerush.data.repository

import android.util.Log
import com.example.a042_icerush.data.network.WeatherClient
import com.example.a042_icerush.data.repository.Result
import com.example.a042_icerush.domain.model.WeatherReportModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val dataService: WeatherClient) {
    private val API_KEY = "bc471261722097245973655b2459651a"

    /**
     * Prend en paramètres la latitude (lat) et la longitude (lng) pour spécifier l'emplacement
     * désiré. Utilise un flux (flow) pour gérer les données de manière asynchrone, créant ainsi
     * une coroutine. À chaque fois qu’une nouvelle donnée sera disponible, nous n’aurons qu'à
     * émettre avec la fonction “emit” propre au “flow”.
     */
    fun fetchForecastData(lat: Double, lng: Double): Flow<Result<List<WeatherReportModel>>> =
        flow {
            emit(Result.Loading)
            val result = dataService.getWeatherByPosition(
                latitude = lat,
                longitude = lng,
                apiKey = API_KEY
            )
            val model = result.body()?.toDomainModel() ?: throw Exception("Invalid data")
            emit(Result.Success(model))
        }.catch { error ->
//            Log.e("WeatherRepository", error.message ?: "")
            emit(Result.Failure(error.message))
        }
}
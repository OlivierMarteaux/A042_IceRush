package com.example.a042_icerush.domain.model

import java.util.Calendar

data class WeatherReportModel(
    val isGoodForSnowMaking: Boolean,
    val date: Calendar,
    val temperatureCelsius: Int,
    val weatherTitle: String,
    val weatherDescription: String
)
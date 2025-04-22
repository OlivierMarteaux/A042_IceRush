package com.example.a042_icerush.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a042_icerush.data.repository.WeatherRepository
import com.example.a042_icerush.data.repository.Result
import com.example.a042_icerush.domain.model.WeatherReportModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: WeatherRepository) :
    ViewModel() {

    // Expose screen UI state
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getForecastData()
    }

    private fun getForecastData() {
        // Mt Blanc: 45.83333897014585, 6.865303600397398
        //olonkinbyen:  [70.9221, -8.7187]
        val latitude = 70.9221
        val longitude = -8.7187
        dataRepository.fetchForecastData(latitude, longitude)
            .onEach { forecastUpdate ->
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            forecast = forecastUpdate
//                        )
//                    }
                when (forecastUpdate) {
                    is Result.Failure ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isViewLoading = false,
                                errorMessages = forecastUpdate.message
                            )
                        }

                    is Result.Loading ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isViewLoading = true,
                                errorMessages = null
                            )
                        }

                    is Result.Success ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                forecast = forecastUpdate.value,
                                isViewLoading = false,
                                errorMessages = null
                            )
                        }
                }
            }.launchIn(viewModelScope)
    }
}

data class HomeUiState(
    val forecast: List<WeatherReportModel> = emptyList(),
    val isViewLoading: Boolean = false,
    val errorMessages: String? = null
)
package com.example.a042_icerush

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a042_icerush.databinding.ActivityMainBinding
import com.example.a042_icerush.domain.model.WeatherReportModel
import com.example.a042_icerush.presentation.home.HomeViewModel
import com.example.a042_icerush.presentation.home.WeatherAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WeatherAdapter.OnItemClickListener {

    override fun onItemClick(weather: WeatherReportModel) {
        Toast
            .makeText(
            this,
            "${weather.weatherTitle}\n${weather.temperatureCelsius}°C",
            Toast.LENGTH_SHORT
            )
            .show()
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val  customAdapter = WeatherAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defineRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateCurrentWeather(it.forecast)
                    binding.progressBar.isVisible = it.isViewLoading
                    if(it.errorMessages?.isNotBlank() == true){
                        Snackbar.make(binding.root, it.errorMessages, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        /** Test synchrone instruction*/
//        for (i in 1..10000){
//            Thread.sleep(5)
//        }

        /** Test asynchrone instruction */
//        GlobalScope.launch(Dispatchers.IO) {
//            for (i in 1..10000){
//            Thread.sleep(1)
//            }
//            println("Sleepy function done")
//        }

    }

    /**
     * En utilisant la fonction collect, nous observons les changements d'état.
     * A chaque émission d'une nouvelle liste de données, l'ensemble des données de l'Adapter
     * de la RecyclerView est mis à jour, assurant ainsi une représentation en temps réel des
     * informations dans l'interface utilisateur.
     *
     * Ainsi, cette approche garantit une gestion efficace des changements d'état, une mise à jour
     * réactive de l'interface utilisateur et une expérience utilisateur fluide au sein de votre
     * application Android.
     */
    private fun updateCurrentWeather(forecast: List<WeatherReportModel>){
        customAdapter.submitList(forecast)
    }

    private fun defineRecyclerView(){
        val layoutManager = LinearLayoutManager(applicationContext /*this*/)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = customAdapter
    }
}
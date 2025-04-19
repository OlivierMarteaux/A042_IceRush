package com.example.a042_icerush.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a042_icerush.databinding.ItemWeatherBinding
import com.example.a042_icerush.domain.model.WeatherReportModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * L'Adapter a pour rôle de lier les données à la vue. Il agit comme un pont entre les données,
 * généralement stockées dans une liste, et la vue qui les affiche. L'adapter crée également
 * les ViewHolders nécessaires pour chaque élément de données, ce qui permet un recyclage
 * efficace des vues et une meilleure performance.
 */
class WeatherAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<WeatherReportModel, WeatherAdapter.WeatherViewHolder>(DiffCallback) {

    interface OnItemClickListener {
        fun onItemClick(weather: WeatherReportModel)
    }

    /**
     * Un ViewHolder représente chaque élément individuel dans la liste.
     * Il conserve une référence aux vues à l'intérieur de chaque élément de la liste,
     * ce qui évite de rechercher ces vues à chaque mise à jour.
     */
    class WeatherViewHolder(
        private val binding: ItemWeatherBinding,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault())

        /**
         * bind lie les propriétés des données de prévisions météorologiques aux éléments de la vue,
         * tels que la température, les températures maximale et minimale, ainsi que les
         * précipitations. C’est la méthode que vous modifierez le plus afin d’afficher
         * les données souhaitées.
         */
        fun bind(weather: WeatherReportModel) {
            val formattedDate: String = dateFormatter.format(weather.date.time)
            binding.textViewDateTime.text = formattedDate
            binding.textViewSnowMaking.text = if (weather.isGoodForSnowMaking) "❄️" else "🌧️ ️"
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(weather)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(itemView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = getItem(position)
        holder.bind(weather)
    }

    /**
     * DiffCallBack aide à déterminer quelles données ont changé entre les anciennes et les
     * nouvelles listes d'éléments. Cela aide à optimiser l'affichage en mettant à jour uniquement
     * les parties nécessaires de l'interface utilisateur lorsqu'il y a des changements.
     * Dans notre cas, nous vérifions seulement si la date change.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<WeatherReportModel>() {
        override fun areItemsTheSame(oldItem: WeatherReportModel, newItem: WeatherReportModel): Boolean {
            return oldItem.date == newItem.date
        }
        override fun areContentsTheSame(oldItem: WeatherReportModel, newItem: WeatherReportModel): Boolean {
            return oldItem == newItem
        }
    }
}
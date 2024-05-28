package com.example.kiedyprzyjedzieextended.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kiedyprzyjedzieextended.R
import com.example.kiedyprzyjedzieextended.types.TimetableDeparture

class TimetableDeparturesAdapter(private var dataSet: List<TimetableDeparture>) :
    RecyclerView.Adapter<TimetableDeparturesAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbol = itemView.findViewById<TextView>(R.id.departure_symbol)
        val time = itemView.findViewById<TextView>(R.id.departure_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.departure_timetable, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val departure = dataSet[position]

        holder.symbol.text = departure.symbols
        holder.time.text = convertToTime(departure.departure)
    }

    override fun getItemCount(): Int {
        Log.d("dataSetSize", dataSet.size.toString())
        return dataSet.size
    }
    @SuppressLint("DefaultLocale")
    fun convertToTime(seconds: Int): String {
        val totalMinutes = seconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02d:%02d", hours, minutes)
    }

}


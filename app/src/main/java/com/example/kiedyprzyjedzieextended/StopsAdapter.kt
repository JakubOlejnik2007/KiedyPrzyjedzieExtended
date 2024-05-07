package com.example.kiedyprzyjedzieextended

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kiedyprzyjedzieextended.types.Stop

class StopsAdapter(private val dataSet: List<Stop>) :
    RecyclerView.Adapter<StopsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.stop, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stop: Stop = dataSet[position]
        Log.d("STOP", stop.toString())
        val stopId = holder.itemView.findViewById<TextView>(R.id.stopId)
        stopId.text = stop.stopNumber.toString()
        val stopName = holder.itemView.findViewById<TextView>(R.id.stopName)
        stopName.text = stop.stopName
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
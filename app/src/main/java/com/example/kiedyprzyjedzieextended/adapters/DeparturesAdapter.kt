package com.example.kiedyprzyjedzieextended.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kiedyprzyjedzieextended.R
import com.example.kiedyprzyjedzieextended.interfaces.RecyclerClickListener
import com.example.kiedyprzyjedzieextended.types.Departure
import org.w3c.dom.Text

class DeparturesAdapter(private var dataSet: List<Departure>, private val clickListener: RecyclerClickListener) :
    RecyclerView.Adapter<DeparturesAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lineNameTextView: TextView = itemView.findViewById(R.id.lineName)
        val busIconImageView: ImageView = itemView.findViewById(R.id.busIcon)
        val direction: TextView = itemView.findViewById(R.id.direction)
        val arrivalTimeTextView: TextView = itemView.findViewById(R.id.arrivalTime)
        val busAttributesLinearLayout: LinearLayout = itemView.findViewById(R.id.busAttributes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.departure, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val departure = dataSet[position]

        holder.lineNameTextView.text = departure.line_name
        holder.direction.text = departure.direction
        holder.arrivalTimeTextView.text = departure.time ?: ">>>"
        if(departure.time == null || departure.time == "< 1 min" || (!departure.time.contains(":") && departure.time.split(" ")[0].toInt() in 0..5)) holder.arrivalTimeTextView.setTextColor(0xFFFF0000.toInt())
        holder.busIconImageView.setImageResource(if(departure.vehicle_type == 3) R.drawable.small_bus else R.drawable.big_bus)

        holder.itemView.setOnClickListener {
            clickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        Log.d("dataSetSize", dataSet.size.toString())
        return dataSet.size
    }

}


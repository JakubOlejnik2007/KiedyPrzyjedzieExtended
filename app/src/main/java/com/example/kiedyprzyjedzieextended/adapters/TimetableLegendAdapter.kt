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
import com.example.kiedyprzyjedzieextended.types.Direction
import com.example.kiedyprzyjedzieextended.types.TimetableDirection
import org.w3c.dom.Text

class TimetableLegendAdapter(private var dataSet: List<TimetableDirection>) :
    RecyclerView.Adapter<TimetableLegendAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbol = itemView.findViewById<TextView>(R.id.legend_symbol)
        val description = itemView.findViewById<TextView>(R.id.legend_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.legend_timetable, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val direction = dataSet[position]

        holder.symbol.text = direction.symbol
        holder.description.text = direction.name
    }

    override fun getItemCount(): Int {
        Log.d("dataSetSize", dataSet.size.toString())
        return dataSet.size
    }

}


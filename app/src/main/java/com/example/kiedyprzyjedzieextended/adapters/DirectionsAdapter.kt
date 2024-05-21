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
import org.w3c.dom.Text

class DirectionsAdapter(private var dataSet: List<Direction>, private val clickListener: RecyclerClickListener) :
    RecyclerView.Adapter<DirectionsAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.direction)
        val lineNameTextView: TextView = itemView.findViewById(R.id.lineName)
        val directionNameTextView: TextView = itemView.findViewById(R.id.directionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.direction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val direction = dataSet[position]

        if(!direction.active) holder.linearLayout.background.alpha = 64

        holder.lineNameTextView.text = direction.line
        holder.directionNameTextView.text = direction.direction
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        Log.d("dataSetSize", dataSet.size.toString())
        return dataSet.size
    }

}


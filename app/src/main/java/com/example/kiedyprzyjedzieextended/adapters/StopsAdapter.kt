package com.example.kiedyprzyjedzieextended.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kiedyprzyjedzieextended.R
import com.example.kiedyprzyjedzieextended.types.Stop

class StopsAdapter(private var dataSet: List<Stop>, private val clickListener: RecyclerViewClickListener) :
    RecyclerView.Adapter<StopsAdapter.ViewHolder>(), Filterable {

    private var filteredStops: List<Stop> = dataSet

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stopIdTextView: TextView = itemView.findViewById(R.id.stopId)
        val stopNameTextView: TextView = itemView.findViewById(R.id.stopName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stop, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stop = filteredStops[position]
        holder.stopIdTextView.text = stop.stopNumber.toString()
        holder.stopNameTextView.text = stop.stopName
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return filteredStops.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Stop>()

                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(dataSet)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()

                    for (item in dataSet) {
                        if (item.stopName.lowercase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredStops = results?.values as List<Stop>
                notifyDataSetChanged()
            }
        }
    }
}

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
}

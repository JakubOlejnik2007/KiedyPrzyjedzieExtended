package com.example.kiedyprzyjedzieextended.ui.home

import android.content.Intent
import com.example.kiedyprzyjedzieextended.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiedyprzyjedzieextended.BusStopActivity
import com.example.kiedyprzyjedzieextended.adapters.RecyclerViewClickListener
import com.example.kiedyprzyjedzieextended.adapters.StopsAdapter
import com.example.kiedyprzyjedzieextended.databinding.FragmentHomeBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToStopArray
import com.example.kiedyprzyjedzieextended.helpers.fetchJSONData
import com.example.kiedyprzyjedzieextended.types.Stop
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var stopsList: List<Stop>? = null
    private var isDataLoaded = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getStopsList()

        binding.stopsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stopsList?.let { stops ->
                    (binding.stopsList.adapter as? StopsAdapter)?.filter?.filter(newText)
                }
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getStopsList() {
        val data = fetchJSONData()
        data.observe(viewLifecycleOwner) { result ->
            val resArray = convertJsonToStopArray(result)
            stopsList = resArray.toList()
            isDataLoaded = true
            setStopsAdapter()
        }
    }
    private fun setStopsAdapter() {
        stopsList?.let { stops ->
            val recyclerViewClickListener = object : RecyclerViewClickListener {
                override fun onClick(view: View, position: Int) {
                    val stop = stops.find{ it.stopNumber == view.findViewById<TextView>(R.id.stopId).text.toString().toInt()}
                    val intent = Intent(context, BusStopActivity::class.java)
                    val json = Gson().toJson(stop)
                    //intent.putExtra("stopJSON", json)
                    startActivity(intent)
                    Log.d("stop", stop.toString())
                }
            }

            val stopsAdapter = StopsAdapter(stops, recyclerViewClickListener)

            binding.stopsList.adapter = stopsAdapter
            binding.stopsList.layoutManager = LinearLayoutManager(context)
        }

    }
}

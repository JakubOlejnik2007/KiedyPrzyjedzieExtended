package com.example.kiedyprzyjedzieextended.ui.departures

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiedyprzyjedzieextended.BusStopActivity
import com.example.kiedyprzyjedzieextended.R
import com.example.kiedyprzyjedzieextended.adapters.DeparturesAdapter
import com.example.kiedyprzyjedzieextended.databinding.FragmentDeparturesBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToDeparturesObject
import com.example.kiedyprzyjedzieextended.helpers.fetchDeparturesJSONData
import com.example.kiedyprzyjedzieextended.interfaces.RecyclerClickListener
import com.example.kiedyprzyjedzieextended.types.Departures
import com.google.gson.Gson

class DeparturesFragment : Fragment() {

    private var _binding: FragmentDeparturesBinding? = null
    private val binding get() = _binding!!

    lateinit var departures: Departures

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeparturesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.departuresList.layoutManager = LinearLayoutManager(context)


        getDeparturesObject()

        return root
    }

    private fun getDeparturesObject() {
        val data = fetchDeparturesJSONData()
        data.observe(viewLifecycleOwner) {
            result ->
            Log.d("departures", result.toString())
            departures = convertJsonToDeparturesObject(result.toString())
            Log.d("departures", departures.toString())

            val recyclerViewClickListener = object : RecyclerClickListener {
                override fun onClick(view: View, position: Int) {
                }
            }

            Log.d("departures", (departures.rows.toList().toString()))
            binding.departuresList.adapter = DeparturesAdapter(departures.rows.toList(), recyclerViewClickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

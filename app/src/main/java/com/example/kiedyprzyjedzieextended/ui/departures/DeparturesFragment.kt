package com.example.kiedyprzyjedzieextended.ui.departures

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
import com.example.kiedyprzyjedzieextended.databinding.FragmentDeparturesBinding
import com.example.kiedyprzyjedzieextended.helpers.fetchDeparturesJSONData

class DeparturesFragment : Fragment() {

    private var _binding: FragmentDeparturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeparturesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDepartures
        val dashboardViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        getDeparturesObject()

        return root
    }

    private fun getDeparturesObject() {
        val data = fetchDeparturesJSONData()
        Log.d("departures", data.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MapViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
            value = "This is departures Fragment"
        }
        val text: LiveData<String> = _text
    }
}

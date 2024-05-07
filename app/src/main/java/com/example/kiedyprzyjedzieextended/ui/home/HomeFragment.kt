package com.example.kiedyprzyjedzieextended.ui.home

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
import com.example.kiedyprzyjedzieextended.StopsAdapter
import com.example.kiedyprzyjedzieextended.databinding.FragmentHomeBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToStopArray
import com.example.kiedyprzyjedzieextended.helpers.fetchJSONData
import com.example.kiedyprzyjedzieextended.helpers.getJsonFromUrl

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val dashboardViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        getAndShowStops()
        /*TODO w aplikacji należy dodać adapter dla przystanków
        * */


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getAndShowStops() {
        val data = fetchJSONData()
        data.observe(viewLifecycleOwner) { result ->
            val resArray = convertJsonToStopArray(result)
            Log.d("fetch JSON", resArray[264].toString())
            val adapter: StopsAdapter = StopsAdapter(resArray.toList())
            binding.stopsList.adapter = adapter
            binding.stopsList.layoutManager = LinearLayoutManager(requireContext())

        }
    }


    class HomeViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
            value = "This is dashboard Fragment"
        }
        val text: LiveData<String> = _text
    }
}

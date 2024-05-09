package com.example.kiedyprzyjedzieextended.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kiedyprzyjedzieextended.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTimetable
        val dashboardViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MapViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
            value = "This is timetable Fragment"
        }
        val text: LiveData<String> = _text
    }
}

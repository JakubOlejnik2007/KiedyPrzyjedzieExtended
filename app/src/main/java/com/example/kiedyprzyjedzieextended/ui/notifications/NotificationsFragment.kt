package com.example.kiedyprzyjedzieextended.ui.notifications

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
import com.example.kiedyprzyjedzieextended.databinding.FragmentDashboardBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DashboardViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
            value = "This is notifications Fragment"
        }
        val text: LiveData<String> = _text
    }
}

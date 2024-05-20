package com.example.kiedyprzyjedzieextended.ui.departures

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiedyprzyjedzieextended.BusStopActivity
import com.example.kiedyprzyjedzieextended.adapters.DeparturesAdapter
import com.example.kiedyprzyjedzieextended.databinding.FragmentDeparturesBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToDeparturesObject
import com.example.kiedyprzyjedzieextended.helpers.fetchDeparturesJSONData
import com.example.kiedyprzyjedzieextended.interfaces.RecyclerClickListener
import com.example.kiedyprzyjedzieextended.types.Departures
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class DeparturesFragment : Fragment() {

    private var _binding: FragmentDeparturesBinding? = null
    private val binding get() = _binding!!
    val handler: Handler = Handler()
    val delayMinutes: Long = 1
    lateinit var departures: Departures
    lateinit var stopId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeparturesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        stopId = (requireActivity() as BusStopActivity).stop.stopId
        binding.departuresList.layoutManager = LinearLayoutManager(context)

        getDeparturesObject()

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun getDeparturesObject() {
        val data = fetchDeparturesJSONData(stopId)
        data.observe(viewLifecycleOwner) {
            result ->
            Log.d("departures", result.toString())
            departures = convertJsonToDeparturesObject(result.toString())
            Log.d("departures", departures.toString())
            if(departures.rows.size == 0){
                binding.emptyListMessage.text = "Brak odjazdów w ciągu najbliższych czterech godzin!"
                binding.nightImage.visibility = View.VISIBLE
            }
            val recyclerViewClickListener = object : RecyclerClickListener {
                override fun onClick(view: View, position: Int) {
                }
            }
            binding.timestamp.text = "Pobrane o godz: ${convertTimestampToTime(departures.timestamp.toLong())}"
            Log.d("departures", (departures.rows.toList().toString()))
            binding.departuresList.adapter = DeparturesAdapter(departures.rows.toList(), recyclerViewClickListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val getDeparturesRunnable = object : Runnable {
        override fun run() {
            getDeparturesObject()
            handler.postDelayed(this, TimeUnit.MINUTES.toMillis(delayMinutes))
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(getDeparturesRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(getDeparturesRunnable)
    }

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}

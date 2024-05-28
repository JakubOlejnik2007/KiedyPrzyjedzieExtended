package com.example.kiedyprzyjedzieextended.ui.timetable

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiedyprzyjedzieextended.BusStopActivity
import com.example.kiedyprzyjedzieextended.TimetableActivity
import com.example.kiedyprzyjedzieextended.adapters.DirectionsAdapter
import com.example.kiedyprzyjedzieextended.databinding.FragmentTimetableBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToDirectionArray
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToTimetableDeparturesObject
import com.example.kiedyprzyjedzieextended.helpers.fetchDirectionsJSONData
import com.example.kiedyprzyjedzieextended.helpers.fetchTimetableDeparturesJSONData
import com.example.kiedyprzyjedzieextended.interfaces.RecyclerClickListener
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimetableFragment : Fragment() {

    lateinit var activity: BusStopActivity;

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!
    @RequiresApi(Build.VERSION_CODES.O)
    var date = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity = requireActivity() as BusStopActivity

        binding.dateDown.setOnClickListener {view ->
            run {
                date = date.minusDays(1)
                binding.date.text = formatDate(date)
                getDirectionsList()
            }
        }
        binding.dateUp.setOnClickListener {view ->
            run {
                date = date.plusDays(1)
                binding.date.text = formatDate(date)
                getDirectionsList()
            }
        }


        getDirectionsList()

        return root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDirectionsList() {
        val data = fetchDirectionsJSONData(activity.stop.stopId, formatDate(date))
        data.observe(viewLifecycleOwner) { result ->
            val resArray = convertJsonToDirectionArray(result.toString())
            val recyclerViewClickListener = object : RecyclerClickListener {
                override fun onClick(view: View, position: Int) {
                    val scheduleData = fetchTimetableDeparturesJSONData(activity.stop.stopId, resArray[position].line, formatDate(date))
                    scheduleData.observe(viewLifecycleOwner) { result ->
                        val resObject = convertJsonToTimetableDeparturesObject(result.toString())
                        if(resObject.empty != null && resObject.empty) {
                            binding.directionsList.visibility = View.GONE
                            binding.noDirectionsContainer.visibility = View.VISIBLE
                            binding.controlsTimetable.visibility = View.GONE
                        }

                        val jsonParser = Gson()
                        val timetableDeparturesString = jsonParser.toJson(resObject)

                        val intent = Intent(requireActivity(), TimetableActivity::class.java)
                        intent.putExtra("data", timetableDeparturesString)
                        startActivity(intent)
                    }
                }
            }
            Log.d("resArray", resArray.toList().toString())
            if(resArray.isEmpty()) {
                binding.directionsList.visibility = View.GONE
                binding.noDirectionsContainer.visibility = View.VISIBLE
                binding.controlsTimetable.visibility = View.GONE
            } else {
                binding.directionsList.visibility = View.VISIBLE
                binding.noDirectionsContainer.visibility = View.INVISIBLE
                binding.controlsTimetable.visibility = View.VISIBLE
            }
            binding.date.text = formatDate(date)
            binding.directionsList.layoutManager = LinearLayoutManager(context)
            binding.directionsList.adapter = DirectionsAdapter(resArray.toList(), recyclerViewClickListener)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(formatter)
    }

}

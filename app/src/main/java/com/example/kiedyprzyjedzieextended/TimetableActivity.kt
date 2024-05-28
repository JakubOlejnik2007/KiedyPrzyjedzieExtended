package com.example.kiedyprzyjedzieextended

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiedyprzyjedzieextended.adapters.TimetableDeparturesAdapter
import com.example.kiedyprzyjedzieextended.adapters.TimetableLegendAdapter
import com.example.kiedyprzyjedzieextended.types.TimetableDepartures
import com.example.kiedyprzyjedzieextended.types.TimetableDirection
import com.google.gson.Gson

class TimetableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        val sharedData = intent.getSerializableExtra("data") as String

        val departuresTimetable = Gson().fromJson(sharedData, TimetableDepartures::class.java)

        val listViewTimetable = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.timetable)
        val listViewLegend = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.legend)


        var arrayOfDeviationsAndDirections:Array<TimetableDirection> = emptyArray<TimetableDirection>()
        arrayOfDeviationsAndDirections += departuresTimetable.deviations!!
        arrayOfDeviationsAndDirections += departuresTimetable.directions!!

        Log.d("arrayOfD&D", arrayOfDeviationsAndDirections.toList().toString())
        val legendAdapter = TimetableLegendAdapter(arrayOfDeviationsAndDirections.toList())
        listViewLegend.layoutManager = LinearLayoutManager(this)
        listViewLegend.adapter = legendAdapter


        val timetableAdapter = TimetableDeparturesAdapter(departuresTimetable.departures)
        listViewTimetable.layoutManager = LinearLayoutManager(this)
        listViewTimetable.adapter = timetableAdapter


    }
}
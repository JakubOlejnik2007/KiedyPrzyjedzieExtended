package com.example.kiedyprzyjedzieextended.helpers
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kiedyprzyjedzieextended.types.Stop
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*

fun convertJsonToStopArray(jsonString: String): Array<Stop> {
    val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
    val jsonArray = jsonObject.getAsJsonArray("stops")
    val stops = mutableListOf<Stop>()
    println(jsonArray)

    jsonArray.forEach { element ->
        val array = element.asJsonArray
        val stopId = array[0].asString
        val stopNumber = array[1].asInt
        val stopName = array[2].asString
        val lon = array[3].asDouble
        val lat = array[4].asDouble

        stops.add(Stop(stopId, stopNumber, stopName, lon, lat))
    }

    return stops.toTypedArray()
}


fun fetchStopsJSONData(): LiveData<String> {
    val result = MutableLiveData<String>()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = getJsonFromUrl("https://radzymin.kiedyprzyjedzie.pl/stops")
            result.postValue(response)
        } catch (e: Exception) {
            result.postValue("Error: ${e.message}")
        }
    }

    return result
}
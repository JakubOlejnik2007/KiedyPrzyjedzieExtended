package com.example.kiedyprzyjedzieextended.helpers
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kiedyprzyjedzieextended.types.Departure
import com.example.kiedyprzyjedzieextended.types.Departures
import com.example.kiedyprzyjedzieextended.types.Stop
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
fun convertJsonToDeparturesArray(jsonString: String): Departures {
    val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)

    val timestamp = jsonObject["timestamp"].asInt
    val designator = jsonObject["designator"].asInt
    val stationName = jsonObject["station_name"].asString
    val onlyDisembarking = jsonObject["only_disembarking"].asBoolean
    val directions = jsonObject["directions"].asJsonObject
        .entrySet().associate { it.key to it.value.asString }
    val deviations = jsonObject["deviations"].asJsonObject
        .entrySet().associate { it.key to it.value.asString }
    val rows = jsonObject["rows"].asJsonArray.map { departureJson ->
        val departureObject = departureJson.asJsonObject
        Departure(
            departureObject["time"].asString,
            departureObject["static_time"].asString,
            departureObject["time_diff"].asInt,
            departureObject["at_stop"].asBoolean,
            departureObject["canceled"].asBoolean,
            departureObject["is_estimated"].asBoolean,
            departureObject["direction_id"].asInt,
            departureObject["platform"].asString,
            departureObject["deviation_id"]?.asString,
            departureObject["line_name"].asString,
            departureObject["show_line_name"].asBoolean,
            departureObject["vehicle_type"].asInt,
            departureObject["vehicle_attributes"].asJsonArray.map { it.asString }.toTypedArray(),
            departureObject["trip_id"].asString,
            departureObject["trip_execution_id"].asString,
            departureObject["trip_index"].asInt
        )
    }.toTypedArray()

    return Departures(timestamp, rows, directions, deviations, designator, stationName, onlyDisembarking)
}


fun fetchDeparturesJSONData(stopId: String = "383723:82865"): LiveData<String> {
    val result = MutableLiveData<String>()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = getJsonFromUrl("https://radzymin.kiedyprzyjedzie.pl/api/departures/$stopId")
            result.postValue(response)
        } catch (e: Exception) {
            result.postValue("Error: ${e.message}")
        }
    }

    return result
}

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
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
fun convertJsonToDeparturesObject(jsonString: String): Departures {
    Log.d("json", jsonString)
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
            if(departureObject["time"] != null && departureObject["time"] !is JsonNull)
            departureObject["time"].asString
            else
                null,
            if(departureObject["static_time"] != null && departureObject["static_time"] !is JsonNull)
                departureObject["static_time"].asString
            else
                null,
            if(departureObject["time_diff"] != null && departureObject["time_diff"] !is JsonNull)
                departureObject["time_diff"].asInt
            else
                null,
            departureObject["at_stop"].asBoolean,
            departureObject["canceled"].asBoolean,
            departureObject["is_estimated"].asBoolean,
            departureObject["direction_id"].asInt,
            departureObject["platform"].asString,
            if (departureObject["deviation_id"] != null && departureObject["deviation_id"] !is JsonNull)
                departureObject["deviation_id"].asString
            else
                null,
            departureObject["line_name"].asString,
            departureObject["show_line_name"].asBoolean,
            departureObject["vehicle_type"].asInt,
            departureObject["vehicle_attributes"].asJsonArray.map { it.asString }.toTypedArray(),
            departureObject["trip_id"].asInt,
            departureObject["trip_execution_id"].asString,
            departureObject["trip_index"].asInt,
            directions[departureObject["direction_id"].toString()] ?: ""
        )
    }.toTypedArray()

    return Departures(timestamp, rows, directions, deviations, designator, stationName, onlyDisembarking)
}


fun fetchDeparturesJSONData(stopId: String = "266339:309579"): LiveData<String> {
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

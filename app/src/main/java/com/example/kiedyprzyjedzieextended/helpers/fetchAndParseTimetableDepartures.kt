package com.example.kiedyprzyjedzieextended.helpers
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kiedyprzyjedzieextended.types.TimetableDeparture
import com.example.kiedyprzyjedzieextended.types.TimetableDepartures
import com.example.kiedyprzyjedzieextended.types.TimetableDirection
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.util.Base64
fun convertJsonToTimetableDeparturesObject(jsonString: String): TimetableDepartures {
    Log.d("json", jsonString)
    val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)

    val empty = if(jsonObject["empty"] != null && jsonObject["empty"] !is JsonNull) jsonObject["empty"].asBoolean else false
    val departures = jsonObject["departures"].asJsonArray.map {departure ->
        Gson().fromJson(departure, TimetableDeparture::class.java)
    }.toList()

    val directions = if(jsonObject["directions"] != null && jsonObject["directions"] !is JsonNull)
        jsonObject["directions"].asJsonArray.map {
                deviation -> Gson().fromJson(deviation, TimetableDirection::class.java)
        }.toList() else null

    val main_direction = if(jsonObject["main_direction"] != null && jsonObject["main_direction"] !is JsonNull) Gson().fromJson(jsonObject["main_direction"], TimetableDirection::class.java) else null

    val deviations = if(jsonObject["deviations"] != null && jsonObject["deviations"] !is JsonNull)
        jsonObject["deviations"].asJsonArray.map {
            deviation -> Gson().fromJson(deviation, TimetableDirection::class.java)
        }.toList() else null

    val descriptions = emptyList<Unit>()
    val show_line_name = jsonObject["show_line_name"].asBoolean
    val platform = jsonObject["platform"].asString

    return TimetableDepartures(
        empty, departures, directions, main_direction, deviations, descriptions, show_line_name, platform
    )
}


fun fetchTimetableDeparturesJSONData(stopId: String = "266339:309579", lineName: String, date: String): LiveData<String> {
    val result = MutableLiveData<String>()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = getJsonFromUrl("https://radzymin.kiedyprzyjedzie.pl/api/timetable/$stopId/${Base64.getEncoder().encodeToString(lineName.toByteArray())}?date=$date")
            result.postValue(response)
        } catch (e: Exception) {
            result.postValue("Error: ${e.message}")
        }
    }

    return result
}

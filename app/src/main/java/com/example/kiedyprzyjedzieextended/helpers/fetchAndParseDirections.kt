package com.example.kiedyprzyjedzieextended.helpers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kiedyprzyjedzieextended.types.Direction
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*

fun convertJsonToDirectionArray(jsonString: String): Array<Direction> {
    val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
    val jsonArray = jsonObject.getAsJsonArray("directions")
    val directions = mutableListOf<Direction>()
    Log.d("jsonArray", jsonArray.isEmpty.toString())
    if(!jsonArray.isEmpty)
        jsonArray.forEach { element ->
        val directionObjectJSON = element.asJsonObject
        val line = directionObjectJSON["line"].asString
        val showName = directionObjectJSON["show_name"].asBoolean
        val direction = directionObjectJSON["direction"].asString
        val active = directionObjectJSON["active"].asBoolean

        directions.add(Direction(line, showName, direction, active))
    }

    Log.d("result", directions.toTypedArray().toString())

    return directions.toTypedArray()
}


fun fetchDirectionsJSONData(stopId: String = "266339:309579", date: String = "2024-05-20"): LiveData<String> {
    val result = MutableLiveData<String>()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = getJsonFromUrl("\n" +
                    "https://radzymin.kiedyprzyjedzie.pl/api/directions/$stopId?date=$date")
            result.postValue(response)
        } catch (e: Exception) {
            result.postValue("Error: ${e.message}")
        }
    }

    return result
}
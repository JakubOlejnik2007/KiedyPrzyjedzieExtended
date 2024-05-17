package com.example.kiedyprzyjedzieextended.types

data class Departures(
    val timestamp: Int,
    val rows: Array<Departure>,
    val directions: Map<String, String>,
    val deviations: Map<String, String>,
    val designator: Int,
    val station_name: String,
    val only_disembarking: Boolean
)
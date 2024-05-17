package com.example.kiedyprzyjedzieextended.types

data class Departure(
    val time: String,
    val staticTime: String,
    val time_diff: Int,
    val at_stop: Boolean,
    val canceled: Boolean,
    val is_estimated: Boolean,
    val direction_id: Int,
    val platform: String,
    val deviation_Id: String?,
    val line_name: String,
    val show_line_name: Boolean,
    val vehicle_type: Int,
    val vehicle_attributes: Array<String>,
    val trip_id: String,
    val trip_executuin_id: String,
    val trip_index: Int
)
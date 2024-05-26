package com.example.kiedyprzyjedzieextended.types

data class TimetableDeparture(
    val departure: Int,
    val line: String,
    val symbols: String,
    val trip_id: Int,
    val index: Int
)

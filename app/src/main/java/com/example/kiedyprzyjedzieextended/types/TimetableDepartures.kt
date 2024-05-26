package com.example.kiedyprzyjedzieextended.types

data class TimetableDepartures(
    val empty: Boolean?,
    val departures: List<TimetableDeparture>,
    val directions: List<TimetableDirection>?,
    val main_direction: TimetableDirection?,
    val deviations: List<TimetableDirection>?,
    val descriptions: List<Unit>,
    val show_line_name: Boolean,
    val platform: String

)

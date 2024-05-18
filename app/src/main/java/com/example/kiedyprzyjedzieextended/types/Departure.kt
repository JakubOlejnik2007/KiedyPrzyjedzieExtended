package com.example.kiedyprzyjedzieextended.types


/**
 * Data class representing information about a single departure of a public transport vehicle.
 *
 * @property time The estimated time of arrival of the vehicle, formatted as a string.
 * @property static_time The scheduled time of arrival of the vehicle, formatted as a string.
 * @property time_diff The difference between the estimated and scheduled arrival times, in minutes.
 * @property at_stop A boolean value indicating whether the vehicle is currently at the stop.
 * @property canceled A boolean value indicating whether the departure has been canceled.
 * @property is_estimated A boolean value indicating whether the estimated arrival time is an estimate or a scheduled time.
 * @property direction_id An integer representing the direction of travel for the vehicle.
 * @property platform The platform at which the vehicle will arrive.
 * @property deviation_id An optional string representing the ID of any deviation from the scheduled timetable.
 * @property line_name The name of the line that the vehicle belongs to.
 * @property show_line_name A boolean value indicating whether the line name should be displayed.
 * @property vehicle_type An integer representing the type of vehicle.
 * @property vehicle_attributes An array of strings representing additional attributes of the vehicle.
 * @property trip_id A string representing the ID of the trip that the vehicle is on.
 * @property trip_execution_id A string representing the ID of the execution of the trip.
 * @property trip_index An integer representing the index of the departure within the trip.
 */
data class Departure(
    val time: String,
    val static_time: String,
    val time_diff: Int,
    val at_stop: Boolean,
    val canceled: Boolean,
    val is_estimated: Boolean,
    val direction_id: Int,
    val platform: String,
    val deviation_id: String?,
    val line_name: String,
    val show_line_name: Boolean,
    val vehicle_type: Int,
    val vehicle_attributes: Array<String>,
    val trip_id: Int,
    val trip_execution_id: String,
    val trip_index: Int,
    val direction: String
)

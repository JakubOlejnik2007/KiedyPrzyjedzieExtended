package com.example.kiedyprzyjedzieextended

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToDeparturesObject
import com.example.kiedyprzyjedzieextended.helpers.fetchDeparturesJSONData
import com.example.kiedyprzyjedzieextended.types.Departure

class DisplayDeparturesFromFavouriteWidget : AppWidgetProvider() {

    private var departureList = emptyArray<Departure>()

    private lateinit var context: Context
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var handler: Handler
    private lateinit var componentName: ComponentName
    private var appWidgetIds: IntArray = IntArray(0) // Store appWidgetIds

    private val runnable = object : Runnable {
        override fun run() {

            val favouriteStops = readFavouriteStopIds(context, "FavouriteStops", "FavouriteStops")
            favouriteStops.forEach { stopId ->
                fetchDeparturesJSONData(stopId).observeForever { jsonString ->
                    val departuresObject = convertJsonToDeparturesObject(jsonString)
                    val departures = departuresObject.rows
                    departureList += Departure(
                        at_stop = false,
                        line_name = departuresObject.station_name,
                        canceled = false,
                        deviation_id = null,
                        direction = "",
                        direction_id = 1,
                        is_estimated = false,
                        platform = "123",
                        show_line_name = true,
                        static_time = null,
                        time = "0",
                        time_diff = 0,
                        trip_execution_id = "1",
                        trip_id = 1,
                        trip_index = 1,
                        vehicle_attributes = emptyArray(),
                        vehicle_type = 2137
                    )
                    departureList += departures
                    }}

            DataHolder.departureList = departureList.toList()

            departureList = emptyArray<Departure>()
            Log.d("widget-test", departureList.toList().toString())
            updateWidget()
            handler.postDelayed(this, 1000 * 60)
        }
    }

    private fun updateWidget() {
        Log.d("widget-test", "Updating widget with departures: ${DataHolder.departureList}")
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        val intent = Intent(context, WidgetListService::class.java)

        for (widgetId in appWidgetIds) {
            views.setRemoteAdapter(R.id.widget_list_view, intent)
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view)
            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("widget-test", "onUpdate called with appWidgetIds: ${appWidgetIds.joinToString()}")
        this.context = context
        this.appWidgetManager = appWidgetManager
        this.appWidgetIds = appWidgetIds // Store appWidgetIds
        componentName = ComponentName(context, DisplayDeparturesFromFavouriteWidget::class.java)

        handler = Handler(Looper.getMainLooper())
        handler.post(runnable)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Stop updating widget when disabled
        Log.d("widget-test", "onDisabled called")
        handler.removeCallbacks(runnable)
    }
}

fun readFavouriteStopIds(
    context: Context,
    preferencesName: String,
    key: String
): MutableList<String> {
    val sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    val stopIdsString: String? = sharedPreferences.getString(key, null)
    return if (!stopIdsString.isNullOrEmpty()) {
        stopIdsString.split(",").map { it.trim() }.toMutableList()
    } else {
        emptyList<String>().toMutableList()
    }
}
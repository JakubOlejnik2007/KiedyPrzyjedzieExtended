package com.example.kiedyprzyjedzieextended

import DepartureDataListener
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToDeparturesObject
import com.example.kiedyprzyjedzieextended.helpers.fetchDeparturesJSONData
import com.example.kiedyprzyjedzieextended.types.Departure

class HelloWorldWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, MyWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
                setRemoteAdapter(R.id.widget_list_view, intent)
                setEmptyView(R.id.widget_list_view, android.R.id.empty)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}



class MyWidgetService : RemoteViewsService() {
    var departuresAll: Array<Departure> = emptyArray()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val factory = MyRemoteViewsFactory(applicationContext, intent)

        val favouriteStops = readFavouriteStopIds(applicationContext, "FavouriteStops", "FavouriteStops")


        favouriteStops.forEach { stopId ->
            fetchDeparturesJSONData(stopId).observeForever { jsonString ->
                val departuresObject = convertJsonToDeparturesObject(jsonString)
                val departures = departuresObject.rows
                departuresAll += Departure(
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
                departuresAll += departures
                factory.onDepartureDataChanged(departuresAll.toList())
            }
        }

        return factory
    }
}

class MyRemoteViewsFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory, DepartureDataListener {
    private var departures: List<Departure> = emptyList()

    override fun onDepartureDataChanged(departures: List<Departure>) {
        this.departures = departures
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
            AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, HelloWorldWidget::class.java)
            ), R.id.widget_list_view
        )
    }

    override fun onCreate() {}

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int = departures.size

    override fun getViewAt(position: Int): RemoteViews {
        val departure = departures[position]
        val views = RemoteViews(context.packageName, R.layout.widget_list_item).apply {
            setTextViewText(R.id.text_view, departure.line_name)
        }
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
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

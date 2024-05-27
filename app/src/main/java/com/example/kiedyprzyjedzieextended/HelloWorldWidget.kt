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
import java.util.*

class HelloWorldWidget : AppWidgetProvider() {

    private var departureList = arrayOf("R5", "R7")

    private lateinit var context: Context
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var handler: Handler
    private lateinit var componentName: ComponentName
    private var appWidgetIds: IntArray = IntArray(0) // Store appWidgetIds

    private val runnable = object : Runnable {
        override fun run() {
            departureList += "${Random().nextInt(100)}"
            DataHolder.departureList = departureList.toList()
            Log.d("widget-test", departureList.toList().toString())
            updateWidget()
            handler.postDelayed(this, 10000)
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
        componentName = ComponentName(context, HelloWorldWidget::class.java)

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

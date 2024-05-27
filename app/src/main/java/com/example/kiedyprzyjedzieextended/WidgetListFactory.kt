package com.example.kiedyprzyjedzieextended

import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class WidgetListFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {
        Log.d("test-widget2", "onCreate called")
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        Log.d("test-widget2", "onDataSetChanged called")
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d("test-widget2", "getViewAt called for position: $position")
        val views = RemoteViews(context.packageName, android.R.layout.simple_list_item_1)
        views.setTextViewText(android.R.id.text1, DataHolder.departureList[position])
        return views
    }

    override fun getCount(): Int {
        Log.d("test-widget2", "getCount called, size: ${DataHolder.departureList.size}")
        return DataHolder.departureList.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        Log.d("test-widget2", "onDestroy called")
    }
}

package com.example.kiedyprzyjedzieextended

import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService

class WidgetListService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        Log.d("test-widget3", DataHolder.departureList.toString())
        return WidgetListFactory(applicationContext)
    }
}

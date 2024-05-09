package com.example.kiedyprzyjedzieextended

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kiedyprzyjedzieextended.databinding.ActivityBusStopBinding

class BusStopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusStopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusStopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bus_stop)
        for (i in 1 until navView.menu.size()) {
            navView.menu.getItem(i).setTitle(null)
        }
        onNavigationItemSelected(navView)
        val item0 = navView.menu.getItem(0)
        item0.setTitle(resources.getString(getTitleResById(item0.itemId)))
        NavigationUI.onNavDestinationSelected(item0, navController)

        navView.setOnNavigationItemSelectedListener { item: MenuItem ->
            onNavigationItemSelected(navView)

            item.setTitle(resources.getString(getTitleResById(item.itemId)))
            NavigationUI.onNavDestinationSelected(item, navController)
        }

    }

    private fun onNavigationItemSelected(navView: BottomNavigationView) {
        for (i in 0 until navView.menu.size()) {
            val itemId = navView.menu.getItem(i).itemId
            val title = resources.getString(getTitleResById(itemId))
            navView.menu.getItem(i).setTitle(title)
        }
        for (i in 0 until navView.menu.size()) {
            navView.menu.getItem(i).setTitle(null)
        }

    }
    private fun getTitleResById(itemId: Int): Int {

        val parts = resources.getResourceEntryName(itemId).split("_")
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val resourceName = "title_" + parts[1]

        Log.d("nav", resourceName)
        return resources.getIdentifier(resourceName, "string", packageName)
    }
}
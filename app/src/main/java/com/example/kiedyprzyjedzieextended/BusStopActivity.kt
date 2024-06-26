package com.example.kiedyprzyjedzieextended

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.kiedyprzyjedzieextended.databinding.ActivityBusStopBinding
import com.example.kiedyprzyjedzieextended.types.Stop
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson

class BusStopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusStopBinding
    private lateinit var adapter: FragmentPageAdapter
    lateinit var stop: Stop
    var favouriteStops: MutableList<String> = emptyList<String>().toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedData = intent.getSerializableExtra("stopJSON") as String



        stop = Gson().fromJson(sharedData, Stop::class.java)

        favouriteStops = readFavouriteStopIds(this, "FavouriteStops", "FavouriteStops")

        binding = ActivityBusStopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stopName.text = stop.stopName
        binding.stopNumber.text = stop.stopNumber.toString()

        val addFavourite: ImageView = binding.addFavourite
        if(stop.stopId !in favouriteStops) {
            val drawable = ContextCompat.getDrawable(this, R.drawable.star_sharp_svgrepo_com)
            drawable?.setTint(ContextCompat.getColor(this, R.color.lynx_white))
            addFavourite.setImageDrawable(drawable)
        } else {
            val drawable = ContextCompat.getDrawable(this, R.drawable.baseline_star_24)
            drawable?.setTint(ContextCompat.getColor(this, R.color.rise_n_shine))
            addFavourite.setImageDrawable(drawable)
        }


        addFavourite.setOnClickListener { view ->
            if(stop.stopId in favouriteStops) {
                val drawable = ContextCompat.getDrawable(this, R.drawable.star_sharp_svgrepo_com)
                drawable?.setTint(ContextCompat.getColor(this, R.color.lynx_white))
                addFavourite.setImageDrawable(drawable)
                favouriteStops.remove(stop.stopId)
                writeFavouriteStopIds(this, "FavouriteStops", "FavouriteStops")
            } else {
                val drawable = ContextCompat.getDrawable(this, R.drawable.baseline_star_24)
                drawable?.setTint(ContextCompat.getColor(this, R.color.rise_n_shine))
                addFavourite.setImageDrawable(drawable)
                favouriteStops += stop.stopId
                writeFavouriteStopIds(this, "FavouriteStops", "FavouriteStops")
            }
            Log.d("FavouriteStops", favouriteStops.toString())

        }
        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Odjazdy".uppercase()))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Rozkłady".uppercase()))

        binding.viewpager2.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewpager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.close.setOnClickListener { finish() }


    }

    fun readFavouriteStopIds(
        context: Context,
        preferencesName: String,
        key: String
    ): MutableList<String> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

        val stopIdsString: String? = sharedPreferences.getString(key, null)

        return if (!stopIdsString.isNullOrEmpty()) {
            stopIdsString.split(",").map { it.trim() }.toMutableList()
        } else {
            emptyList<String>().toMutableList()
        }
    }

    fun writeFavouriteStopIds(context: Context, preferencesName: String, key: String) {
        val stopIdsString = this.favouriteStops.joinToString(",")
        Log.d("FavouriteStops", stopIdsString)
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, stopIdsString)

        editor.apply()

        favouriteStops = readFavouriteStopIds(context, "FavouriteStops", "FavouriteStops")
    }
}

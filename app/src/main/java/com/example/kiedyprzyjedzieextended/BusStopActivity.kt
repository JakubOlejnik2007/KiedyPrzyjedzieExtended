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
import androidx.viewpager2.widget.ViewPager2
import com.example.kiedyprzyjedzieextended.databinding.ActivityBusStopBinding
import com.example.kiedyprzyjedzieextended.types.Stop
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson

class BusStopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusStopBinding
    private lateinit var adapter: FragmentPageAdapter
    lateinit var stop: Stop
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedData = intent.getSerializableExtra("stopJSON") as String

        stop = Gson().fromJson(sharedData, Stop::class.java)

        binding = ActivityBusStopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stopName.text = stop.stopName
        binding.stopNumber.text = stop.stopNumber.toString()


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

        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.close.setOnClickListener {finish()}
    }
}
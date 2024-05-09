package com.example.kiedyprzyjedzieextended

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kiedyprzyjedzieextended.ui.departures.DeparturesFragment
import com.example.kiedyprzyjedzieextended.ui.timetable.TimetableFragment

class FragmentPageAdapter(FragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(FragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position==0) DeparturesFragment() else TimetableFragment()
    }
}
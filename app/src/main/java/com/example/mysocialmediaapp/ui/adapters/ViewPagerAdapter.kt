package com.example.mysocialmediaapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysocialmediaapp.ui.fragments.RequestFragment
import com.example.mysocialmediaapp.ui.fragments.notificationfragments.TabNotificationFragment

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> TabNotificationFragment()
            1 -> RequestFragment()
            else -> TabNotificationFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        title = if (position == 0){
            "NOTIFICATION"
        } else {
            "REQUEST"
        }
        return title
    }
}
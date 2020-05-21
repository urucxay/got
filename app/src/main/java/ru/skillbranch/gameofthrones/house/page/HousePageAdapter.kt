package ru.skillbranch.gameofthrones.house.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.house.HouseFragment

class HousePageAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = HouseFragment(AppConfig.HOUSES[position])

    override fun getCount(): Int = AppConfig.HOUSES.size

    override fun getPageTitle(position: Int): CharSequence? = AppConfig.HOUSES[position]
}
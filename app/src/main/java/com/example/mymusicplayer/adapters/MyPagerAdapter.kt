package com.example.mymusicplayer.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mymusicplayer.fragments.PagerFragment
import com.example.mymusicplayer.models.PageData

class MyPagerAdapter (private val list: List<PageData>, fm: FragmentManager):
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PagerFragment.newInstance(list[0])
            }
            1 -> {
                PagerFragment.newInstance(list[1])
            }
            2 -> {
                PagerFragment.newInstance(list[2])
            }
            3 -> {
                PagerFragment.newInstance(list[3])
            }
            else -> PagerFragment()
        }
    }
}
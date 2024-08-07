package com.example.githubuser.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.follow.FollowFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var username: String = ""
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment  = FollowFragment.newInstance(position + 1, username)
}
package com.github.nukc.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class ViewPager2Activity : AppCompatActivity() {

    private val animator = ViewPager2.PageTransformer { page, position ->
        val absPos = abs(position)
        page.apply {
            rotation = position * 360
            translationY = absPos * 500f
            translationX = absPos * 350f
            val scale = if (absPos > 1) 0F else 1 - absPos
            scaleX = scale
            scaleY = scale
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        viewPager.setPageTransformer(animator)
        viewPager.adapter = PagerAdapter(this)
        TabLayoutMediator(findViewById(R.id.tab_layout), viewPager) { tab, position ->
            tab.text = "$position"
        }.attach()
    }

    private class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return InjectFragment()
        }
    }
}

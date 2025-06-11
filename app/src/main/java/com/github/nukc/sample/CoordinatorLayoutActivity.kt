package com.github.nukc.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.nukc.stateview.StateView


class CoordinatorLayoutActivity : AppCompatActivity() {

    private lateinit var stateView: StateView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        stateView = StateView.inject(this)
        (stateView.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                resources.getDimensionPixelSize(R.dimen.action_bar_size)

        stateView.onRetryClickListener = object : StateView.OnRetryClickListener {
            override fun onRetryClick() {
                stateView.showContent()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_inject, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_empty -> stateView.showEmpty()
            R.id.show_retry -> stateView.showRetry()
            R.id.show_loading -> stateView.showLoading()
            R.id.show_content -> stateView.showContent()
        }
        return super.onOptionsItemSelected(item)
    }
}

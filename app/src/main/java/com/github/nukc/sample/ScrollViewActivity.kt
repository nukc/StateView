package com.github.nukc.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.github.nukc.stateview.StateView

class ScrollViewActivity : AppCompatActivity() {

    private lateinit var stateView: StateView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)

        stateView = StateView.inject(findViewById<ScrollView>(R.id.scroll_view))
        stateView.onRetryClickListener = object : StateView.OnRetryClickListener {
            override fun onRetryClick() {

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

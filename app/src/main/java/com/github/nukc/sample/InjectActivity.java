package com.github.nukc.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nukc.stateview.StateView;
import com.github.nukc.stateview.animations.SlideAnimatorProvider;

public class InjectActivity extends AppCompatActivity {

    private static final String TAG = InjectActivity.class.getSimpleName();

    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        mStateView = StateView.inject(this);
        mStateView.setAnimatorProvider(new SlideAnimatorProvider());
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                //do something
                mStateView.showRetry();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_inject, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.show_empty) {
            mStateView.showEmpty();
        } else if (itemId == R.id.show_retry) {
            mStateView.showRetry();
        } else if (itemId == R.id.show_loading) {
            mStateView.showLoading();
        } else if (itemId == R.id.show_content) {
            mStateView.showContent();
        }
        return super.onOptionsItemSelected(item);
    }
}

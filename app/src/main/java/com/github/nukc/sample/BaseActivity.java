package com.github.nukc.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nukc.stateview.AnimatorProvider;
import com.github.nukc.stateview.StateView;

/**
 * @author Nukc.
 */

public abstract class BaseActivity extends AppCompatActivity {

	private StateView mStateView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setContentView());
		mStateView = StateView.inject(injectTarget());

		mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
			@Override
			public void onRetryClick() {
				//do something
				Toast.makeText(BaseActivity.this, "onRetryClick", Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected abstract @LayoutRes
	int setContentView();

	protected abstract View injectTarget();

	protected void setAnimator(AnimatorProvider animator) {
		if (mStateView != null) {
			mStateView.setAnimatorProvider(animator);
		}
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

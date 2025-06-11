package com.github.nukc.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nukc.stateview.StateView;
import com.github.nukc.stateview.animations.SlideAnimatorProvider;

public class SetViewActivity extends AppCompatActivity {

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
				mStateView.showContent();
				Toast.makeText(SetViewActivity.this, "onRetryClick", Toast.LENGTH_SHORT).show();
			}
		});

		View emptyView = View.inflate(this, R.layout.view_empty, null);
		TextView tvMessage = emptyView.findViewById(R.id.tv_message);
		if (tvMessage != null) {
			tvMessage.setText("Run setEmptyView");
		}
		mStateView.setView(mStateView.getEmptyResource(), emptyView);

		mStateView.setRetryResource(R.layout.view_retry31);

		TextView tvCustom = new TextView(this);
		tvCustom.setText("Custom");
		mStateView.setView(1, tvCustom);
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
		} else if (itemId == R.id.show_custom) {
			mStateView.show(1);
		}
		return super.onOptionsItemSelected(item);
	}
}

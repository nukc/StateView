package com.github.nukc.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nukc.stateview.StateView;

public class DefaultActivity extends AppCompatActivity implements View.OnClickListener {

    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        mStateView = (StateView) findViewById(R.id.stateView);
        findViewById(R.id.btnInEmpty).setOnClickListener(this);
        findViewById(R.id.btnInRetry).setOnClickListener(this);
        findViewById(R.id.btnRemove).setOnClickListener(this);

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                Toast.makeText(DefaultActivity.this, "retry click", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DefaultActivity.this, "can click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int itemId = v.getId();
        if (itemId == R.id.btnInEmpty) {
            mStateView.showEmpty();
        } else if (itemId == R.id.btnInRetry) {
            mStateView.showRetry();
        } else if (itemId == R.id.btnRemove) {
            mStateView.showContent();
        }
    }
}

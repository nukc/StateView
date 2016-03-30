package com.github.nukc.sample;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.github.nukc.stateview.StateView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateView = (StateView) findViewById(R.id.stateView);

        findViewById(R.id.btnInEmpty).setOnClickListener(this);
        findViewById(R.id.btnInRetry).setOnClickListener(this);
        findViewById(R.id.btnRemove).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInEmpty:
                mStateView.inflateEmpty();
                break;
            case R.id.btnInRetry:
                mStateView.inflateRetry().setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                break;
            case R.id.btnRemove:
                mStateView.showContent();
                break;
        }
    }
}

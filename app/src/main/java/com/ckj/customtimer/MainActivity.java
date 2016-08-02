package com.ckj.customtimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private CircleTimerView timerView;
    private CustomSeekBar seerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerView = (CircleTimerView) findViewById(R.id.timer_time);

        seerBar = (CustomSeekBar) findViewById(R.id.seekbar);
        seerBar.setScrollListener(new CustomSeekBar.OnScrollListener() {
            @Override
            public void onScroll(int degree) {
                timerView.setDegree(degree);
            }
        });
    }
}

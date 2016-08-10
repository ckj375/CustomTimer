package com.ckj.customtimer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private CircleTimerView timerView;
    private TextView timeTv;
    private Button startBtn;
    private Button cancelBtn;
    private int leftTime = 0;

    private TimerTask timerTask;
    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (leftTime > 0) {
                        leftTime--;
                    } else {
                        startBtn.setEnabled(true);
                        cancelBtn.setEnabled(false);
                        timer.cancel();
                        timerTask.cancel();
                    }
                    timeTv.setText(String.valueOf(leftTime));
                    timerView.setAngle(leftTime * 6);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerView = (CircleTimerView) findViewById(R.id.timer_time);
        timeTv = (TextView) findViewById(R.id.time);
        timeTv.setText(String.valueOf(leftTime));
        timerView.setScrollListener(new CircleTimerView.OnScrollListener() {
            @Override
            public void onScroll(int angle) {
                leftTime = angle / 6;
                timeTv.setText(String.valueOf(leftTime));
            }
        });

        startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftTime == 0) return;
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                };
                timer.scheduleAtFixedRate(timerTask, 0, 1000);
                startBtn.setEnabled(false);
                cancelBtn.setEnabled(true);
            }
        });
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setEnabled(false);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(true);
                cancelBtn.setEnabled(false);
                timer.cancel();
            }
        });

    }
}

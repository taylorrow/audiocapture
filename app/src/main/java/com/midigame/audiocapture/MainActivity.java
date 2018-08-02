package com.midigame.audiocapture;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.midigame.audiocapture.play.PlayTrack;
import com.midigame.audiocapture.record.RecordTrack;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button record, play;
    private TextView timerText;
    private String fileName = Environment.getExternalStorageDirectory() + "/test.pcm";
    private int timeSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_record:
                timerText.setVisibility(View.VISIBLE);
                record.setClickable(false);
                play.setClickable(false);
                setTimer();
                new RecordTrack().start();
                break;
            case R.id.button_play:
                File outFile = new File(fileName);
                if (outFile.exists()) {
                    timerText.setVisibility(View.INVISIBLE);
                    record.setClickable(false);
                    play.setClickable(false);
                    setTimer();
                    new PlayTrack().start();
                } else {
                    Toast.makeText(this, getText(R.string.not_file), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void initializeView() {
        timerText = findViewById(R.id.text_timer);
        record = findViewById(R.id.button_record);
        play = findViewById(R.id.button_play);
        record.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    private void setTimer() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeSeconds++;
                timerText.setText("" + timeSeconds);
                if (timeSeconds == 10) {
                    timeSeconds = 0;
                    record.setClickable(true);
                    play.setClickable(true);
                    timerText.setText(getText(R.string.timer_finish));
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }
}

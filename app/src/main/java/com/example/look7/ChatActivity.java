package com.example.look7;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity{
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 120000; // 2 minute

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);

            timerTextView = findViewById(R.id.timerTextView);

            startTimer();

            updateTimerText();
        }

        private void startTimer() {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                }
            }.start();

        }



        private void updateTimerText() {
            int minutes = (int) (timeLeftInMillis / 1000) / 60;
            int seconds = (int) (timeLeftInMillis / 1000) % 60;

            String timeFormatted = String.format("%02d:%02d", minutes, seconds);
            timerTextView.setText("Timer: " + timeFormatted);
        }
    }



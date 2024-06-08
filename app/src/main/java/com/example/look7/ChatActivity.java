package com.example.look7;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity{
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 120000; // 2 minute

    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
            messageList = new ArrayList<>();

            timerTextView = findViewById(R.id.timerTextView);

            startTimer();

            updateTimerText();

            recyclerView = findViewById(R.id.recycler_view);
            messageEditText = findViewById(R.id.message_edit_text);
            sendButton = findViewById(R.id.send_button);

            messageAdapter = new MessageAdapter(messageList);
            recyclerView.setAdapter(messageAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setStackFromEnd(true);
            recyclerView.setLayoutManager(llm);

            sendButton.setOnClickListener(v -> {
                String question = messageEditText.getText().toString().trim();
                addToChat(question,Message.SENT_BY_ME);
            });

        }

        void addToChat(String message, String sentBy){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageList.add(new Message(message, sentBy));
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            });

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



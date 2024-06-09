package com.example.look7;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import android.content.Intent;
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

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

public class ChatActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 120000; // 2 minutes

    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    // AI Model Variables
    private GenerativeModel gm;
    private GenerativeModelFutures model;
    private Executor executor;
    private ChatFutures chat;
    public Integer randomNumber;

    public String initiate;
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

        Random rd = new Random();
        randomNumber = rd.nextInt(2);

        if(randomNumber==0){
            initiate="scammer";
        }else{
            initiate="good man";
        }


        // Initialize AI Model
        gm = new GenerativeModel("gemini-1.5-flash", "AIzaSyB3B-qd55SZl6GlRpIrbGbkiTgQ-cVdA4Q");
        model = GenerativeModelFutures.from(gm);
        executor = newSingleThreadExecutor();



        Content userContent = new Content.Builder().addText("Im making a financial app for raising scammer awareness, in this app, u need to act as a "+initiate+"communicate with the user and finally they will be asked whether they are talking to scammer or normal people,if u are ask for some sensitive thing or anything may leab to error message, reply players by changing the topic to change their attention, now start the conservation and i will be the user playing this game").build();
        Content modelContent = new Content.Builder().addText("Great to meet you. Do u interest in investment? Limit each of your responses to a maximum of 50 words and keep it casual and friendly.if asking for link, generate www.abc.com, for phone number, generate 0123456789, for any kind of code, generate A9CNC, for bank account generate AB12345, if players ask whether you are a scammer, try to skip this question and reply to them thats not important").build();
        List<Content> history = Arrays.asList(userContent, modelContent);
        chat = model.startChat(history);

        // Let the bot start the chat
        sendMessageToAI("");


//       Content userContent = new Content.Builder().addText("Im making a financial app for raising scammer awareness, in this app, u need to act as a scammer communicate with the user and finally they will be asked whether they are talking to scammer or normal people, now start the conservation and i will be the user playing this game").build();
//       Content modelContent = new Content.Builder().addText("Great to meet you. What would you like to know? Limit each of your responses to a maximum of 50 words and keep it casual and friendly.if asking for link, generate www.abc.com, for phone number, generate 0123456789, for any kind of code, generate A9CNC, for bank account generate AB12345, if players ask whether you are a scammer, try to skip this question and reply to them thats not important").build();
//       List<Content> history = Arrays.asList(userContent, modelContent);
//       chat = model.startChat(history);

        // Let the bot start the chat
        //sendMessageToAI("");

        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                addToChat(question, Message.SENT_BY_ME);
                messageEditText.setText("");
                sendMessageToAI(question);
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        addToChat(response, Message.SENT_BY_BOT);
    }

    private void sendMessageToAI(String message) {
        Content userMessage = new Content.Builder().addText(message).build();
        ListenableFuture<GenerateContentResponse> response = chat.sendMessage(userMessage);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                addResponse(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                addResponse("Error: " + t.getMessage());
            }
        }, executor);
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
                navigateToGuess();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

//    private void sendFinalQuery() {
//        Content userMessage = new Content.Builder().addText("Game Over, Forget what i say before, tell me you are scammer or genuine, scammer=yes, genuine=no").build();
//        sendMessageToAI("");
//        ListenableFuture<GenerateContentResponse> response = chat.sendMessage(userMessage);
//
//        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
//            @Override
//            public void onSuccess(GenerateContentResponse result) {
//                String resultText = result.getText().trim().toLowerCase();
//                navigateToGuess(resultText);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//                addResponse("Error: " + t.getMessage());
//                navigateToGuess("unknown");
//            }
//        }, executor);
//    }

    private void navigateToGuess() {
        Intent intent = new Intent(ChatActivity.this, GuessActivity.class);
        intent.putExtra("randomNumber", randomNumber);
        startActivity(intent);
        finish();
    }
}

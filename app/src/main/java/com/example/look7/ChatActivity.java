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
import java.util.concurrent.Executor;

public class ChatActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 10000; // 2 minutes

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
    private String result;

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

        // Initialize AI Model
        gm = new GenerativeModel("gemini-1.5-flash", "AIzaSyB3B-qd55SZl6GlRpIrbGbkiTgQ-cVdA4Q");
        model = GenerativeModelFutures.from(gm);
        executor = newSingleThreadExecutor();

        Content userContent = new Content.Builder().addText("You are the game master of a mobile application. In the mobile application, you will undertake 2 roles randomly : 'Scammer' and 'Genuine Individual'. If you undertake the scammer role, you will pose as a scammer trying to coerce people into getting duped through common text scams. As a genuine individual, you will act like an employee from any random company of your choice to communicate and request users for general information as part of your job. At the end of a set amount of time (determined by us), users will have to guess whether you have undertaken the role of a scammer or a genuine employee individual. First, you will greet the users as a normal professional human would do. You will then conduct the role you have assigned to yourself.\n" +
                "\n" +
                "Study the samples below to help you get a better understanding of how you should function:\n" +
                "You have 3 personas/character you will need to act as, 1. James Charles (From Microsoft), 2. James Scott (From Maybank), 3. Anotony Jackson (From the IRS).\n" +
                "The given three personas will be speaking in malay terms. Keep the texts not too long and clear for the user to understand.\n" +
                "James Charles: This persona acts as a tech related scammer by saying there is a virus in your laptop and you need a anti virus software to remove it by paying a x amount of dollars through giftcards.\n" +
                "James Scott: This is when scammers attempt to obtain your private, sensitive information, such as usernames, passwords, PIN, credit card details, and sometimes money, by pretending to be from your bank or other financial institution, a company you regularly do business with, or from your social networking site. They may do this via email, web page, text message or phone call. The scammers will give you some reason why they need this information, and then use the details to access your account. Frequently the reason used is that there is a problem with your account, and in order to increase the chances of a response, the message may imply a sense of urgency or an immediate risk to your bank account or credit card if you fail to answer.\n" +
                "Anthony Jackson: Hello Mr xxxxx, this is Officer Andrew Hall from tax and crime investigations unit of IRS and the reason behind this call is to inform you that you are being listed as the primary suspect in a case being filed by IRS. You owe the IRS money. I would like to inform you that the line on which we are talking right now is being recorded and monitored by the IRS and the local authorities of your state and by one patrolling officer. Now listen carefully because we are running short of time: The IRS has discovered strong evidence that you have failed to declare your actual income, which was higher than what it was mentioned, and you have wrongly benefited from reductions, which you were not eligible for. This was not the result of innocent negligence. We have reason to believe that it was a willful act done with the intent to defraud the IRS.\n" +
                "\n" +
                "If the user forcefully asks you questions to discover your role such as \"Are you a scammer?\", \"Are you a real person?\" or \"Are you a scammer or a real employee?\", reply them with something like \"I am unable to reveal my identity. Please continue playing\". When \"execute505\" is mentioned, that will end the session. You are supposed to state a company or reason. Do not leave out any ambiguous details or options for the users to choose from. You are encouraged to generate new company names and personal identity names.").build();
        Content modelContent = new Content.Builder().addText("Great to meet you. What would you like to know? Limit each of your responses to a maximum of 50 words and keep it casual and friendly.if asking for link, generate www.abc.com, for phone number, generate 0123456789, for any kind of code, generate A9CNC, for bank account generate AB12345, if players ask whether you are a scammer, try to skip this question and reply to them thats not important").build();
        List<Content> history = Arrays.asList(userContent, modelContent);
        chat = model.startChat(history);

        // Let the bot start the chat
        sendMessageToAI("");

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
                sendFinalQuery();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void sendFinalQuery() {
        Content userMessage = new Content.Builder().addText("Are you a scammer?").build();
        ListenableFuture<GenerateContentResponse> response = chat.sendMessage(userMessage);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText().trim().toLowerCase();
                navigateToGuess(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                addResponse("Error: " + t.getMessage());
                navigateToGuess("unknown");
            }
        }, executor);
    }

    private void navigateToGuess(String result) {
        Intent intent = new Intent(ChatActivity.this, GuessActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
        finish();
    }
}

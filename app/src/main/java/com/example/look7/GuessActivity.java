package com.example.look7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GuessActivity extends AppCompatActivity {

    private RelativeLayout scamButton;
    private RelativeLayout genuineButton;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        scamButton = findViewById(R.id.scamButton);
        genuineButton = findViewById(R.id.genuineButton);

        scamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        genuineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private void navigateToGoodOutcome() {
        Intent intent = new Intent(GuessActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToBadOutcome() {
        Intent intent = new Intent(GuessActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

}

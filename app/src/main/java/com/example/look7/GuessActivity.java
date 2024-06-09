package com.example.look7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
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
    private String result;
    private MediaPlayer mediaPlayer;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);


        scamButton = findViewById(R.id.scamButton);
        genuineButton = findViewById(R.id.genuineButton);

        Intent intent = getIntent();
        Integer rd = (Integer) intent.getExtras().get("randomNumber");
        mediaPlayer = MediaPlayer.create(this, R.raw.sneak);
        mediaPlayer.start();



        scamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rd==0){

                    navigateToGoodOutcome();

                }else{

                    navigateToBadOutcome();

                }



            }
        });

        genuineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rd==1){

                    navigateToGoodOutcome();

                }else{

                    navigateToBadOutcome();

                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void navigateToGoodOutcome() {
        Intent intent = new Intent(GuessActivity.this, CorrectActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToBadOutcome() {
        Intent intent = new Intent(GuessActivity.this, WrongActivity.class);
        startActivity(intent);
        finish();
    }

}

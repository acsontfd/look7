package com.example.look7;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    public static final int TIMER = 1800;
    RelativeLayout beginButton;
    TextView buttonText;
    LottieAnimationView buttonAnimation;
    TextView description;
    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beginButton = findViewById(R.id.beginButton);
        buttonText= findViewById(R.id.buttonText);
        buttonAnimation = findViewById(R.id.button_Animation);

        mediaPlayer = MediaPlayer.create(this, R.raw.mainmenu);
        mediaPlayer.start();


        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonAnimation.setVisibility(View.VISIBLE);
                buttonAnimation.playAnimation();

                buttonText.setVisibility(View.GONE);
                beginButton.setVisibility(View.GONE);
                new Handler().postDelayed(this::resetButton, TIMER);

            }
            private void resetButton(){
                buttonAnimation.pauseAnimation();
                buttonAnimation.setVisibility(View.GONE);
                buttonText.setVisibility(View.VISIBLE);
                navigateToChat();

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
    private void navigateToChat() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}

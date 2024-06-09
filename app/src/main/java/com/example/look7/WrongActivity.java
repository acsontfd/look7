package com.example.look7;

import static androidx.core.content.ContextCompat.startActivity;

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

public class WrongActivity extends AppCompatActivity {
    RelativeLayout againButton;
    TextView anotherOne;
    LottieAnimationView wrongAnimation;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);

        againButton = findViewById(R.id.againButton);
        anotherOne = findViewById(R.id.anotherOne);
        wrongAnimation = findViewById(R.id.wrongAnimation);

        mediaPlayer = MediaPlayer.create(this, R.raw.lose);
        mediaPlayer.start();


        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Intent intent = new Intent(WrongActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.example.look7;

import android.content.Intent;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beginButton = findViewById(R.id.beginButton);
        buttonText= findViewById(R.id.buttonText);
        buttonAnimation = findViewById(R.id.button_Animation);

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
    private void navigateToChat() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}

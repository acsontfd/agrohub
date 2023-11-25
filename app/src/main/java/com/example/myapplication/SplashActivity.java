package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_BEFORE_ANIMATION = 2000;
    private static final long ANIMATION_DURATION = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView logo = findViewById(R.id.logo);
        final TextView punchlineTextView = findViewById(R.id.punchlineTextView);

        punchlineTextView.setVisibility(View.VISIBLE);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(ANIMATION_DURATION);

        punchlineTextView.startAnimation(fadeIn);

        // Create a delay before starting the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an AlphaAnimation for the fade-out effect with a duration of ANIMATION_DURATION
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(ANIMATION_DURATION);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Animation started
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Animation ended
                        logo.setVisibility(View.GONE); // Hide the logo
                        punchlineTextView.setVisibility(View.GONE); // Hide the punchline
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Animation repeated
                    }
                });
                punchlineTextView.startAnimation(fadeOut);
                logo.startAnimation(fadeOut);
            }
        }, DELAY_BEFORE_ANIMATION);
    }
}

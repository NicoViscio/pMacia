package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textBeenThere = findViewById(R.id.textBeenThere);
        ImageView logo = findViewById(R.id.image1);
        TextView textDoneThat = findViewById(R.id.textDoneThat);

        handler = new Handler();

        scheduleAnimation(textBeenThere, R.anim.fade_in, 500, true);
        scheduleAnimation(logo, R.anim.scale_up, 1500, false);
        scheduleAnimation(textDoneThat, R.anim.fade_in, 2500, true);

        handler.postDelayed(this::navigateToNextActivity, 5700);
    }

    // Logo animation
    private void scheduleAnimation(View view, int animationResId, int delay, boolean applyPulse) {
        handler.postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(this, animationResId);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);

            if (applyPulse) {
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation pulse = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pulse);
                        view.startAnimation(pulse);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, delay);
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

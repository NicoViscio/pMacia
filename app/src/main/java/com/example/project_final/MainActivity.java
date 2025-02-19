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

        // Inicializar vistas
        TextView textBeenThere = findViewById(R.id.textBeenThere);
        ImageView logo = findViewById(R.id.image1);
        TextView textDoneThat = findViewById(R.id.textDoneThat);

        // Inicializar Handler
        handler = new Handler();

        // Configurar animaciones
        scheduleAnimation(textBeenThere, R.anim.fade_in, 500, true);
        scheduleAnimation(logo, R.anim.scale_up, 1500, false);
        scheduleAnimation(textDoneThat, R.anim.fade_in, 2500, true);

        // Navegar a la siguiente actividad
        handler.postDelayed(this::navigateToNextActivity, 5700);
    }

    /**
     * Método para programar una animación en una vista con un retraso opcional.
     *
     * @param view La vista a animar.
     * @param animationResId El recurso de animación.
     * @param delay Tiempo de retraso antes de iniciar la animación (en milisegundos).
     * @param applyPulse Si debe aplicar una animación de "pulso" después de la animación inicial.
     */

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

    /**
     * Navega a la siguiente actividad.
     */

    private void navigateToNextActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverDosmil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosmil_game_over);

        // Obtener el puntaje
        int score = getIntent().getIntExtra("SCORE", 0);

        // Guardar el puntaje en la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertDosmilScore(score);

        TextView scoreText = findViewById(R.id.scoreText);
        Button restartButton = findViewById(R.id.restartButton);
        Button gobackButton = findViewById(R.id.goBack);

        scoreText.setText("Score: " + score);

        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DosmilActivity.class);
            startActivity(intent);
            finish();
        });

        gobackButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameSelectionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
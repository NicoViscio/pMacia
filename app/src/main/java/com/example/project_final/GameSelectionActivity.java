package com.example.project_final;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        LinearLayout gameOption1 = findViewById(R.id.gameOption1);
        LinearLayout gameOption2 = findViewById(R.id.gameOption2);

        gameOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar la lógica para iniciar el juego 1
                Toast.makeText(GameSelectionActivity.this, "Starting Game 1", Toast.LENGTH_SHORT).show();
            }
        });

        gameOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar la lógica para iniciar el juego 2
                Toast.makeText(GameSelectionActivity.this, "Starting Game 2", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
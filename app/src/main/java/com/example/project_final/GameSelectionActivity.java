package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        LinearLayout gameOption1 = findViewById(R.id.gameOption1);
        LinearLayout gameOption2 = findViewById(R.id.gameOption2);
        Button btnReturn = findViewById(R.id.atras);

        gameOption1.setOnClickListener(v -> {
            Intent intent = new Intent(GameSelectionActivity.this, CandyCrushActivity.class);
            startActivity(intent);
        });

        gameOption2.setOnClickListener(v -> {
            Intent intent = new Intent(GameSelectionActivity.this, DosmilActivity.class);
            startActivity(intent);
        });

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(GameSelectionActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }

}
package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnSelectGame = findViewById(R.id.btnSelectGame);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        Button btnExitGame = findViewById(R.id.btnExitGame);

        btnSelectGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameSelectionActivity.class);
                startActivity(intent);
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        btnExitGame.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });
    }
}
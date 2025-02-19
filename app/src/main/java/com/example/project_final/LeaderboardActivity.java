package com.example.project_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TableLayout candyTable;
    private TableLayout dosmilTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        dbHelper = new DatabaseHelper(this);
        candyTable = findViewById(R.id.candyTable);
        dosmilTable = findViewById(R.id.dosmilTable);

        // Configurar el botón de retorno
        Button btnReturn = findViewById(R.id.atras);
        btnReturn.setOnClickListener(v -> {
            finish(); // Cierra esta actividad y vuelve a la anterior
        });

        // Cargar puntajes
        loadScores();
    }

    private void loadScores() {
        // Cargar puntajes de Candy Crush
        List<DatabaseHelper.ScoreEntry> candyScores = dbHelper.getCandyScores();
        for (DatabaseHelper.ScoreEntry score : candyScores) {
            addRowToTable(candyTable, score);
        }

        // Cargar puntajes de 2048
        List<DatabaseHelper.ScoreEntry> dosmilScores = dbHelper.getDosmilScores();
        for (DatabaseHelper.ScoreEntry score : dosmilScores) {
            addRowToTable(dosmilTable, score);
        }
    }

    private void addRowToTable(TableLayout table, DatabaseHelper.ScoreEntry score) {
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow row = (TableRow) inflater.inflate(R.layout.leaderboard_row, table, false);

        TextView gameNumberView = row.findViewById(R.id.gameNumber);
        TextView dateView = row.findViewById(R.id.date);
        TextView scoreView = row.findViewById(R.id.score);

        gameNumberView.setText("Game " + score.gameNumber);
        dateView.setText(score.date);
        scoreView.setText(String.valueOf(score.score));

        table.addView(row);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
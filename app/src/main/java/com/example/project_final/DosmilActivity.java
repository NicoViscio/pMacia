package com.example.project_final;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class DosmilActivity extends AppCompatActivity {
    private static final int GRID_SIZE = 4;
    private TextView[][] cells;
    private int[][] values;
    private TextView scoreView;
    private int score = 0;
    private float startX, startY;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosmil_main);

        scoreView = findViewById(R.id.score);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        cells = new TextView[GRID_SIZE][GRID_SIZE];
        values = new int[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                TextView cell = new TextView(this);
                cell.setTextSize(32);
                cell.setGravity(Gravity.CENTER);
                cell.setBackground(getResources().getDrawable(R.drawable.cell_background_c, null));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                params.setMargins(8, 8, 8, 8);
                cell.setLayoutParams(params);
                gridLayout.addView(cell);
                cells[i][j] = cell;
                values[i][j] = 0;
            }
        }

        spawnRandomTile();
        spawnRandomTile();
        updateGrid();

        Button menuButton = findViewById(R.id.menuButton);
        Button restartButton = findViewById(R.id.restartButton);

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameSelectionActivity.class);
            startActivity(intent);
            finish();
        });

        restartButton.setOnClickListener(v -> {
            recreate(); // Esto volverá a crear la actividad desde cero
        });

        Button endGameButton = findViewById(R.id.endGameButton);

        endGameButton.setOnClickListener(v -> {
            // Ir a la pantalla de Game Over con la puntuación actual
            Intent intent = new Intent(this, GameOverDosmil.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        });
    }

    private void resetGame() {
        // Reiniciar variables
        score = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                values[i][j] = 0;
            }
        }
        spawnRandomTile();
        spawnRandomTile();
        updateGrid();
    }

    private boolean isGameOver() {
        // Verificar si hay celdas vacías
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (values[i][j] == 0) return false;
            }
        }

        // Verificar si hay movimientos posibles horizontalmente
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE - 1; j++) {
                if (values[i][j] == values[i][j + 1]) return false;
            }
        }

        // Verificar si hay movimientos posibles verticalmente
        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 0; i < GRID_SIZE - 1; i++) {
                if (values[i][j] == values[i + 1][j]) return false;
            }
        }

        return true;
    }

    private void checkGameOver() {
        if (isGameOver()) {
            Intent intent = new Intent(this, GameOverDosmil.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        }
    }

    private void spawnRandomTile() {
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (values[i][j] == 0) emptyCells.add(new int[]{i, j});
            }
        }
        if (!emptyCells.isEmpty()) {
            int[] pos = emptyCells.get(new Random().nextInt(emptyCells.size()));
            values[pos[0]][pos[1]] = new Random().nextInt(10) == 0 ? 4 : 2;

            animateNewTile(pos[0], pos[1]);
        }
    }

    private void updateGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int value = values[i][j];
                cells[i][j].setText(value == 0 ? "" : String.valueOf(value));
                cells[i][j].setBackgroundColor(getTileColor(value));
            }
        }
        scoreView.setText("Score: " + score);
    }

    private int getTileColor(int value) {
        switch (value) {
            case 2: return 0xFFEEE4DA;
            case 4: return 0xFFEDE0C8;
            case 8: return 0xFFF2B179;
            case 16: return 0xFFF59563;
            case 32: return 0xFFF67C5F;
            case 64: return 0xFFF65E3B;
            case 128: return 0xFFEDCF72;
            case 256: return 0xFFEDCC61;
            case 512: return 0xFFEDC850;
            case 1024: return 0xFFEDC53F;
            case 2048: return 0xFFEDC22E;
            default: return 0xFFCDC1B4;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float endY = event.getY();
                float deltaX = endX - startX;
                float deltaY = endY - startY;

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX > 0) moveRight();
                    else moveLeft();
                } else {
                    if (deltaY > 0) moveDown();
                    else moveUp();
                }

                spawnRandomTile();
                updateGrid();
                animateGridChanges();
                checkGameOver(); // Añadir verificación de game over después de cada movimiento
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void moveLeft() {
        for (int i = 0; i < GRID_SIZE; i++) {
            int[] newRow = new int[GRID_SIZE];
            int index = 0;
            for (int j = 0; j < GRID_SIZE; j++) {
                if (values[i][j] != 0) {
                    if (index > 0 && newRow[index - 1] == values[i][j]) {
                        newRow[index - 1] *= 2;
                        score += newRow[index - 1];
                    } else {
                        newRow[index] = values[i][j];
                        index++;
                    }
                }
            }
            System.arraycopy(newRow, 0, values[i], 0, GRID_SIZE);
        }
    }

    private void moveRight() {
        for (int i = 0; i < GRID_SIZE; i++) {
            int[] newRow = new int[GRID_SIZE];
            int index = GRID_SIZE - 1;
            for (int j = GRID_SIZE - 1; j >= 0; j--) {
                if (values[i][j] != 0) {
                    if (index < GRID_SIZE - 1 && newRow[index + 1] == values[i][j]) {
                        newRow[index + 1] *= 2;
                        score += newRow[index + 1];
                    } else {
                        newRow[index] = values[i][j];
                        index--;
                    }
                }
            }
            System.arraycopy(newRow, 0, values[i], 0, GRID_SIZE);
        }
    }

    private void moveUp() {
        for (int j = 0; j < GRID_SIZE; j++) {
            int[] newCol = new int[GRID_SIZE];
            int index = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                if (values[i][j] != 0) {
                    if (index > 0 && newCol[index - 1] == values[i][j]) {
                        newCol[index - 1] *= 2;
                        score += newCol[index - 1];
                    } else {
                        newCol[index] = values[i][j];
                        index++;
                    }
                }
            }
            for (int i = 0; i < GRID_SIZE; i++) values[i][j] = newCol[i];
        }
    }

    private void moveDown() {
        for (int j = 0; j < GRID_SIZE; j++) {
            int[] newCol = new int[GRID_SIZE];
            int index = GRID_SIZE - 1;
            for (int i = GRID_SIZE - 1; i >= 0; i--) {
                if (values[i][j] != 0) {
                    if (index < GRID_SIZE - 1 && newCol[index + 1] == values[i][j]) {
                        newCol[index + 1] *= 2;
                        score += newCol[index + 1];
                    } else {
                        newCol[index] = values[i][j];
                        index--;
                    }
                }
            }
            for (int i = 0; i < GRID_SIZE; i++) values[i][j] = newCol[i];
        }
    }

    private void animateGridChanges() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                TextView cell = cells[i][j];
                ObjectAnimator translationX = ObjectAnimator.ofFloat(cell, "translationX", cell.getTranslationX(), 0);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(cell, "translationY", cell.getTranslationY(), 0);
                translationX.setInterpolator(new AccelerateDecelerateInterpolator());
                translationY.setInterpolator(new AccelerateDecelerateInterpolator());
                translationX.setDuration(200);
                translationY.setDuration(200);
                translationX.start();
                translationY.start();
            }
        }
    }

    private void animateNewTile(int row, int col) {
        TextView cell = cells[row][col];
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cell, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cell, "scaleY", 0f, 1f);
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleY.setInterpolator(new AccelerateInterpolator());
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
}
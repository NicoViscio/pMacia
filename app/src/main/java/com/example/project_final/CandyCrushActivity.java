package com.example.project_final;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CandyCrushActivity extends AppCompatActivity {
    private ImageView selectedCell = null;
    private int selectedCellIndex = -1;
    private ImageView[] gridCells; // Array para almacenar las celdas
    private int[] pieceDrawables = {
            R.drawable.c1,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5
    };
    private int gridSize = 5;
    private int score = 0; // Variable para almacenar el puntaje
    private int movesRemaining = 15; // Número de movimientos permitidos inicialmente
    private TextView scoreTextView; // TextView para mostrar el puntaje
    private TextView movesTextView; // TextView para mostrar movimientos restantes
    private VideoView videoView; // VideoView para el fondo de video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candy_main);

        scoreTextView = findViewById(R.id.scoreTextView); // Referencia al TextView de puntaje
        movesTextView = findViewById(R.id.movesTextView); // Referencia al TextView de movimientos restantes

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridCells = new ImageView[gridSize * gridSize];

        Random random = new Random();

        // Crear celdas con piezas aleatorias
        for (int i = 0; i < gridSize * gridSize; i++) {
            ImageView cell = new ImageView(this);

            // Crear LayoutParams con margen
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i / gridSize),
                    GridLayout.spec(i % gridSize)
            );

            // Tamaño de cada celda (ajustado para márgenes)
            int cellSize = getResources().getDisplayMetrics().widthPixels / gridSize - 40;
            params.width = cellSize;
            params.height = cellSize;
            params.setMargins(10, 10, 10, 10); // Margen entre celdas

            cell.setLayoutParams(params);

            // Fondo redondeado de la celda
            cell.setBackgroundResource(R.drawable.cell_background);

            // Asignar una pieza aleatoria como imagen encima de la celda
            int randomPiece = pieceDrawables[random.nextInt(pieceDrawables.length)];
            cell.setImageResource(randomPiece);
            cell.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Ajustar imagen dentro de la celda

            // Establecer la acción de clic para intercambiar las piezas
            int finalI = i;
            cell.setOnClickListener(v -> {
                // Si no hay movimientos restantes, no hacer nada
                if (movesRemaining <= 0) {
                    return;
                }

                // Si no hay celda seleccionada, seleccionar la actual
                if (selectedCell == null) {
                    selectedCell = (ImageView) v;
                    selectedCellIndex = finalI;
                    selectedCell.setAlpha(0.7f); // Indicar que está seleccionada (opacidad)
                } else {
                    // Verificar si las celdas son adyacentes
                    if (selectedCell != v && areAdjacent(selectedCellIndex, finalI, gridSize)) {
                        // Intercambiar las piezas si son adyacentes
                        ImageView currentCell = (ImageView) v;

                        // Intercambiar las imágenes
                        int selectedPieceRes = (Integer) selectedCell.getTag();
                        int currentPieceRes = (Integer) currentCell.getTag();

                        selectedCell.setImageResource(currentPieceRes);
                        currentCell.setImageResource(selectedPieceRes);

                        // Mantener el estado del tag con la nueva pieza
                        selectedCell.setTag(currentPieceRes);
                        currentCell.setTag(selectedPieceRes);

                        // Restaurar la opacidad de la celda seleccionada
                        selectedCell.setAlpha(1f);

                        // Limpiar la celda seleccionada
                        selectedCell = null;
                        selectedCellIndex = -1;


                        // Reducir movimientos restantes y actualizar el TextView
                        movesRemaining--;
                        updateMovesText();

                        // Comprobar si hay coincidencias
                        checkAndHandleMatches();
                    } else {
                        // Si no son adyacentes, mostrar mensaje
                    }
                }
            });

            // Establecer el recurso de la pieza como tag (para intercambiar más fácilmente)
            cell.setTag(randomPiece);

            // Guardar la referencia de la celda
            gridCells[i] = cell;

            // Añadir celda al GridLayout
            gridLayout.addView(cell);
        }

        // Actualizar el TextView de movimientos al iniciar
        updateMovesText();
    }

    private void updateMovesText() {
        movesTextView.setText("Movimientos restantes: " + movesRemaining);

        // Si no hay movimientos restantes, iniciar GameOverActivity
        if (movesRemaining == 0) {
            Intent gameOverIntent = new Intent(CandyCrushActivity.this, GameOverCandyActivity.class);
            gameOverIntent.putExtra("SCORE", score); // Pasar el puntaje actual
            startActivity(gameOverIntent);
            finish(); // Finalizar MainActivity para evitar que el usuario vuelva atrás
        }
    }

    // Método para verificar si dos celdas son adyacentes (horizontal o vertical)
    private boolean areAdjacent(int index1, int index2, int gridSize) {
        int row1 = index1 / gridSize;
        int col1 = index1 % gridSize;
        int row2 = index2 / gridSize;
        int col2 = index2 % gridSize;

        // Celdas adyacentes si están en la misma fila o columna
        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    // Método para comprobar y manejar coincidencias (sin cambios)
    private void checkAndHandleMatches() {
        List<Integer> cellsToRemove = new ArrayList<>();
        int pointsEarned = 0;

        // Comprobar filas para coincidencias horizontales
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize - 2; col++) {
                int index1 = row * gridSize + col;
                int index2 = row * gridSize + col + 1;
                int index3 = row * gridSize + col + 2;

                if (gridCells[index1].getTag().equals(gridCells[index2].getTag()) && gridCells[index1].getTag().equals(gridCells[index3].getTag())) {
                    cellsToRemove.add(index1);
                    cellsToRemove.add(index2);
                    cellsToRemove.add(index3);

                    pointsEarned += 500;
                }
            }
        }

        // Comprobar columnas para coincidencias verticales
        for (int col = 0; col < gridSize; col++) {
            for (int row = 0; row < gridSize - 2; row++) {
                int index1 = row * gridSize + col;
                int index2 = (row + 1) * gridSize + col;
                int index3 = (row + 2) * gridSize + col;

                if (gridCells[index1].getTag().equals(gridCells[index2].getTag()) && gridCells[index1].getTag().equals(gridCells[index3].getTag())) {
                    cellsToRemove.add(index1);
                    cellsToRemove.add(index2);
                    cellsToRemove.add(index3);

                    pointsEarned += 500;
                }
            }
        }

        // Eliminar celdas y generar nuevas piezas
        for (int index : cellsToRemove) {
            gridCells[index].setVisibility(View.INVISIBLE);
        }
        for (int index : cellsToRemove) {
            Random random = new Random();
            int newPiece = pieceDrawables[random.nextInt(pieceDrawables.length)];
            gridCells[index].setImageResource(newPiece);
            gridCells[index].setVisibility(View.VISIBLE);
            gridCells[index].setTag(newPiece);

            animatePieceAppearance(gridCells[index]);
        }

        score += pointsEarned;
        scoreTextView.setText("Puntos: " + score);
    }

    private void animatePieceAppearance(ImageView cell) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cell, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cell, "scaleY", 0f, 1f);

        scaleX.setDuration(300);
        scaleY.setDuration(300);

        scaleX.start();
        scaleY.start();
    }
}
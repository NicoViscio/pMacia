package com.example.project_final;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CandyCrushActivity extends AppCompatActivity {
    private ImageView selectedCell = null;
    private int selectedCellIndex = -1;
    private ImageView[] gridCells; // Array to store grid cells
    private int[] pieceDrawables = {
            R.drawable.c1,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5
    };
    private int gridSize = 5;
    private int score = 0; // Variable to store the score
    private int movesRemaining = 15; // Initial allowed moves
    private TextView scoreTextView; // TextView to display the score
    private TextView movesTextView; // TextView to display remaining moves

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candy_main);
        Button btnReturn = findViewById(R.id.atras);

        scoreTextView = findViewById(R.id.scoreTextView); // Reference to the score TextView
        movesTextView = findViewById(R.id.movesTextView); // Reference to the moves TextView


        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridCells = new ImageView[gridSize * gridSize];

        Random random = new Random();

        // Create cells with random pieces
        for (int i = 0; i < gridSize * gridSize; i++) {
            ImageView cell = new ImageView(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i / gridSize),
                    GridLayout.spec(i % gridSize)
            );

            // Set cell size
            int cellSize = getResources().getDisplayMetrics().widthPixels / gridSize - 40;
            params.width = cellSize;
            params.height = cellSize;
            params.setMargins(10, 10, 10, 10); // Margin between cells

            cell.setLayoutParams(params);

            cell.setBackgroundResource(R.drawable.cell_background);

            // Assign a random piece as the image on top of the cell
            int randomPiece = pieceDrawables[random.nextInt(pieceDrawables.length)];
            cell.setImageResource(randomPiece);
            cell.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            // Click action to swap pieces
            int finalI = i;
            cell.setOnClickListener(v -> {
                if (movesRemaining <= 0) {
                    return;
                }

                // If no cell is selected, select the current one
                if (selectedCell == null) {
                    selectedCell = (ImageView) v;
                    selectedCellIndex = finalI;
                    selectedCell.setAlpha(0.7f);
                } else {
                    // Check if cells are adjacent
                    if (selectedCell != v && areAdjacent(selectedCellIndex, finalI, gridSize)) {
                        // Swap pieces if adjacent
                        ImageView currentCell = (ImageView) v;

                        // Swap images
                        int selectedPieceRes = (Integer) selectedCell.getTag();
                        int currentPieceRes = (Integer) currentCell.getTag();

                        selectedCell.setImageResource(currentPieceRes);
                        currentCell.setImageResource(selectedPieceRes);

                        // Maintain tag state with the new piece
                        selectedCell.setTag(currentPieceRes);
                        currentCell.setTag(selectedPieceRes);

                        // Restore opacity of the selected cell
                        selectedCell.setAlpha(1f);

                        // Clear the selected cell
                        selectedCell = null;
                        selectedCellIndex = -1;

                        // Decrease remaining moves and update the TextView
                        movesRemaining--;
                        updateMovesText();

                        checkAndHandleMatches();
                    }
                }
            });

            // Set the piece resource as a tag (for easier swapping)
            cell.setTag(randomPiece);

            // Store the cell reference
            gridCells[i] = cell;

            // Add cell to the GridLayout
            gridLayout.addView(cell);
        }
        // Update moves TextView on start
        updateMovesText();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandyCrushActivity.this, GameSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateMovesText() {
        movesTextView.setText("Moves remaining: " + movesRemaining);

        // If no moves are left, start GameOverActivity
        if (movesRemaining == 0) {
            Intent gameOverIntent = new Intent(CandyCrushActivity.this, GameOverCandyActivity.class);
            gameOverIntent.putExtra("SCORE", score);
            startActivity(gameOverIntent);
            finish();
        }
    }

    // Check if two cells are adjacent (horizontal or vertical)
    private boolean areAdjacent(int index1, int index2, int gridSize) {
        int row1 = index1 / gridSize;
        int col1 = index1 % gridSize;
        int row2 = index2 / gridSize;
        int col2 = index2 % gridSize;

        // Cells are adjacent if they are in the same row or column
        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    // Check and handle matches (no changes)
    private void checkAndHandleMatches() {
        List<Integer> cellsToRemove = new ArrayList<>();
        int pointsEarned = 0;

        // Check rows for horizontal matches
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

        // Check columns for vertical matches
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

        // Deletes cells and generate new pieces
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
        scoreTextView.setText("Points: " + score);
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
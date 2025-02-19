package com.example.project_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LeaderboardDB";
    private static final int DATABASE_VERSION = 1;

    // Tabla para Candy Crush
    private static final String TABLE_CANDY = "candy_scores";
    // Tabla para 2048
    private static final String TABLE_DOSMIL = "dosmil_scores";

    // Columnas comunes
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GAME_NUMBER = "game_number";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Candy Crush
        String createCandyTable = "CREATE TABLE " + TABLE_CANDY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GAME_NUMBER + " INTEGER,"
                + COLUMN_SCORE + " INTEGER,"
                + COLUMN_DATE + " TEXT)";

        // Crear tabla 2048
        String createDosmilTable = "CREATE TABLE " + TABLE_DOSMIL + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GAME_NUMBER + " INTEGER,"
                + COLUMN_SCORE + " INTEGER,"
                + COLUMN_DATE + " TEXT)";

        db.execSQL(createCandyTable);
        db.execSQL(createDosmilTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSMIL);
        onCreate(db);
    }

    // Insertar nuevo puntaje de Candy Crush
    public void insertCandyScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Obtener el siguiente número de juego
        int gameNumber = getNextGameNumber(TABLE_CANDY);

        values.put(COLUMN_GAME_NUMBER, gameNumber);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_DATE, getCurrentDate());

        db.insert(TABLE_CANDY, null, values);
        db.close();
    }

    // Insertar nuevo puntaje de 2048
    public void insertDosmilScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Obtener el siguiente número de juego
        int gameNumber = getNextGameNumber(TABLE_DOSMIL);

        values.put(COLUMN_GAME_NUMBER, gameNumber);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_DATE, getCurrentDate());

        db.insert(TABLE_DOSMIL, null, values);
        db.close();
    }

    // Obtener todos los puntajes de Candy Crush
    public List<ScoreEntry> getCandyScores() {
        return getScores(TABLE_CANDY);
    }

    // Obtener todos los puntajes de 2048
    public List<ScoreEntry> getDosmilScores() {
        return getScores(TABLE_DOSMIL);
    }

    private List<ScoreEntry> getScores(String tableName) {
        List<ScoreEntry> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName,
                new String[]{COLUMN_GAME_NUMBER, COLUMN_SCORE, COLUMN_DATE},
                null, null, null, null,
                COLUMN_GAME_NUMBER + " DESC", "10"); // Limitar a los últimos 10 juegos

        if (cursor.moveToFirst()) {
            do {
                ScoreEntry entry = new ScoreEntry(
                        cursor.getInt(0),    // game number
                        cursor.getInt(1),    // score
                        cursor.getString(2)   // date
                );
                scoreList.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }

    private int getNextGameNumber(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_GAME_NUMBER + ") FROM " + tableName, null);
        int nextNumber = 1;
        if (cursor.moveToFirst()) {
            nextNumber = cursor.getInt(0) + 1;
        }
        cursor.close();
        return nextNumber;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Clase para almacenar los datos de cada entrada
    public static class ScoreEntry {
        public int gameNumber;
        public int score;
        public String date;

        public ScoreEntry(int gameNumber, int score, String date) {
            this.gameNumber = gameNumber;
            this.score = score;
            this.date = date;
        }
    }
}
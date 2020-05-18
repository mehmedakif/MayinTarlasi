package com.koala.mayintarlasi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.koala.mayintarlasi.DBHelper.DIFFICULTY;
import static com.koala.mayintarlasi.DBHelper.SCORE;
import static com.koala.mayintarlasi.DBHelper.SCORE_TABLE;

class DBManager {

    private DBHelper dbHelper;
    private Context context;
    private static SQLiteDatabase database;

    DBManager(Context c) {
        context = c;
    }

    void open() throws SQLException
    {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        Log.i("DB","DATABASE ACILDI");
        dbHelper.onCreate(database);
    }

    void cont() throws SQLException
    {
        dbHelper = new DBHelper(context);
        database = dbHelper.getReadableDatabase();
        Log.i("DB","DATABASE DEVAM");
        dbHelper.onCreate(database);
    }

    void close()
    {
        Log.i("DB","DATABASE KAPANDI");
        dbHelper.close();
    }

    static void insertScore(int score, String difficulty)
    {
        ContentValues contentValue = new ContentValues();
        contentValue.put(SCORE, score);
        contentValue.put(DIFFICULTY, difficulty);
        database.insertWithOnConflict(SCORE_TABLE, null, contentValue,SQLiteDatabase.CONFLICT_REPLACE);
    }

    static Cursor fetchBest(String dif)
    {// Sorgunun sonundaki 1 kac taneyi getirecegimi secmeme yaradi.
        return database.rawQuery("SELECT " + SCORE + " FROM " + SCORE_TABLE + " WHERE "+DIFFICULTY+" = '"+ dif +"' ORDER BY score ASC LIMIT 1",null);
    }

}
package com.koala.mayintarlasi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
    // Table Name
    static final String SCORE_TABLE = "scoreTable";
    // Table columns
    private static final String SCOREID = "scoreID";
    static final String SCORE = "score";
    static final String DIFFICULTY = "difficulty";
    // Database Information
    private static final String DB_NAME = "MINESWEEP.DB";
    // database version
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_IMG = "CREATE TABLE IF NOT EXISTS " + SCORE_TABLE + " ("+ SCOREID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+SCORE+" INTEGER,"+DIFFICULTY+" TEXT)";


    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_IMG);
        Log.i("SCORE TABLE","CREATED !");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
        Log.i("TABLES"," DROPPED !");
        onCreate(db);
    }

    DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;


public class Scoreboard extends AppCompatActivity
{
    TextView text_best_easy;
    TextView text_best_medium;
    TextView text_best_hard;
    DBManager dbScore;
    Cursor cursor;
    String[] difficulty_array = new String[] {"easy","medium","hard"};
    int best;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        text_best_easy = findViewById(R.id.easy_score);
        text_best_medium = findViewById(R.id.medium_score);
        text_best_hard = findViewById(R.id.hard_score);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dbScore = new DBManager(Scoreboard.this);
        dbScore.open();


        for (String s : difficulty_array)
        {
            cursor = DBManager.fetchBest(s);
            cursor.moveToFirst();
            if ((cursor != null) && (cursor.getCount() > 0)) {

                try {
                    do {
                        best = cursor.getInt(0);
                        String bestString = String.valueOf(best);
                        if (s.equals("easy"))
                            {
                            text_best_easy.setText(best + " Saniye");
                            } else if (s.equals("medium")) {
                           text_best_medium.setText(best + " Saniye");
                        } else text_best_hard.setText(best + " Saniye");
                    } while (cursor.moveToNext());
                } finally {
                    cursor.close();

                }
            }
        }


        dbScore.close();
    }
}

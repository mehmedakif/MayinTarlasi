package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Objects;


public class LocalScoresActivity extends AppCompatActivity
{
    TextView text_best_easy;
    TextView text_best_medium;
    TextView text_best_hard;
    DBManager dbScore;
    Cursor cursor;
    String[] difficulty_array = new String[] {"easy","medium","hard"};
    String best;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        text_best_easy = findViewById(R.id.easy_score);
        text_best_medium = findViewById(R.id.medium_score);
        text_best_hard = findViewById(R.id.hard_score);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dbScore = new DBManager(LocalScoresActivity.this);
        dbScore.open();


        for (String s : difficulty_array)
        {
            cursor = DBManager.fetchBest(s);
            cursor.moveToFirst();
            if ((cursor != null) && (cursor.getCount() > 0)) {

                try {
                    do {
                        best = String.valueOf(cursor.getInt(0));
                        String string_second = getResources().getString(R.string.seconds);
                        if (s.equals("easy"))
                            {
                            text_best_easy.setText(new StringBuilder().append(best).append(" ").append(string_second).toString());
                            } else if (s.equals("medium")) {
                           text_best_medium.setText(new StringBuilder().append(best).append(" ").append(string_second).toString());
                        } else text_best_hard.setText(new StringBuilder().append(best).append(" ").append(string_second).toString());
                    } while (cursor.moveToNext());
                } finally {
                    cursor.close();

                }
            }
        }


        dbScore.close();
    }
}

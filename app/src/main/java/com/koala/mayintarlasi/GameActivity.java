package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class GameActivity extends AppCompatActivity {

    public int[][] map;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();
        Objects.requireNonNull(getSupportActionBar()).hide();
        TableLayout table = findViewById(R.id.tableLayout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int[] linear_map = getIntent().getIntArrayExtra("Map");
        int map_size = getIntent().getIntExtra("Size",8);
        final GameMap new_map = new GameMap(1);

        //This loop creates a game grid with given size(difficulty).
        int buttonID = 0;
        for (int i = 0; i < map_size; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    (width/map_size),
                    (width/map_size)
            );
            for (int j = 0; j < map_size; j++)
            {
                final Button tileButton = new Button(this);
                tileButton.setText("");
                tileButton.setId(buttonID);
                tileButton.setBackgroundResource(R.drawable.button_tile);
                final int finalI = i;
                final int finalJ = j;
                tileButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View paramV)
                    {
                        //TODO isAlreadyRevealed function.
                        if (new_map.mine_array_2d[finalI][finalJ]==1)
                        {
                            //TODO Game over alert will be added.
                            Toast.makeText(getApplicationContext(),
                                    "BOM!", Toast.LENGTH_LONG).show();
                        }
                        //TODO Adjency coltrol function.
                        //TODO Reveal cell function.

                    }
                });
                //This value makes tile color difference to create better game experience.
                if((i+j)%2==0)
                {
                    tileButton.getBackground().setAlpha(230);
                }
                tileButton.setLayoutParams(params);
                row.addView(tileButton);
                buttonID++;
            }
            buttonID++;
            table.addView(row);
        }
        //map = create_map(map_size,linear_map);
        //boolean [][] array_name = new boolean[getIntent().getIntExtra("Size",8)][getIntent().getIntExtra("Size",8)];
    }

    private int[][] create_map(int map_size, int[] linear_map)
    {
        int[][] internal_map = new int[map_size][map_size];
        int i = 0;
        for (int j = 0; j<map_size; j++)
        {
            for(int h = 0; h<map_size; h++)
            {
                internal_map[j][h] = linear_map[i];
                i++;
            }
        }
        return internal_map;
    }
}

package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    public int[][] map;
    public GridView game_grid;
    int[] logos = {R.drawable.custom_thumb};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Objects.requireNonNull(getSupportActionBar()).hide();

        int[] linear_map = getIntent().getIntArrayExtra("Map");
        int map_size = getIntent().getIntExtra("Size",8);
        map = create_map(map_size,linear_map);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);

        game_grid = findViewById(R.id.grid_game);
        game_grid.setNumColumns(8);
        game_grid.setAdapter(customAdapter);

        game_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Toast.makeText(getApplicationContext(),position, Toast.LENGTH_LONG).show();
            }
        });


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

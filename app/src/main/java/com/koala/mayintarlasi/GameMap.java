package com.koala.mayintarlasi;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Random;

public class GameMap
{

    public int[][] map;
    int map_size;
    int[] mine_array_1d;
    int[][] mine_array_2d;
    private int difficulty;
    private int tile_count;

/*Constructor of GameMap class. Gets difficulty.*/
    GameMap(int chosen_difficulty)
    {
        difficulty = chosen_difficulty;

        int mine_count;
        if(chosen_difficulty == 0)
        {
            map_size = 7;
            mine_count = 9;
        }
        else if (chosen_difficulty==1)
        {
            map_size = 9;
            mine_count = 16;

        }
        else
        {map_size = 11;
            mine_count = 25;
        }

        //Total number of tile assigned as square of map size.
        tile_count = map_size * map_size;
        mine_array_1d = new int[tile_count];

        /*Constructor calls set_mines after setting map size and mine count*/
        set_mines_to_1d_array(mine_count);


    }

    private void set_mines_to_1d_array(int mine_count)
    {
        Random random = new Random();
        //Mayinlarin oldugu tek boyutlu dizi 0'lar ile dolduruluyor.
        Arrays.fill(mine_array_1d, 0);
        //Toplam mayin sayisina ulaslincaya kadar mayin olmayan yerlere yeni mayin doseniyor.
        for(int i = 0; i< mine_count;)
        {
            int iterator = random.nextInt(tile_count);
            if (this.mine_array_1d[iterator] != 1)
            {
                this.mine_array_1d[iterator] = 1;
                i++;
            }
        }
        mines_1d_to_2d_convert(this.mine_array_1d);
    }
    private void mines_1d_to_2d_convert(int[] one_dimensional)
    {
        mine_array_2d = new int[map_size][map_size];
        tile_count=0;
        for(int i = 0; i < map_size-1; i++)
        {
            for(int j = 0; j < map_size-1; j++)
            {
                mine_array_2d[i][j] = one_dimensional[tile_count];
                tile_count++;
            }
            tile_count++;
        }
        Log.i("Map", Arrays.deepToString(mine_array_2d));
    }

    public int getDifficulty()
    {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        difficulty = difficulty;
    }
}

package com.koala.mayintarlasi;

import java.util.Arrays;
import java.util.Random;

class GameMap
{
    int map_size;
    private int[] mine_array_1d;
    int[][] mine_array_2d;
    int[][] revealedTiles;
    private int tile_count;

    GameMap(int chosen_difficulty)
    {

        int mine_count;
        if(chosen_difficulty == 0)
        {
            map_size = 7;
            mine_count = 5;
        }
        else if (chosen_difficulty==1)
        {
            map_size = 9;
            mine_count = 10;

        }
        else
        {map_size = 11;
            mine_count = 20;
        }

        //Total number of tile assigned as square of map size.
        tile_count = map_size * map_size;
        mine_array_1d = new int[tile_count];
        plantMinesTo1D(mine_count);
    }

    private void plantMinesTo1D(int mine_count)
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
        convert1dTo2d(this.mine_array_1d);
    }

    private void convert1dTo2d(int[] one_dimensional)
    {
        mine_array_2d = new int[map_size][map_size];
        revealedTiles = new int[map_size][map_size];
        tile_count=0;
        for(int i = 0; i < map_size-1; i++)
        {
            for(int j = 0; j < map_size-1; j++)
            {
                revealedTiles[i][j]=0;
                mine_array_2d[i][j] = one_dimensional[tile_count];
                tile_count++;
            }
            tile_count++;
        }
    }

}

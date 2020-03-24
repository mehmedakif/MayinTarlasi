package com.koala.mayintarlasi;

import java.util.Random;

public class GameMap
{
    public int[][] map;
    int map_size;
    int[] linear_map;
    private int difficulty;
    private int tile_count;
    private int mine_count;




    GameMap(int chosen_difficulty)
    {
        this.difficulty = chosen_difficulty;

        if(chosen_difficulty == 0)
        {
            this.map_size = 8;
            this.mine_count = 10;
        }
        else if (chosen_difficulty==1)
        {
            this.map_size = 10;
            this.mine_count = 20;

        }
        else
        {this.map_size = 12;
            this.mine_count = 30;
        }

        this.tile_count = this.map_size * this.map_size;
        this.linear_map = new int[this.tile_count];
        this.set_mines(this.tile_count,this.mine_count);

    }

    void set_mines(int map_size,int mine_count)
    {
        Random random = new Random();

        for (int i:this.linear_map)
        {
            linear_map[i]=0;
        }

        for(int i=0; i< mine_count;)
        {
            int iterator = random.nextInt(this.tile_count);
            if (this.linear_map[iterator] != 1)
            {
                this.linear_map[iterator] = 1;
                i++;
            }
        }


    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}

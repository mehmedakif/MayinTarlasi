package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    Context context;
    Dialog dialog;
    Button exit_yes;
    TableLayout table;
    GameMap new_game;
    int map_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();
        Objects.requireNonNull(getSupportActionBar()).hide();




        //Assing map size as intent value. Seekbar value is choosen by user.
        int difficulty = getIntent().getIntExtra("SeekbarStatus",8);
        //Assing map size as intent value. Seekbar value is choosen by user.
        new_game = new GameMap(difficulty);


        fill_game_grid();


    }

    public void fill_game_grid()
    {
        map_size = new_game.map_size;
        table = findViewById(R.id.tableLayout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        //This loop creates a game grid with given size(difficulty).
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
                tileButton.setId(i*map_size+j);
                tileButton.setBackgroundResource(R.drawable.button_tile);
                tileButton.setBackgroundColor(Color.argb(255,116,214,0));
                final int finalI = i;
                final int finalJ = j;
                tileButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View paramV)
                    {
                        if (new_game.revealedTiles[finalI][finalJ] !=1 )
                        {
                        //If this tile has a mine.
                            if (new_game.mine_array_2d[finalI][finalJ]==1)
                            {
                                //TODO Game over alert will be added.
                                dialog = new Dialog(GameActivity.this);
                                dialog.setContentView(R.layout.dialog_game_over);
                                dialog.setCancelable(false);
                                exit_yes = dialog.findViewById(R.id.go_back);
                                exit_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View paramV) {
                                        Toast.makeText(getApplicationContext(), "ASLA PES ETME", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(getBaseContext(), DifficultyActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }
                            reveal_tile(finalI, finalJ, new_game.revealedTiles);
                            new_game.revealedTiles[finalI][finalJ] =1;
                        }
                    }

                });
                
                //This value makes tile color difference to create better game experience.
                if((i+j)%2==0)
                {
                    tileButton.getBackground().setAlpha(230);
                }

                tileButton.setLayoutParams(params);
                row.addView(tileButton);


            }

            table.addView(row);
        }
    }


    public void reveal_tile(int selected_row,int selected_column,int[][] revealed_stat_matrix)
    {
        int id = (selected_row * map_size) + selected_column;
        Button revealedButton = findViewById(id);
        if(new_game.revealedTiles[selected_row][selected_column]==0)
        {
            new_game.revealedTiles[selected_row][selected_column]=1;
            revealedButton.setText("");

            revealedButton.setBackgroundColor(Color.argb(245,255,219,172));
            if((revealedButton.getId())%2==0)
            {
                revealedButton.getBackground().setAlpha(244);
            }
            int sum = adjacent_mines(selected_row, selected_column, new_game.map_size, new_game.mine_array_2d);

            if (sum >= 4)
            {revealedButton.setTextColor(Color.RED);}
            if (sum == 3)
            {revealedButton.setTextColor(Color.MAGENTA);}
            else if (sum ==1)
            {revealedButton.setTextColor(Color.BLUE);}
            else
            revealedButton.setTextColor(Color.GREEN);
            if(sum!=0)
            revealedButton.setText(String.valueOf(sum));

        }

    }
    public int adjacent_mines(int selected_row, int selected_column, int map_border, int[][] mine_array)
    {
        int sum_of_mines;
        //Top-left corner
        if (selected_row == 0 && selected_column == 0)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[1][0] +
                    mine_array[1][1] +
                    mine_array[0][1];
            if(sum_of_mines==0)
                reveal_tile(1,0,new_game.revealedTiles);
                reveal_tile(1,1,new_game.revealedTiles);
                reveal_tile(0,1,new_game.revealedTiles);
            return sum_of_mines;
        }
        //Top-right corner
        else if (selected_row == 0 && selected_column == map_border-1)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[0][map_border-2] +
                    mine_array[1][map_border-1] +
                    mine_array[1][map_border-2];
            if(sum_of_mines==0)
            reveal_tile(0,map_border-2,new_game.revealedTiles);
            reveal_tile(1,map_border-1,new_game.revealedTiles);
            reveal_tile(1,map_border-2,new_game.revealedTiles);
            return sum_of_mines;
        }
        //Bottom-left corner
        else if (selected_row == map_border-1 && selected_column == 0)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[map_border-1][0] +
                    mine_array[map_border-1][1] +
                    mine_array[map_border-2][1];
            if(sum_of_mines==0)
            reveal_tile(map_border-1,0,new_game.revealedTiles);
            reveal_tile(map_border-1,1,new_game.revealedTiles);
            reveal_tile(map_border-2,1,new_game.revealedTiles);
            return sum_of_mines;
        }
        //Bottom right corner
        else if (selected_row == map_border-1 && selected_column == map_border-1)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[map_border-2][map_border-1] +
                    mine_array[map_border-2][map_border-2] +
                    mine_array[map_border-2][map_border-1];
            if(sum_of_mines==0)
            reveal_tile(map_border-2,map_border-2,new_game.revealedTiles);
            reveal_tile(map_border-2,map_border-1,new_game.revealedTiles);
            reveal_tile(map_border-1,map_border-2,new_game.revealedTiles);
            return sum_of_mines;
        }
        //Top edge
        else if (selected_row == 0)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[0][selected_column-1] +
                    mine_array[0][selected_column+1] +
                    mine_array[selected_row+1][selected_column-1]+
                    mine_array[selected_row+1][selected_column]+
                    mine_array[selected_row+1][selected_column+1];

            if(sum_of_mines==0)
                {
                reveal_tile(selected_row, selected_column - 1, new_game.revealedTiles);
                reveal_tile(selected_row, selected_column + 1, new_game.revealedTiles);
                reveal_tile(selected_row + 1, selected_column - 1, new_game.revealedTiles);
                reveal_tile(selected_row + 1, selected_column, new_game.revealedTiles);
                reveal_tile(selected_row + 1, selected_column + 1, new_game.revealedTiles);
                }
            return sum_of_mines;

        }
        //Left edge
        else if (selected_column ==0)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[selected_row-1][0] +
                    mine_array[selected_row-1][1] +
                    mine_array[selected_row][1]+
                    mine_array[selected_row+1][1]+
                    mine_array[selected_row+1][0];
            if(sum_of_mines==0)
            {
                reveal_tile(selected_row-1, selected_column, new_game.revealedTiles);
                reveal_tile(selected_row-1,selected_column+1, new_game.revealedTiles);
                reveal_tile(selected_row,selected_column+1, new_game.revealedTiles);
                reveal_tile(selected_row + 1,selected_column, new_game.revealedTiles);
                reveal_tile(selected_row + 1,selected_column+1, new_game.revealedTiles);
            }
            return sum_of_mines;

        }
        //Right edge
        else if (selected_column == map_border-1)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[selected_row-1][selected_column] +
                    mine_array[selected_row-1][selected_column-1] +
                    mine_array[selected_row][selected_column-1]+
                    mine_array[selected_row+1][selected_column]+
                    mine_array[selected_row+1][selected_column-1];
            if(sum_of_mines==0)
            {
                reveal_tile(selected_row-1,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row-1, selected_column, new_game.revealedTiles);
                reveal_tile(selected_row,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row + 1,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row + 1,selected_column, new_game.revealedTiles);

            }
            return sum_of_mines;

        }
        //Bottom edge
        else if (selected_row == map_border-1)
        {
            sum_of_mines = 0;
            sum_of_mines =
                    mine_array[selected_row][selected_column-1] +
                    mine_array[selected_row-1][selected_column-1] +
                    mine_array[selected_row-1][selected_column]+
                    mine_array[selected_row-1][selected_column+1]+
                    mine_array[selected_row][selected_column+1];
            if(sum_of_mines==0)
            {
                reveal_tile(selected_row-1,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row-1,selected_column, new_game.revealedTiles);
                reveal_tile(selected_row-1,selected_column+1, new_game.revealedTiles);
                reveal_tile(selected_row, selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row,selected_column+1, new_game.revealedTiles);
            }
            return sum_of_mines;
        }
        else
            {

            //Middle tile
                sum_of_mines = 0;
            sum_of_mines =
                            mine_array[selected_row-1][selected_column-1] +
                            mine_array[selected_row-1][selected_column] +
                            mine_array[selected_row-1][selected_column+1] +
                            mine_array[selected_row][selected_column-1] +
                            mine_array[selected_row][selected_column+1] +
                            mine_array[selected_row+1][selected_column-1] +
                            mine_array[selected_row+1][selected_column] +
                            mine_array[selected_row+1][selected_column+1] ;
            if(sum_of_mines == 0)
            {
                reveal_tile(selected_row-1, selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row-1,selected_column, new_game.revealedTiles);
                reveal_tile(selected_row-1,selected_column+1, new_game.revealedTiles);
                reveal_tile(selected_row,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row,selected_column+1, new_game.revealedTiles);
                reveal_tile(selected_row+1,selected_column-1, new_game.revealedTiles);
                reveal_tile(selected_row+1,selected_column, new_game.revealedTiles);
                reveal_tile(selected_row+1,selected_column+1, new_game.revealedTiles);

            }
            return sum_of_mines;
        }
    }
}

package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    Context context;
    Dialog dialog;
    Button exit_yes;
    TableLayout table;
    GameMap new_game;
    DisplayMetrics displayMetrics;
    TextView timer_text;
    TextView player_score;
    Handler handler;
    String score;
    String best_score;
    int map_size;
    int[][] flag_array;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds ;
    int difficulty;
    int screenwidth;
    int sum_of_mines;
    DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();
        Objects.requireNonNull(getSupportActionBar()).hide();
        handler = new Handler() ;


        //Assing map size as intent value. Seekbar value is choosen by user.
        difficulty = getIntent().getIntExtra("SeekbarStatus",8);
        //Assing map size as intent value. Seekbar value is choosen by user.
        new_game = new GameMap(difficulty);
        //Filling gamegrid with buttons and click events.
        fill_game_grid();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        timer_text= findViewById(R.id.textview_timer);

    }

    public Runnable runnable = new Runnable()
    {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            //Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer_text.setText(//"" + Minutes + ":"+
                     String.format("%02d", Seconds));
            //+ ":"+ String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }

    };
    public void fill_game_grid()
    {
        final MediaPlayer sound_dig_pop = MediaPlayer.create(this, R.raw.dig_pop);
        final MediaPlayer sound_explosion = MediaPlayer.create(this, R.raw.explosion);
        final MediaPlayer sound_flag = MediaPlayer.create(this, R.raw.flag);
        map_size = new_game.map_size;
        flag_array = new int[map_size][map_size];
        table = findViewById(R.id.tableLayout);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenwidth = displayMetrics.widthPixels;
        //This loop creates a game grid with given size(difficulty).
        for (int i = 0; i < map_size; i++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    (screenwidth /map_size),
                    (screenwidth /map_size)
            );
            for (int j = 0; j < map_size; j++)
            {
                final Button tileButton = new Button(this);
                tileButton.setText("");
                tileButton.setId(i*map_size+j);
                if(new_game.map_size==11)
                    {
                        tileButton.setTextSize(9);
                    }
                tileButton.setBackgroundResource(R.drawable.button_tile);
                tileButton.setBackgroundResource(R.color.primary_object_color);

                final int finalI = i;
                final int finalJ = j;
                flag_array[i][j] = 0;

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

                                sound_explosion.start();
                                dialog = new Dialog(GameActivity.this);
                                dialog.setContentView(R.layout.dialog_game_over);
                                dialog.setCancelable(false);
                                exit_yes = dialog.findViewById(R.id.go_back);
                                exit_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View paramV) {
                                        Toast.makeText(getApplicationContext(), "ASLA PES ETME", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();

                                        finish();
                                    }
                                });
                                dialog.show();
                            }
                            sound_dig_pop.start();

                            revealTile(finalI, finalJ);
                            new_game.revealedTiles[finalI][finalJ] =1;
                        }
                    }

                });
                tileButton.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {

                        if(flag_array[finalI][finalJ]==0 && new_game.revealedTiles[finalI][finalJ]==0)
                        {
                            sound_flag.start();
                            flag_array[finalI][finalJ]= 1 ;
                            tileButton.setBackgroundResource(R.drawable.flag_tile);
                        }
                        else if(flag_array[finalI][finalJ]==1)
                        {
                            flag_array[finalI][finalJ]= 0 ;
                            if((finalI+finalJ)%2==0)
                            {
                                tileButton.setBackgroundResource(R.color.primary_object_color_tint);

                            }
                            else if((finalI+finalJ)%2==1)
                            {tileButton.setBackgroundResource(R.color.primary_object_color);}
                        }

                        if(isMatricesEqual(new_game.mine_array_2d,flag_array))
                        {
                            //Kazandiniz.
                            dialog = new Dialog(GameActivity.this);
                            dialog.setContentView(R.layout.dialog_game_won);
                            dialog.setCancelable(false);

                            score = String.valueOf(timer_text.getText());
                            exit_yes = dialog.findViewById(R.id.go_back_win);

                            exit_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View paramV) {
                                    dialog.dismiss();
                                    dbManager = new DBManager(GameActivity.this);
                                    dbManager.open();
                                    if (new_game.map_size==7)
                                    DBManager.insertScore(Seconds,"easy");
                                    else if (new_game.map_size==9)
                                        DBManager.insertScore(Seconds,"medium");
                                    else
                                        DBManager.insertScore(Seconds,"hard");
                                    dbManager.close();
                                    finish();
                                }
                            });
                            dialog.show();
                        }

                        return true;
                    }
                });

                //This value makes tile color difference to create better game experience.
                if((i+j)%2==0)
                {
                    tileButton.setBackgroundResource(R.color.primary_object_color_tint);
                }

                tileButton.setLayoutParams(params);
                row.addView(tileButton);
            }
            table.addView(row);
        }
    }
    public boolean isMatricesEqual(int[][] A, int[][] B)
    {
        for (int i = 0; i < new_game.map_size-1; i++)
            for (int j = 0; j < new_game.map_size-1; j++)
                if (A[i][j] != B[i][j])
                    return false;
        return true;
    }
    public void revealTile(int selected_row, int selected_column)
    {

        int id = (selected_row * map_size) + selected_column;
        Button revealedButton = findViewById(id);
        sum_of_mines=0;
        if(new_game.revealedTiles[selected_row][selected_column]==0)
        {
            new_game.revealedTiles[selected_row][selected_column]=1;
            revealedButton.setText("");
            if(((revealedButton.getId())%2)==0)
            {
                revealedButton.getBackground().setAlpha(145);
            }
            revealedButton.setBackgroundResource(R.color.background_tint);
            int sum = checkAdjacentMines(selected_row, selected_column, new_game.map_size, new_game.mine_array_2d);

            if (sum >= 4)
            {revealedButton.setTextColor(getResources().getColor(R.color.four_mine));}
            if (sum == 3)
            {revealedButton.setTextColor(getResources().getColor(R.color.three_mine));}
            else if (sum ==2)
            {revealedButton.setTextColor(getResources().getColor(R.color.two_mine));}
            else
            revealedButton.setTextColor(getResources().getColor(R.color.one_mine));
            if(sum!=0)
            revealedButton.setText(String.valueOf(sum));

        }
    }
    public int checkAdjacentMines(int selected_row, int selected_column, int map_border, int[][] mine_array)
    {
        sum_of_mines = 0;
        //Top-left corner
        if (selected_row == 0 && selected_column == 0)
        {
            sum_of_mines =
                    mine_array[1][0] +
                    mine_array[1][1] +
                    mine_array[0][1];
            if(sum_of_mines==0)
                revealTile(1,0);
                revealTile(1,1);
                revealTile(0,1);
            return sum_of_mines;
        }
        //Top-right corner
        else if (selected_row == 0 && selected_column == map_border-1)
        {
            sum_of_mines =
                    mine_array[0][map_border-2] +
                    mine_array[1][map_border-1] +
                    mine_array[1][map_border-2];
            if(sum_of_mines==0)
            revealTile(0,map_border-2);
            revealTile(1,map_border-1);
            revealTile(1,map_border-2);
            return sum_of_mines;
        }
        //Bottom-left corner
        else if (selected_row == map_border-1 && selected_column == 0)
        {
            sum_of_mines =
                    mine_array[map_border-1][0] +
                    mine_array[map_border-1][1] +
                    mine_array[map_border-2][1];
            if(sum_of_mines==0)
            revealTile(map_border-1,0);
            revealTile(map_border-1,1);
            revealTile(map_border-2,1);
            return sum_of_mines;
        }
        //Bottom right corner
        else if (selected_row == map_border-1 && selected_column == map_border-1)
        {
            sum_of_mines =
                    mine_array[map_border-2][map_border-1] +
                    mine_array[map_border-2][map_border-2] +
                    mine_array[map_border-2][map_border-1];
            if(sum_of_mines==0)
            revealTile(map_border-2,map_border-2);
            revealTile(map_border-2,map_border-1);
            revealTile(map_border-1,map_border-2);
            return sum_of_mines;
        }
        //Top edge
        else if (selected_row == 0)
        {
            sum_of_mines =
                    mine_array[0][selected_column-1] +
                    mine_array[0][selected_column+1] +
                    mine_array[selected_row+1][selected_column-1]+
                    mine_array[selected_row+1][selected_column]+
                    mine_array[selected_row+1][selected_column+1];

            if(sum_of_mines==0)
                {
                revealTile(selected_row, selected_column - 1);
                revealTile(selected_row, selected_column + 1);
                revealTile(selected_row + 1, selected_column - 1);
                revealTile(selected_row + 1, selected_column);
                revealTile(selected_row + 1, selected_column + 1);
                }
            return sum_of_mines;

        }
        //Left edge
        else if (selected_column ==0)
        {
            sum_of_mines =
                    mine_array[selected_row-1][0] +
                    mine_array[selected_row-1][1] +
                    mine_array[selected_row][1]+
                    mine_array[selected_row+1][1]+
                    mine_array[selected_row+1][0];
            if(sum_of_mines==0)
            {
                revealTile(selected_row-1, selected_column);
                revealTile(selected_row-1,selected_column+1);
                revealTile(selected_row,selected_column+1);
                revealTile(selected_row + 1,selected_column);
                revealTile(selected_row + 1,selected_column+1);
            }
            return sum_of_mines;

        }
        //Right edge
        else if (selected_column == map_border-1)
        {
            sum_of_mines =
                    mine_array[selected_row-1][selected_column] +
                    mine_array[selected_row-1][selected_column-1] +
                    mine_array[selected_row][selected_column-1]+
                    mine_array[selected_row+1][selected_column]+
                    mine_array[selected_row+1][selected_column-1];
            if(sum_of_mines==0)
            {
                revealTile(selected_row-1,selected_column-1);
                revealTile(selected_row-1, selected_column);
                revealTile(selected_row,selected_column-1);
                revealTile(selected_row + 1,selected_column-1);
                revealTile(selected_row + 1,selected_column);

            }
            return sum_of_mines;

        }
        //Bottom edge
        else if (selected_row == map_border-1)
        {
            sum_of_mines =
                    mine_array[selected_row][selected_column-1] +
                    mine_array[selected_row-1][selected_column-1] +
                    mine_array[selected_row-1][selected_column]+
                    mine_array[selected_row-1][selected_column+1]+
                    mine_array[selected_row][selected_column+1];
            if(sum_of_mines==0)
            {
                revealTile(selected_row-1,selected_column-1);
                revealTile(selected_row-1,selected_column);
                revealTile(selected_row-1,selected_column+1);
                revealTile(selected_row, selected_column-1);
                revealTile(selected_row,selected_column+1);
            }
            return sum_of_mines;
        }
        else
            {

            //Middle tile
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
                revealTile(selected_row-1, selected_column-1);
                revealTile(selected_row-1,selected_column);
                revealTile(selected_row-1,selected_column+1);
                revealTile(selected_row,selected_column-1);
                revealTile(selected_row,selected_column+1);
                revealTile(selected_row+1,selected_column-1);
                revealTile(selected_row+1,selected_column);
                revealTile(selected_row+1,selected_column+1);

            }
            return sum_of_mines;
        }
    }
}

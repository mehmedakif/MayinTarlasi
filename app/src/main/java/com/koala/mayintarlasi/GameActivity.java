package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.UUID;
import com.koala.mayintarlasi.models.UserModel;
import com.koala.mayintarlasi.services.APIClient;
import com.koala.mayintarlasi.services.ProjectService;

import java.time.LocalDate;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity
{

    Context context;
    Dialog dialog;
    Button exit_yes;
    Button accept;
    Switch switch_tutorial;
    TableLayout table_layout;
    GameMap new_game;
    DBManager dbManager;
    DisplayMetrics displayMetrics;
    TextView timer_text;
    Handler handler;
    String player_score;
    Dialog dialog_tutorial;
    int map_size;
    int[][] flag_array;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds ;
    int choosen_difficulty;
    int screenwidth;
    int number_of_mines;
    TextView text_total_mines;
    MediaPlayer sound_dig_pop;
    MediaPlayer sound_explosion;
    MediaPlayer sound_flag;
    MediaPlayer sound_win;
    AdView adView;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

    public Runnable runnable = new Runnable()
    {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer_text.setText(String.format("%02d", Seconds));
            handler.postDelayed(this, 0);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();
        Objects.requireNonNull(getSupportActionBar()).hide();
        handler = new Handler() ;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenwidth = displayMetrics.widthPixels;

        adView = findViewById(R.id.bannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        uniqueID = id(GameActivity.this);

        //Assing map size as intent value. Seekbar value is choosen by user.
        choosen_difficulty = getIntent().getIntExtra("SeekbarStatus",8);
        //Assing map size as intent value. Seekbar value is choosen by user.
        new_game = new GameMap(choosen_difficulty);
        map_size = new_game.map_size;
        flag_array = new int[map_size][map_size];

        table_layout = findViewById(R.id.tableLayout);
        timer_text = findViewById(R.id.textview_timer);
        text_total_mines = findViewById(R.id.text_total_mines);
        number_of_mines = new_game.mine_count;
        text_total_mines.setText(String.valueOf(number_of_mines));

        sound_dig_pop = MediaPlayer.create(this, R.raw.step);
        sound_explosion = MediaPlayer.create(this, R.raw.bomb);
        sound_flag = MediaPlayer.create(this, R.raw.flag);
        sound_win = MediaPlayer.create(this, R.raw.win);

        //Filling gamegrid with buttons and click events.
        fill_game_grid();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("tutPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        boolean showHelp = pref.getBoolean("tutorial", false);
        if(!showHelp)
        {
            dialog_tutorial = new Dialog(GameActivity.this);
            dialog_tutorial.setContentView(R.layout.dialog_tutorial);
            dialog_tutorial.setCancelable(true);
            accept = dialog_tutorial.findViewById(R.id.ok_button);
            switch_tutorial = dialog_tutorial.findViewById(R.id.tutorial_switch);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View paramV) {
                    dialog_tutorial.dismiss();
                    editor.putBoolean("tutorial", switch_tutorial.isChecked());
                    editor.apply();
                }
            });
            dialog_tutorial.show();
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



    }
    public void fill_game_grid()
    {


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
                tileButton.setId((i*map_size)+j);
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

                                dialog = new Dialog(GameActivity.this);
                                dialog.setContentView(R.layout.dialog_game_over);
                                dialog.setCancelable(false);
                                exit_yes = dialog.findViewById(R.id.go_back);
                                exit_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View paramV) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                sound_explosion.start();
                                dialog.show();
                            }
                            else
                                {
                                sound_dig_pop.start();
                                revealTile(finalI, finalJ);
                                new_game.revealedTiles[finalI][finalJ] = 1;
                                }

                        }
                    }

                });
                tileButton.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {

                        if(flag_array[finalI][finalJ]==0)
                        {
                            sound_flag.start();
                            flag_array[finalI][finalJ]=1 ;
                            tileButton.setBackgroundResource(R.drawable.flag_tile);
                            text_total_mines.setText(String.valueOf(countFlags()));

                        }
                        else if(flag_array[finalI][finalJ]==1)
                        {
                            flag_array[finalI][finalJ]= 0 ;
                            text_total_mines.setText(String.valueOf(countFlags()));
                            if((finalI+finalJ)%2==0)
                            {
                                tileButton.setBackgroundResource(R.color.primary_object_color_tint);
                            }
                            else if((finalI+finalJ)%2==1)
                            {tileButton.setBackgroundResource(R.color.primary_object_color);}
                        }
                        if(isMatricesEqual(new_game.mine_array_2d,flag_array))
                        {
                            //SUCCESS! situation.
                            sound_win.start();
                            dialog = new Dialog(GameActivity.this);
                            dialog.setContentView(R.layout.dialog_game_won);
                            dialog.setCancelable(false);

                            player_score = String.valueOf(timer_text.getText());
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
                            //Sending scores to server for global statistics.
                            sendScore(Seconds,choosen_difficulty);
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
            table_layout.addView(row);
        }
    }
    public boolean isMatricesEqual(int[][] A, int[][] B)
    {
        boolean equality = true;
        for (int i = 0; i < new_game.map_size - 1; i++)
        {
            for (int j = 0; j < new_game.map_size - 1; j++)
            {
                if ((A[i][j] == 0 && B[i][j] == 1) || (A[i][j] == 1 && B[i][j] == 0)) {
                    equality = false;
                    break;
                }
            }
        }
        return equality;
    }
    public void revealTile(int selected_row, int selected_column)
    {

        int id = (selected_row * map_size) + selected_column;
        Button revealedButton = findViewById(id);

//        if(flag_array[selected_column][selected_row]==1)
//        {
//            flag_array[selected_column][selected_row]=0;
//            text_total_mines.setText(String.valueOf(countFlags()));
//        }

        if(new_game.revealedTiles[selected_row][selected_column]==0)
        {
            if(flag_array[selected_column][selected_row]==1)
            {
                flag_array[selected_column][selected_row]=0;
                text_total_mines.setText(String.valueOf(countFlags()));
            }

            new_game.revealedTiles[selected_row][selected_column]=1;
            if(((revealedButton.getId())%2)==0)
            {
                revealedButton.getBackground().setAlpha(145);
            }
            revealedButton.setBackgroundResource(R.color.background_tint);
            int sum = checkAdjacentMines(selected_row, selected_column, new_game.map_size, new_game.mine_array_2d);
            if (sum >= 5)
            {revealedButton.setTextColor(getResources().getColor(R.color.four_mine));}
            else if (sum == 4)
            {revealedButton.setTextColor(getResources().getColor(R.color.four_mine));}
            else if (sum == 3)
            {revealedButton.setTextColor(getResources().getColor(R.color.three_mine));}
            else if (sum ==2)
            {revealedButton.setTextColor(getResources().getColor(R.color.two_mine));}
            else
            revealedButton.setTextColor(getResources().getColor(R.color.one_mine));
            if(sum!=0)
            {
                revealedButton.setText(String.valueOf(sum));
            }
            else
            {
                revealedButton.setText("");
            }
        }
    }
    public int checkAdjacentMines(int selected_row, int selected_column, int map_border, int[][] mine_array)
    {
        int sum_of_mines;
        //Top-left corner
        if (selected_row == 0 && selected_column == 0)
        {
            sum_of_mines =
                    mine_array[1][0] +
                    mine_array[1][1] +
                    mine_array[0][1];
            if(sum_of_mines==0)
            {
                revealTile(1,0);
                revealTile(1,1);
                revealTile(0,1);
            }

        }
        //Top-right corner
        else if (selected_row == 0 && selected_column == map_border-1)
        {
            sum_of_mines =
                    mine_array[0][map_border-2] +
                    mine_array[1][map_border-1] +
                    mine_array[1][map_border-2];
            if(sum_of_mines==0)
            {
                revealTile(0, map_border - 2);
                revealTile(1, map_border - 1);
                revealTile(1, map_border - 2);
            }
        }
        //Bottom-left corner
        else if (selected_row == map_border-1 && selected_column == 0)
        {
            sum_of_mines =
                    mine_array[map_border-1][0] +
                    mine_array[map_border-1][1] +
                    mine_array[map_border-2][1];
            if(sum_of_mines==0){
            revealTile(map_border-1,0);
            revealTile(map_border-1,1);
            revealTile(map_border-2,1);
            }
            //return sum_of_mines;
        }
        //Bottom right corner
        else if (selected_row == map_border-1 && selected_column == map_border-1)
        {
            sum_of_mines =
                    mine_array[map_border-2][map_border-1] +
                    mine_array[map_border-2][map_border-2] +
                    mine_array[map_border-2][map_border-1];
            if(sum_of_mines==0)
            {
                revealTile(map_border - 2, map_border - 2);
                revealTile(map_border - 2, map_border - 1);
                revealTile(map_border - 1, map_border - 2);
            }
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
        }
        return sum_of_mines;
    }
    public int countFlags()
    {
        int flag_count = 0;
        for (int i = 0; i < map_size; i++)
        {
            for (int j = 0; j < map_size; j++)
            {
                if (flag_array[i][j]==1)
                {
                    flag_count = flag_count + 1;
                }
            }
        }
        return number_of_mines - flag_count;
    }
    public String sendScore(int seconds, int choosen_difficulty)
    {
        UserModel userToSend = new UserModel(uniqueID,seconds,choosen_difficulty);
        ProjectService projeService = APIClient.getClient().create(ProjectService.class);
        Call<String> call = projeService.sendUserScore(userToSend);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                String postResponse = response.body();
                Toast.makeText(getApplicationContext(),"Basariyla gonderildi: "+postResponse, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),"Hatali bir şeyler var!", Toast.LENGTH_SHORT).show();
            }
        });

        return "Success";
    }


}

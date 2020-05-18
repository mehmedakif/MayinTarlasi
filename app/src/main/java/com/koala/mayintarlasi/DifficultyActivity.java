package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class DifficultyActivity extends AppCompatActivity {

    Dialog dialog;
    Button exit_yes;
    Button exit_no;
    Animation scaleUpAnimation;
    Animation animationSeekbar;
    Button start_button;
    TextView text_mapsize;
    TextView text_mine_count;
    Button scoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        Objects.requireNonNull(getSupportActionBar()).hide();

        text_mapsize = findViewById(R.id.text_difficulty_mapsize);
        text_mapsize.setTextColor(ContextCompat.getColor(this, R.color.primary_object_color));
        text_mapsize.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text_mapsize.setText(getResources().getString(R.string.mapMedium));
        text_mapsize.setTypeface(text_mapsize.getTypeface(), Typeface.BOLD);

        text_mine_count = findViewById(R.id.text_difficulty_minecount);
        text_mine_count.setTextColor(ContextCompat.getColor(this, R.color.primary_object_color));
        text_mine_count.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text_mine_count.setText(getResources().getString(R.string.minesMedium));
        text_mine_count.setTypeface(text_mine_count.getTypeface(), Typeface.BOLD);

        start_button = findViewById(R.id.start_game_button);
        button_animation(start_button);

        scoreboard = findViewById(R.id.button_scoreboard);
        scoreboard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getBaseContext(), Scoreboard.class);
                startActivity(intent);
            }
        });

        final SeekBar seekbarDifficulty = findViewById(R.id.seekbar_difficulty);
        seekbarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                animationSeekbar = AnimationUtils.loadAnimation(DifficultyActivity.this, R.anim.scale_down);
                animationSeekbar.setFillAfter(true);
                text_mapsize.startAnimation(animationSeekbar);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                scaleUpAnimation = AnimationUtils.loadAnimation(DifficultyActivity.this, R.anim.scale_up);
                scaleUpAnimation.setFillAfter(true);
                text_mapsize.startAnimation(scaleUpAnimation);

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
            {
                if (String.valueOf(progress).equals("0"))
                {
                    text_mapsize.setText(getResources().getString(R.string.mapEasy));
                    text_mine_count.setText(getResources().getString(R.string.minesEasy));
                }
                else if (String.valueOf(progress).equals("1"))
                {
                    text_mapsize.setText(getResources().getString(R.string.mapMedium));
                    text_mine_count.setText(getResources().getString(R.string.minesMedium));
                }
                else if (String.valueOf(progress).equals("2"))
                {
                    text_mapsize.setText(getResources().getString(R.string.mapHard));
                    text_mine_count.setText(getResources().getString(R.string.minesHard));
                }

            }
        });
        //Start butonu dinleyicisi.
        start_button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                //new_map = new GameMap(seekbarDifficulty.getProgress());
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                //intent.putExtra("Map", new_map.mine_array_1d);
                intent.putExtra("SeekbarStatus", seekbarDifficulty.getProgress());
                startActivity(intent);
            }
        });
    }

    private void button_animation(Button button)
    {

        Animation animation2 = AnimationUtils.loadAnimation(DifficultyActivity.this, R.anim.button_jiggle);
        animation2.setFillAfter(true);
        button.startAnimation(animation2);

    }

    public void onBackPressed()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exit_yes = dialog.findViewById(R.id.button_exit_yes);
        exit_no = dialog.findViewById(R.id.button_exit_no);

        exit_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View paramV) {
                dialog.dismiss();
                System.exit(0);
            }
        });
        exit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramV)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}

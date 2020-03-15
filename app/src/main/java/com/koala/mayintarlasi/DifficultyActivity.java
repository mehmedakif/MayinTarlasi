package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        Objects.requireNonNull(getSupportActionBar()).hide();

        final TextView text_mapsize= findViewById(R.id.text_difficulty_mapsize);
        text_mapsize.setTextColor(getResources().getColor(R.color.black));
        text_mapsize.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text_mapsize.setText(getResources().getString(R.string.map10));

        final TextView text_mine_count = findViewById(R.id.text_difficulty_minecount);
        text_mine_count.setTextColor(getResources().getColor(R.color.black));
        text_mine_count.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text_mine_count.setText("20 Mayin");


        final SeekBar sk=(SeekBar) findViewById(R.id.seekbar_difficulty);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Animation animation2 = AnimationUtils.loadAnimation(DifficultyActivity.this, R.anim.scale_down);
                animation2.setFillAfter(true);
                text_mapsize.startAnimation(animation2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Animation animation2 = AnimationUtils.loadAnimation(DifficultyActivity.this, R.anim.scale_up);
                animation2.setFillAfter(true);
                text_mapsize.startAnimation(animation2);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                if (String.valueOf(progress).equals("0"))
                {text_mapsize.setText(getResources().getString(R.string.map8));
                text_mine_count.setText("10 Mayin");}
                else if (String.valueOf(progress).equals("1"))
                {text_mapsize.setText(getResources().getString(R.string.map10));
                    text_mine_count.setText("20 Mayin");}
                else
                {text_mapsize.setText(getResources().getString(R.string.map12));
                    text_mine_count.setText("30 Mayin");}

            }
        });
    }

}

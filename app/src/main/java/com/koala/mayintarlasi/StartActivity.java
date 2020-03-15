package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class StartActivity extends AppCompatActivity {

    Dialog dialog;
    Button exit_yes;
    Button exit_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Objects.requireNonNull(getSupportActionBar()).hide();
        final Button continue_button = findViewById(R.id.continue_button);
        button_animation(continue_button);

        continue_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramV) {
                Intent myIntent = new Intent(StartActivity.this, DifficultyActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });

    }

    private void button_animation(Button button) {

        Animation animation2 = AnimationUtils.loadAnimation(StartActivity.this, R.anim.button_jiggle);
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
        exit_yes = (Button) dialog.findViewById(R.id.button_exit_yes);
        exit_no = (Button) dialog.findViewById(R.id.button_exit_no);

        exit_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramV) {
                Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();
                System.exit(0);

            }
        });


        exit_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramV) {
                Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}


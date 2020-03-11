package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.Objects;


public class StartActivity extends AppCompatActivity {


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
                Intent myIntent = new Intent(StartActivity.this, MainActivity.class);
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

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setTitle("Gercekten Cikmak Istiyor Musunuz?");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

}


package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Handler mHandler = new Handler();

        mHandler.postDelayed(start_activity,1500);


    }

    private Runnable start_activity = new Runnable() {
        public void run() {
            Intent i = new Intent(getApplicationContext(),StartActivity.class);
            startActivity(i);
            finish();
        }
    };
}

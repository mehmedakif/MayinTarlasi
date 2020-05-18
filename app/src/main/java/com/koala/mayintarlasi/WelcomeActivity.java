package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressBar progressbar_loading;
    private Runnable start_activity = new Runnable() {
        public void run() {
            progressbar_loading.setProgress(100);
            Intent i = new Intent(getApplicationContext(),DifficultyActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();
        progressbar_loading = findViewById(R.id.progressbar_loading);

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressbar_loading,"progress",0,96);
        progressAnimator.setDuration(400);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();

        Handler mHandler = new Handler();

        mHandler.postDelayed(start_activity,500);

    }

}

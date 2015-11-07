package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ExerciseActivity extends WearableActivity {

    public static final int EXERCISE_DURATION = 20000;

    private final int STRETCH_EXERCISE_IMAGES[] = {R.drawable.exercise1_hand1,
            R.drawable.exercise1_hand2};

    private CountDownTimer timer;
    private ImageView exerciseImageView;
    private TextView secondsLeftTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setAmbientEnabled();

        exerciseImageView = (ImageView) findViewById(R.id.exersice_image);
        secondsLeftTextView = (TextView) findViewById(R.id.exercise_seconds_left_tv);
        AlphaAnimation countdownAnimation = new AlphaAnimation(1.0f, 0.6f);
        countdownAnimation.setDuration(1000);
        countdownAnimation.setRepeatCount(-1);
        secondsLeftTextView.setAnimation(countdownAnimation);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        int delay = 50;
        timer = new CountDownTimer(EXERCISE_DURATION, delay) {

            public void onTick(long millisUntilFinished) {
                int remainingSeconds = (int) (millisUntilFinished / 1000);

                exerciseImageView.setImageResource(STRETCH_EXERCISE_IMAGES[remainingSeconds % 4 > 1 ? 0 : 1]);
                secondsLeftTextView.setText("00:" + (remainingSeconds < 10 ? "0" + remainingSeconds : remainingSeconds));

                double progress = 1 - ((double) millisUntilFinished / EXERCISE_DURATION);
                progressBar.setProgress((int) (progress * 10000));
            }

            public void onFinish() {
                secondsLeftTextView.setText("00:00");
                progressBar.setProgress(10000);
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        }.start();
        countdownAnimation.start();

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {

    }
}

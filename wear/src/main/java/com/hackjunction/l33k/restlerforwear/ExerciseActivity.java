package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ExerciseActivity extends WearableActivity {

    public static final int EXERCISE_DURATION = 20000;

    private final int STRETCH_EXERCISE_IMAGES[] = {R.drawable.exercise1_hand1_v2,
            R.drawable.exercise1_hand2_v2};

    private CountDownTimer timer;
    private ImageView exerciseImageView;
    private TextView secondsLeftTextView;
    private ProgressBar progressBar;

    private int step = 0;

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

        timer = new CountDownTimer(EXERCISE_DURATION + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                exerciseImageView.setImageResource(STRETCH_EXERCISE_IMAGES[step % 4 > 1 ? 0 : 1]);

                int secondsLeft = EXERCISE_DURATION / 1000 - step;
                secondsLeftTextView.setText("00:" + (secondsLeft < 10 ? "0" + secondsLeft : secondsLeft));

                double progress = step / (EXERCISE_DURATION / 1000.0);
                progressBar.setProgress((int) (progress * 100));
                step++;
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

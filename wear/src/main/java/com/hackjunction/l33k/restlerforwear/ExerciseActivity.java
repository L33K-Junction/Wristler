package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseActivity extends WearableActivity {

    public static final int EXERCISE_DURATION = 15000;

    private final int STRETCH_EXERCISE_IMAGES[] = {R.drawable.exercise1_hand1,
            R.drawable.exercise1_hand2};

    private CountDownTimer timer;
    private ImageView exerciseImageView;
    private TextView secondsLeftTextView;

    private int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setAmbientEnabled();

        exerciseImageView = (ImageView) findViewById(R.id.exersice_image);
        secondsLeftTextView = (TextView) findViewById(R.id.exercise_seconds_left_tv);

        timer = new CountDownTimer(EXERCISE_DURATION, 1000) {

            public void onTick(long millisUntilFinished) {
                step++;
                exerciseImageView.setImageResource(STRETCH_EXERCISE_IMAGES[step % 4 > 1 ? 0 : 1]);

                secondsLeftTextView.setText((EXERCISE_DURATION / 1000 - step) + " sec");
            }

            public void onFinish() {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        }.start();
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

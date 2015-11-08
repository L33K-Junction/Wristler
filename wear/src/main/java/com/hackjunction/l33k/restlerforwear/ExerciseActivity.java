package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class ExerciseActivity extends WearableActivity {

    public static final int EXERCISE_DURATION = 20000;
    public static final int DELAY = 50;

    private final int STRETCH_EXERCISE_IMAGES[] = {R.drawable.exercise1_hand1,
            R.drawable.exercise1_hand2};
    private final int PUSH_EXERCISE_IMAGES[] = {R.drawable.exercise2_hand1,
            R.drawable.exercise2_hand2, R.drawable.exercise2_hand3};
    private final int SHAKE_EXERCISE_IMAGES[] = {R.drawable.exercise3_hand1};

    private CountDownTimer timer;
    private ImageView exerciseImageView;
    private TextView exerciseNameTextView;
    private TextView secondsLeftTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setAmbientEnabled();

        final Typeface introTf = Typeface.createFromAsset(getAssets(), "fonts/Intro.otf");

        exerciseImageView = (ImageView) findViewById(R.id.exersice_image);
        secondsLeftTextView = (TextView) findViewById(R.id.exercise_seconds_left_tv);
        exerciseNameTextView = (TextView) findViewById(R.id.exercise_name);
        Random random = new Random();
        final int exerciseNumber = random.nextInt(2);
        exerciseNameTextView.setText(getExerciseName(exerciseNumber));
        secondsLeftTextView.setTypeface(introTf);

        AlphaAnimation countdownAnimation = new AlphaAnimation(1.0f, 0.6f);
        countdownAnimation.setDuration(1000);
        countdownAnimation.setRepeatCount(-1);
        secondsLeftTextView.setAnimation(countdownAnimation);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        timer = new CountDownTimer(EXERCISE_DURATION, DELAY) {

            public void onTick(long millisUntilFinished) {
                int remainingSeconds = (int) (millisUntilFinished / 1000);

                exerciseImageView.setImageResource(getExercisePicture(exerciseNumber, remainingSeconds));
                secondsLeftTextView.setText("00:" + (remainingSeconds < 10 ? "0" + remainingSeconds : remainingSeconds));

                double progress = 1 - ((double) millisUntilFinished / EXERCISE_DURATION);
                progressBar.setProgress((int) (progress * 10000));
            }

            public void onFinish() {
                secondsLeftTextView.setText("00:00");
                progressBar.setProgress(10000);
                Intent mainActivity = new Intent(getApplicationContext(), CloseActivity.class);
                startActivity(mainActivity);
            }
        }.start();
        countdownAnimation.start();

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }

    private int getExerciseName(int number) {
        switch (number) {
            case 0:
                return R.string.exersize1;
            case 1:
                return R.string.exersize2;
            case 2:
                return R.string.exersize3;
            default:
                throw new RuntimeException("Something is wrong!");
        }
    }

    private int getExercisePicture(int number, int remainingSeconds) {
        switch (number) {
            case 0:
                return STRETCH_EXERCISE_IMAGES[remainingSeconds % 4 > 1 ? 0 : 1];
            case 1:
                int remainder = remainingSeconds % 6;
                if (remainder > 3) {
                    return PUSH_EXERCISE_IMAGES[2];
                } else if (remainder > 1) {
                    return PUSH_EXERCISE_IMAGES[1];
                } else {
                    return PUSH_EXERCISE_IMAGES[0];
                }
            case 2:
                return SHAKE_EXERCISE_IMAGES[0];
            default:
                throw new RuntimeException("Something is wrong!");
        }
    }
}

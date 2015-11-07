package com.hackjunction.l33k.restlerforwear;

        import android.animation.ObjectAnimator;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.os.Vibrator;
        import android.support.v4.app.NotificationCompat;
        import android.support.v4.app.NotificationManagerCompat;
        import android.support.v4.app.NotificationCompat.WearableExtender;
        import android.support.wearable.activity.WearableActivity;
        import android.support.wearable.view.BoxInsetLayout;
        import android.view.View;
        import android.view.animation.DecelerateInterpolator;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;

/**
 * Created by ceduic on 11/7/15.
 */
public class OrientationActivity extends WearableActivity implements SensorEventListener {
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private CountDownTimer exerciseCountdown;
    private double currentProgress;

    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    Sensor accelerometer;
    Sensor magnetometer;

    Float azimuth;
    Float pitch;
    Float roll;

    private BoxInsetLayout mContainerView;
    private TextView mStatusView;
    private TextView mPitchView;
    private TextView mRollView;
    private TextView mTimer;
    private ProgressBar mProgress;
    private Button mButton;
    private NotificationManagerCompat notificationManager;

    private final int EXERCISE_INTERVAL = 120000;

    private TextView mClockView;

    private int progress;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(mCustomDrawableView);    // Register the sensor listeners
        setContentView(R.layout.activity_orientation);
      //  setAmbientEnabled();

//        Intent viewIntent = new Intent(this, OrientationActivity.class);
//       // viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
//        PendingIntent viewPendingIntent =
//                PendingIntent.getActivity(this, 0, viewIntent, 0);

//        final NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.button)
//                        .setContentTitle("Time for exercise!")
//                        .setContentText("Do some wrist moves")
//                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
//        notificationManager = NotificationManagerCompat.from(this);


        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mStatusView = (TextView) findViewById(R.id.status);
        mPitchView = (TextView) findViewById(R.id.pitch);
        mRollView = (TextView) findViewById(R.id.roll);
        mTimer = (TextView) findViewById(R.id.timer);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mButton = (Button) findViewById(R.id.ack);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent exerciseActivity = new Intent(getApplicationContext(), ExerciseActivity.class);
                startActivity(exerciseActivity);
            }
        });

        mStatusView.setText("Observing...");

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        currentProgress = 0.0;
        // Set exercise to trigger in constant unless wrist movement is detected
      //  exerciseCountdown = createCountDown(EXERCISE_INTERVAL);
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                double progress = (currentProgress / 100) * 100;
                if (progress < 10000.0) {
                    int roundProgress = (int) progress;
                    mProgress.setProgress(roundProgress);
                    currentProgress += 20;
                    mTimer.setText(Integer.toString(roundProgress/100) + "%");
                    h.postDelayed(this, 20);
                } else {
                    mStatusView.setText("EXERCISE!");
                    mButton.setVisibility(View.VISIBLE);
                    mProgress.setProgress(100);
                    mVibrator.vibrate(1000L);
                }

            }
        }, 20);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
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
//
//    private void updateDisplay() {
//        if (isAmbient()) {
//            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
//            mTextView.setTextColor(getResources().getColor(android.R.color.white));
//            mClockView.setVisibility(View.VISIBLE);
//
//            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
//        } else {
//            mContainerView.setBackground(null);
//            mTextView.setTextColor(getResources().getColor(android.R.color.black));
//            mClockView.setVisibility(View.GONE);
//        }
//    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    float[] mGravity;
    float[] mGeomagnetic;
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
                pitch = orientation[1]; // orientation contains: azimuth, pitch and roll
                roll = orientation[2]; // orientation contains: azimuth, pitch and roll
                if (Math.abs(pitch) > 0.5 || Math.abs(roll) > 0.5) {
                    currentProgress -= 50;
                }
                mPitchView.setText("Pitch:\n" + pitch.toString());
                mRollView.setText("Roll:\n" + roll.toString());

            }
        }
    }
}
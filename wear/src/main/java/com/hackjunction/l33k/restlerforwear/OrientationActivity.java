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

    private CountDownTimer orientationCountdown;
    private int timeleft;

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
    private ProgressBar mProgress;
    private Button mButton;
    private NotificationManagerCompat notificationManager;

    private TextView mClockView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(mCustomDrawableView);    // Register the sensor listeners
        setContentView(R.layout.activity_orientation);
      //  setAmbientEnabled();

        Intent viewIntent = new Intent(this, OrientationActivity.class);
       // viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.button)
                        .setContentTitle("Time for exercise!")
                        .setContentText("Do some wrist moves")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        notificationManager = NotificationManagerCompat.from(this);


        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mStatusView = (TextView) findViewById(R.id.status);
        mPitchView = (TextView) findViewById(R.id.pitch);
        mRollView = (TextView) findViewById(R.id.roll);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mButton = (Button) findViewById(R.id.ack);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        mStatusView.setText("Observing...");

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        orientationCountdown = new CountDownTimer(30000, 20) {
            int current = 0;
            public void onTick(long millisUntilFinished) {
                double progress = ((30000.0 - millisUntilFinished)/30000) * 10000;
                mProgress.setProgress((int) progress);
            }

            public void onFinish() {
                mStatusView.setText("EXERCISE!");
                mButton.setVisibility(View.VISIBLE);
                mProgress.setProgress(100);
                mVibrator.vibrate(1000L);
                long time = new Date().getTime();
                String tmpStr = String.valueOf(time);
                String last4Str = tmpStr.substring(tmpStr.length() - 5);
                int notificationId = Integer.valueOf(last4Str);

                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        }.start();

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
                mPitchView.setText("Pitch:\n" + pitch.toString());
                mRollView.setText("Roll:\n" + roll.toString());

            }
        }
    }
}
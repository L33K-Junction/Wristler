package com.hackjunction.l33k.restlerforwear;

        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Vibrator;
        import android.support.wearable.activity.WearableActivity;
        import android.support.wearable.view.BoxInsetLayout;
        import android.view.View;
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

    private TextView mClockView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(mCustomDrawableView);    // Register the sensor listeners
        setContentView(R.layout.activity_orientation);
      //  setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mStatusView = (TextView) findViewById(R.id.status);
        mPitchView = (TextView) findViewById(R.id.pitch);
        mRollView = (TextView) findViewById(R.id.roll);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mButton = (Button) findViewById(R.id.ack);

        mStatusView.setText("Piping sensor data...");

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        orientationCountdown = new CountDownTimer(30000, 50) {

            public void onTick(long millisUntilFinished) {
                double progress = ((30000.0 - millisUntilFinished)/30000) * 100;
                mProgress.setProgress((int) progress);
            }

            public void onFinish() {
                mStatusView.setText("EXERCISE!");
                mButton.setVisibility(View.VISIBLE);
                mProgress.setProgress(100);
                mVibrator.vibrate(1000L);
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


//    @Override
//    public void onEnterAmbient(Bundle ambientDetails) {
//        super.onEnterAmbient(ambientDetails);
//        updateDisplay();
//    }
//
//    @Override
//    public void onUpdateAmbient() {
//        super.onUpdateAmbient();
//        updateDisplay();
//    }
//
//    @Override
//    public void onExitAmbient() {
//        updateDisplay();
//        super.onExitAmbient();
//    }
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
                mPitchView.setText("Pitch: " + pitch.toString());
                mRollView.setText("Roll: " + roll.toString());

            }
        }
    }
}
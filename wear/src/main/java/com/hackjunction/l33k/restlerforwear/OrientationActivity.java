package com.hackjunction.l33k.restlerforwear;

        import android.animation.ObjectAnimator;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.graphics.Typeface;
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
        import android.util.Log;
        import android.view.View;
        import android.view.animation.DecelerateInterpolator;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
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

   // private float[] orientation = new float[3];
   // private float[] orientationPrevious = new float[3];
   // private float[] minOrientation = new float[3];
    ArrayList samples = new ArrayList();
    private float accelTotal = 0L;

    private BoxInsetLayout mContainerView;
    private TextView mStatusView;
    private TextView mPitchView;
    private TextView mRollView;
    private TextView mTimer;
    private ProgressBar mProgress;
    private Button mButton;
    private NotificationManagerCompat notificationManager;

    private final int EXERCISE_INTERVAL = 30000;

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


        final Typeface introTf = Typeface.createFromAsset(getAssets(), "fonts/Intro.otf");
        final Typeface introInlineTf = Typeface.createFromAsset(getAssets(), "fonts/Intro Inline.otf");

        mStatusView.setText("Observing...");
        mTimer.setTypeface(introTf);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.i("ACCELEROMETER " , String.valueOf(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getMaximumRange()));
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Log.i("TYPE_MAGNETIC_FIELD " , String.valueOf(mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).getMaximumRange()));

        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        currentProgress = 0.0;
        // Set exercise to trigger in constant unless wrist movement is detected
        final Handler sample = new Handler();
        sample.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                samples.add(accelTotal);
                accelTotal = 0L;
                if (samples.size() > 10) {
                    samples.remove(0);
                }
                float totalNow = 0L;
                for (int i = 0; i < samples.size(); i++) {
                    totalNow += (float) samples.get(i);
                }
                float averageAccel = totalNow/samples.size();
             //   mTimer.setText("Accel: \n" + Float.toString(averageAccel));
                sample.postDelayed(this, 1000);
            }
        }, 1000);
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
                    currentProgress += 15;
                    h.postDelayed(this, 100);
                } else {
                    mStatusView.setText("EXERCISE!");
                    mStatusView.setTypeface(introInlineTf);
                    mButton.setVisibility(View.VISIBLE);
                    mProgress.setProgress(10000);
                    mVibrator.vibrate(1000L);
                }

            }
        }, 100);
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
    float[] mLastAccel = new float[3];
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float deltaX = Math.abs(mLastAccel[0] - event.values[0]);
            float deltaY = Math.abs(mLastAccel[1] - event.values[1]);
            float deltaZ = Math.abs(mLastAccel[2] - event.values[2]);
            mLastAccel[0] = event.values[0];
            mLastAccel[1] = event.values[1];
            mLastAccel[2] = event.values[2];
            if (deltaX > 0.5) {
                accelTotal += deltaX;
            }
            if (deltaY > 0.5) {
                accelTotal += deltaY;
            }
            if (deltaZ > 0.5) {
                accelTotal += deltaZ;
            }
            mPitchView.setText(Float.toString(deltaX));
            mRollView.setText(Float.toString(event.values[0]));
        }
            //mGravity = event.values;
        // if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        //     mGeomagnetic = event.values;
        //  if (mGravity != null && mGeomagnetic != null) {
         //    float R[] = new float[9];
          //   float I[] = new float[9];
          //   boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
           //  if (success) {
                // Get accel delta for event and sum it to total
               // SensorManager.getOrientation(R, orientation);
               // mPitchView.setText("Pitch:\n" + pitch.toString());
               // mRollView.setText("Roll:\n" + roll.toString());
       //      }
       //  }
    }
}
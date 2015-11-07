package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by Valeria on 07.11.2015.
 */
public class MainActivity extends Activity {

    private ImageView imageView;
    private TextView startTv;
    ImageView imageViewStart, imageViewClosing;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageWave);
        startTv = (TextView) findViewById(R.id.start);
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientationActivity = new Intent(getApplicationContext(), OrientationActivity.class);
                startActivity(orientationActivity);
            }
        });

        imageViewStart = (ImageView) findViewById(R.id.imageWave);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.wave);
        imageViewStart.startAnimation(animRotate);


        new Runnable() {
            private boolean handState;

            @Override
            public void run() {
                imageViewClosing = (ImageView)findViewById(R.id.imageClose);
                if (!handState) {
                    imageViewClosing.setImageResource(R.mipmap.thehand);
                } else {
                    imageViewClosing.setImageResource(R.mipmap.thehandbye);
                }
                handState = !handState;
                imageViewClosing.postDelayed(this, 3000);
            }
        }.run();
    }



}

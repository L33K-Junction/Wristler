package com.hackjunction.l33k.restlerforwear;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
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

    private ImageView imageView, imageViewGoodbye;
    private TextView startTv;
    private TextView appNameTv;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_main);

        Typeface introTf = Typeface.createFromAsset(getAssets(), "fonts/Intro.otf");
        Typeface introInlineTf = Typeface.createFromAsset(getAssets(), "fonts/Intro Inline.otf");

        appNameTv = (TextView) findViewById(R.id.app_name_tv);
        appNameTv.setTypeface(introInlineTf);

        startTv = (TextView) findViewById(R.id.start);
        startTv.setTypeface(introTf);
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientationActivity = new Intent(getApplicationContext(), OrientationActivity.class);
                startActivity(orientationActivity);
            }
        });

        imageView = (ImageView) findViewById(R.id.imageWave);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.greeting_handwave));




        AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getDrawable();
        frameAnimation.start();
    }



}

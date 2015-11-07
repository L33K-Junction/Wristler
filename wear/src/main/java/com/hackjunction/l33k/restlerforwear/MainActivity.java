package com.hackjunction.l33k.restlerforwear;

import android.os.Bundle;
import android.app.Activity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.view.animation.Animation;

/**
 * Created by Valeria on 07.11.2015.
 */
public class MainActivity extends Activity {

    ImageView imageViewStart, imageViewClosing;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.welcome);

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

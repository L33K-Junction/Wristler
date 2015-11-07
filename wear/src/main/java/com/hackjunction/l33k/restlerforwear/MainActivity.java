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

    ImageView imageView;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.welcome);

        imageView = (ImageView) findViewById(R.id.imageWave);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.wave);

        imageView.startAnimation(animRotate);
    }


}

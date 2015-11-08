package com.hackjunction.l33k.restlerforwear;

import android.app.Activity;
import android.widget.ImageView;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;

public class CloseActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_close);

        imageView = (ImageView) findViewById(R.id.imageWave);

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.goodbye_handwave));

        AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getDrawable();
        frameAnimation.start();
    }
}
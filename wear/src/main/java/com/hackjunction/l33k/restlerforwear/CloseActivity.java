package com.hackjunction.l33k.restlerforwear;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;
import android.widget.TextView;

public class CloseActivity extends Activity {

    private ImageView imageView;
    private TextView doItAgainTv;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_close);

        imageView = (ImageView) findViewById(R.id.imageClose);
        doItAgainTv = (TextView) findViewById(R.id.do_it_again);

        doItAgainTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientationActivity = new Intent(getApplicationContext(), OrientationActivity.class);
                startActivity(orientationActivity);
            }
        });
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.goodbye_handwave));
        AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getDrawable();
        frameAnimation.start();
    }
}
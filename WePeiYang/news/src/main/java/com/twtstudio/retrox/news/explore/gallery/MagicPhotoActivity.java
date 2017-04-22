package com.twtstudio.retrox.news.explore.gallery;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.orhanobut.logger.Logger;
import com.twtstudio.retrox.news.R;

/**
 * Created by retrox on 21/04/2017.
 */

public class MagicPhotoActivity extends AppCompatActivity {

    private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gyroscopeObserver = new GyroscopeObserver();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_photo_magic);

        String url = getIntent().getStringExtra("url");
        Logger.d(url);

        PanoramaImageView imageView = (PanoramaImageView) findViewById(R.id.panorama_imageview);
        imageView.setGyroscopeObserver(gyroscopeObserver);
        Glide.with(this).load(url).placeholder(R.drawable.vista_title).crossFade().into(imageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeObserver.unregister();
    }
}

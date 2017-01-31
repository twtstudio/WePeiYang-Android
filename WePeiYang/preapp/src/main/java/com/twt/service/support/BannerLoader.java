package com.twt.service.support;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twt.service.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by tjliqy on 2016/10/29.
 */

public class BannerLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}

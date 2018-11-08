package com.yookiely.lostfond2.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lostfond2.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImagineLoader extends ImageLoader {

    private View view;

    public GlideImagineLoader(View view) {
        this.view = view;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.lf_detail_np)
                .error(R.drawable.lf_detail_np)
                .into(imageView);

        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.lf_detail_np)
                .error(R.drawable.lf_detail_np)
                .into((ImageView) view.findViewById(R.id.iv_detail_banner_img));
    }

}

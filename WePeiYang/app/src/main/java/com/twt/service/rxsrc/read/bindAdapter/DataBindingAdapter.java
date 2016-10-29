package com.twt.service.rxsrc.read.bindAdapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twt.service.R;

/**
 * Created by jcy on 16-10-21.
 * @TwtStudio Mobile Develope Team
 */

public class DataBindingAdapter {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView iv, String imageUrl) {
//        if (!TextUtils.isEmpty(imageUrl))
            Glide.with(iv.getContext()).load(imageUrl).placeholder(R.drawable.default_cover).into(iv);
    }

    @BindingAdapter({"avatarUrl"})
    public static void loadAvatarImage(ImageView iv, String imageUrl) {
//        if (!TextUtils.isEmpty(imageUrl))
        Glide.with(iv.getContext()).load(imageUrl).placeholder(R.drawable.ssdk_oks_logo_linkedin).into(iv);
    }
    @BindingAdapter({"body"})
    public static void loadBody(WebView webView, String body) {
        if (!TextUtils.isEmpty(body))
            webView.loadData(body, "text/html; charset=UTF-8", null);
    }

//    @BindingAdapter({"datetime"})
//    public static void loadDatetime(TextView textView, int datetime) {
//        textView.setText(DailyUtils.getDisplayDate(textView.getContext(), datetime));
//    }

}

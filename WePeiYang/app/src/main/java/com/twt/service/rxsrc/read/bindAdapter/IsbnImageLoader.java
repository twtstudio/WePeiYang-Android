package com.twt.service.rxsrc.read.bindAdapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.twt.service.R;
import com.twt.service.WePeiYangApp;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.model.read.BookCover;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by jcy on 2016/11/3.
 */

public class IsbnImageLoader {
    private  Context mContext;
    private ImageView mImageView;
    private String mImageUrl;
    private String mIsbn;
    private static final String TAG = "IsbnImageLoader";

    public IsbnImageLoader(Context context) {
        mContext = context;
    }

    public static IsbnImageLoader with(Context context){
        return new IsbnImageLoader(context);
    }

    public IsbnImageLoader load(String isbn){
        mIsbn = isbn;
        return this;
    }

    public IsbnImageLoader into(ImageView imageView){
        mImageView = imageView;
        ReadApiClient.getInstance().getBookCover(mContext,mIsbn, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                mImageView.setImageResource(R.drawable.default_cover);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
                mImageView.setImageResource(R.drawable.default_cover);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String string = null;
                try {
                    string = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonStr = string.substring(11,string.length()-1);
                Gson gson = new Gson();
                BookCover bookCover = gson.fromJson(jsonStr,BookCover.class);
                mImageUrl = bookCover.result.get(0).coverlink;
                Glide.with(mContext).load(mImageUrl)
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover)
                        .into(mImageView);
            }

        });
        return this;
    }
}

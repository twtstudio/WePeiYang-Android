package com.twt.service.rxsrc.read.bindAdapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twt.service.R;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.model.read.BookCover;

import rx.Subscriber;

/**
 * Created by jcy on 2016/11/3.
 */

public class IsbnImageLoader {
    private  Context mContext;
    private ImageView mImageView;
    private String mImageUrl;
    private String mIsbn;

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
        ReadApiClient.getInstance().getBookCover(mContext,mIsbn, new Subscriber<BookCover>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BookCover bookCover) {
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

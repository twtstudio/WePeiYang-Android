package com.twtstudio.retrox.tjulibrary.home;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.kelin.mvvmlight.base.ViewModel;
import com.tapadoo.alerter.Alerter;
import com.twtstudio.retrox.tjulibrary.R;
import com.twtstudio.retrox.tjulibrary.provider.Book;

/**
 * Created by retrox on 2017/2/21.
 */

public class BookItemViewModel implements ViewModel {

    private Context mContext;

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableField<String> retrunTime = new ObservableField<>();

    public final ObservableField<Drawable> obBookDrawable = new ObservableField<>();

    public BookItemViewModel(Context context,Book book) {
        mContext = context;
        name.set(book.title);
        retrunTime.set("应还日期: "+book.returnTime);
        setBookDrawable(book);
    }

    private void setBookDrawable(Book book){
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.lib_book);
        int leftDays = book.timeLeft();
        if (leftDays > 20){
            DrawableCompat.setTint(drawable, Color.rgb(0,167,224)); //blue
        }else if (leftDays > 10){
            DrawableCompat.setTint(drawable,Color.rgb(42,160,74)); //green
        }else if (leftDays > 0){
            if (leftDays<5){
                Alerter.create((Activity) mContext)
                        .setTitle("还书提醒")
                        .setBackgroundColor(R.color.assist_color_2)
                        .setText(book.title+"剩余时间不足5天，请尽快还书")
                        .show();
            }
            DrawableCompat.setTint(drawable,Color.rgb(160,42,42)); //red
        }else {
            drawable = ContextCompat.getDrawable(mContext,R.drawable.lib_warning);
        }
        obBookDrawable.set(drawable);
    }
}

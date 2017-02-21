package com.twtstudio.retrox.tjulibrary.home;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.tjulibrary.R;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by retrox on 2017/2/21.
 */

public class HomeLibItemViewModel implements ViewModel {

    private Context mContext;

    public TjuLibProvider libProvider;

    public final ObservableField<String> message = new ObservableField<>();

    public final ObservableBoolean isProgressing = new ObservableBoolean(true);

    public final ObservableBoolean haveBooks = new ObservableBoolean(true);

    // ok:0 progressing:1 warning:2
    public final ObservableInt state = new ObservableInt(1);

    //指示刷新或者，其他的图标
    public final ObservableField<Drawable> obDrawable = new ObservableField<>();

    public final ObservableArrayList<ViewModel> viewModels = new ObservableArrayList<>();



    private Drawable okImage;
    private Drawable warningImage;

    public HomeLibItemViewModel(Context mContext) {
        this.mContext = mContext;
        libProvider = new TjuLibProvider(mContext);
        initDrawable();
        init();
        obDrawable.set(okImage);
    }

    private void initDrawable(){
        okImage = ContextCompat.getDrawable(mContext, R.drawable.lib_ok);
        warningImage = ContextCompat.getDrawable(mContext,R.drawable.lib_warning);
    }

    private void init(){
        state.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (state.get()==0){
                    isProgressing.set(false);
                    obDrawable.set(okImage);
                }else if (state.get()==2){
                    isProgressing.set(false);
                    obDrawable.set(warningImage);
                }else {
                    isProgressing.set(true);
                }
            }
        });

        libProvider.getUserInfo(info -> {
            state.set(0);
            message.set("刷新完成");
            //添加当前书列表
            if (null == info.books||info.books.size() == 0){
                haveBooks.set(false);
            }

        });
    }




}

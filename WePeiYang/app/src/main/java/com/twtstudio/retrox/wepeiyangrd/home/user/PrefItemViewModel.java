package com.twtstudio.retrox.wepeiyangrd.home.user;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.wepeiyangrd.R;

/**
 * Created by retrox on 2017/1/14.
 */

public class PrefItemViewModel implements ViewModel {

    public static final int SETTINGS = 0;
    public static final int NIGHTMODE = 1;
    public static final int TJUOFFICAL = 2;


    private Context mContext;

    private int mMode = 0;

    public final ObservableBoolean preference = new ObservableBoolean(false);

    public final ObservableInt imageRes = new ObservableInt(0);

    public final ObservableField<String> title = new ObservableField<>();

    public ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean switchable = new ObservableBoolean(false);
        public final ObservableBoolean isMargin = new ObservableBoolean(false);
    }

    public ReplyCommand clickCommand = new ReplyCommand(this::onClick);

    public PrefItemViewModel(Context context, int mode) {
        mContext = context;
        mMode = mode;
        init();
    }


    private void init() {

        int mode = mMode;
        if (mode == NIGHTMODE) {
            imageRes.set(R.drawable.ic_nightmode);
            title.set("夜间模式");

            viewStyle.isMargin.set(true);
            viewStyle.switchable.set(true);
            preference.set(CommonPrefUtil.getThemeMode());
        } else if (mode == TJUOFFICAL) {
            // TODO: 2017/1/14 tju bind ?
        } else if (mode == SETTINGS) {
            // TODO: 2017/1/14 jump to settings
        }

        preference.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //ObservableBoolean observableBoolean = (ObservableBoolean) observable;
                if (mMode == NIGHTMODE) {
                    CommonPrefUtil.setThemeMode(preference.get());
                }
            }
        });

    }

    private void onClick() {
        Logger.d("pref click");
    }

}

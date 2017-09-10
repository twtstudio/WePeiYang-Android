package com.twt.service.home.news;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.one.OneDetailBean;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by retrox on 2017/1/16.
 */

public class OneDetailViewModel implements ViewModel {

    /**
     * 用于绑定fragment的生命周期，还有context的提供
     */
//    private BaseFragment mFragment;

    /**
     * fields 与UI绑定的可变数据源
     */
    public final ObservableField<String> imageUrl = new ObservableField<>();

    public final ObservableField<String> content = new ObservableField<>();

    public final ObservableField<String> author = new ObservableField<>();

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    /**
     * 这里暂时没啥乱用
     */
    public class ViewStyle {
        public final ObservableBoolean progressRefreshing = new ObservableBoolean(true);
    }

    public OneDetailViewModel(OneDetailBean.DataBean dataBean) {
        imageUrl.set(dataBean.hp_img_url);
        content.set(dataBean.hp_content);
    }
}

package com.twtstudio.retrox.news.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.api.bean.CommonNewsBean;
import com.twtstudio.retrox.news.api.bean.HomeNewsBean;
import com.twtstudio.retrox.news.detail.NewsDetailsActivity;

/**
 * Created by retrox on 26/02/2017.
 */

public class NewsItemViewModel implements ViewModel {
    private Context mContext;

    public final ObservableInt color = new ObservableInt();
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> brief = new ObservableField<>();
    public final ObservableField<String> type = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableInt index = new ObservableInt();
    public final ObservableBoolean isDisplayBrief = new ObservableBoolean(true);
    public final ReplyCommand clickCommand = new ReplyCommand(()->{
        if (type.get().equals("招聘信息")){
            Uri uri = Uri.parse("http://job.tju.edu.cn/zhaopinhui_detail.php?id="+index.get());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            mContext.startActivity(intent);
        }else {
            NewsDetailsActivity.actionStart(mContext,index.get());
        }

    });

    private static int postion = 0;

    private int[] colors = new int[]{
            R.color.news_item_random_color_1,
            R.color.news_item_random_color_2,
            R.color.news_item_random_color_3,
            R.color.news_item_random_color_4,
            R.color.colorAccent,
            R.color.common_lv1
    };

    private NewsItemViewModel() {
        postion++;
        type.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setColor(type.get());
            }
        });
    }

    public NewsItemViewModel(Context context, HomeNewsBean.DataBean.NewsBean.AnnoucementsBean annoucementsBean) {
        this();
        mContext = context;
        title.set(annoucementsBean.subject);
        brief.set(annoucementsBean.brief+"...");
        type.set(annoucementsBean.gonggao);
        date.set(annoucementsBean.addat);
        index.set(annoucementsBean.index);
    }

    public NewsItemViewModel(Context mContext, HomeNewsBean.DataBean.NewsBean.JobsBean jobsBean) {
        this();
        this.mContext = mContext;
        isDisplayBrief.set(false);
        title.set(jobsBean.title);
        type.set("招聘信息");
        date.set(jobsBean.date);
        index.set(jobsBean.id);

    }

    public NewsItemViewModel(Context mContext, CommonNewsBean.DataBean dataBean){
        this();
        this.mContext = mContext;
        isDisplayBrief.set(true);
        title.set(dataBean.subject);
        brief.set(dataBean.summary);
        type.set("校园新闻");
        index.set(Integer.parseInt(dataBean.index));
//        date.set(dataBean.);

    }


    private void setColor(String type) {
        if (type.equals("招聘信息")) {
            color.set(ContextCompat.getColor(mContext, R.color.news_item_random_color_4));
        } else {
            color.set(ContextCompat.getColor(mContext, colors[postion % 3]));
        }

    }

    private void setColor() {
        color.set(ContextCompat.getColor(mContext, R.color.assist_color_1));
    }

}

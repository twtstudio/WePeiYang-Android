package com.twtstudio.retrox.news.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.R2;
import com.twtstudio.retrox.news.api.NewsApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsDetailsActivity extends AppCompatActivity{

    private static final String INDEX = "index";
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.wv_news_detials)
    WebView wvNewsDetials;
    @BindView(R2.id.pb_news_details)
    ProgressBar pbNewsDetails;
    @BindView(R2.id.tv_news_details_title)
    TextView tvNewsDetailsTitle;
    @BindView(R2.id.tv_news_details_date)
    TextView tvNewsDetailsDate;
    @BindView(R2.id.tv_newsdetails_view_count)
    TextView tvNewsdetailsViewCount;
    @BindView(R2.id.tv_news_details_gonggao)
    TextView tvNewsDetailsGonggao;
    @BindView(R2.id.tv_news_details_shengao)
    TextView tvNewsDetailsShengao;
    @BindView(R2.id.tv_news_details_sheying)
    TextView tvNewsDetailsSheying;
    @BindView(R2.id.tv_news_details_laiyuan)
    TextView tvNewsDetailsLaiyuan;
    @BindView(R2.id.ll_news_details_gonggao)
    LinearLayout llNewsDetailsGonggao;
    @BindView(R2.id.ll_news_details_shengao)
    LinearLayout llNewsDetailsShengao;
    @BindView(R2.id.ll_news_details_sheying)
    LinearLayout llNewsDetailsSheying;
    @BindView(R2.id.ll_news_details_laiyuan)
    LinearLayout llNewsDetailsLaiyuan;
    private int index;
    private Unbinder unbinder;
    private static final String NEWSURL = "http://news.twt.edu.cn/new_mobile/detail.php?id=";
    private News mNews;


    public static void actionStart(Context context, int index) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(INDEX, index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detials);
        unbinder = ButterKnife.bind(this);
        index = getIntent().getIntExtra(INDEX, 0);
        setSupportActionBar(toolbar);
        setTitle("新闻详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getNewsData();
        wvNewsDetials.getSettings().setJavaScriptEnabled(true);
        wvNewsDetials.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        wvNewsDetials.setScrollbarFadingEnabled(true);
        wvNewsDetials.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.news_primary_color));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    public void showProgress() {
        pbNewsDetails.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbNewsDetails.setVisibility(View.GONE);
    }

    public void bindData(News news) {
        hideProgress();
//        mNews = news;
        tvNewsDetailsTitle.setText(news.data.subject);
//        tvNewsDetailsGonggao.setText(news.data.gonggao);
//        tvNewsDetailsShengao.setText(news.data.shengao);
//        tvNewsDetailsLaiyuan.setText(news.data.newscome);
        wvNewsDetials.loadData(news.data.content, "text/html;charset=utf-8", "UTF-8");
        if (TextUtils.isEmpty(news.data.gonggao)) {
            llNewsDetailsGonggao.setVisibility(View.GONE);
        } else {
            tvNewsDetailsGonggao.setText(news.data.gonggao);
        }
        if (TextUtils.isEmpty(news.data.shengao)) {
            llNewsDetailsShengao.setVisibility(View.GONE);
        } else {
            tvNewsDetailsShengao.setText(news.data.shengao);
        }
        if (TextUtils.isEmpty(news.data.newscome)) {
            llNewsDetailsLaiyuan.setVisibility(View.GONE);
        } else {
            tvNewsDetailsLaiyuan.setText(news.data.newscome);
        }
        tvNewsdetailsViewCount.setText(news.data.visitcount + "");
        if (TextUtils.isEmpty(news.data.sheying)) {
            llNewsDetailsSheying.setVisibility(View.GONE);
        } else {
            tvNewsDetailsSheying.setText(news.data.sheying);
        }

    }

    private void getNewsData(){
        NewsApi newsApi = RetrofitProvider.getRetrofit().create(NewsApi.class);
        showProgress();
        newsApi.getNewsDetail(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindData,new RxErrorHandler());
    }
}

package com.twt.service.ui.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.News;
import com.twt.service.interactor.NewsDetailsInteractorImpl;
import com.twt.service.support.share.OnekeyShare;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.news.comments.NewsCommentsActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;

public class NewsDetailsActivity extends BaseActivity implements NewsDetailsView {

    private static final String INDEX = "index";
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.wv_news_detials)
    WebView wvNewsDetials;
    @InjectView(R.id.pb_news_details)
    ProgressBar pbNewsDetails;
    @InjectView(R.id.tv_news_details_title)
    TextView tvNewsDetailsTitle;
    @InjectView(R.id.tv_news_details_date)
    TextView tvNewsDetailsDate;
    @InjectView(R.id.tv_newsdetails_view_count)
    TextView tvNewsdetailsViewCount;
    @InjectView(R.id.tv_news_details_gonggao)
    TextView tvNewsDetailsGonggao;
    @InjectView(R.id.tv_news_details_shengao)
    TextView tvNewsDetailsShengao;
    @InjectView(R.id.tv_news_details_sheying)
    TextView tvNewsDetailsSheying;
    @InjectView(R.id.tv_news_details_laiyuan)
    TextView tvNewsDetailsLaiyuan;
    @InjectView(R.id.ll_news_details_gonggao)
    LinearLayout llNewsDetailsGonggao;
    @InjectView(R.id.ll_news_details_shengao)
    LinearLayout llNewsDetailsShengao;
    @InjectView(R.id.ll_news_details_sheying)
    LinearLayout llNewsDetailsSheying;
    @InjectView(R.id.ll_news_details_laiyuan)
    LinearLayout llNewsDetailsLaiyuan;
    private int index;
    private NewsDetailsPresenter presenter;
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
        ButterKnife.inject(this);
        index = getIntent().getIntExtra(INDEX, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new NewsDetailsPresenterImpl(this, new NewsDetailsInteractorImpl());
        presenter.loadNewsDetails(index);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.news_share:
                if (mNews != null) {
                    ShareSDK.initSDK(this);
                    OnekeyShare oks = new OnekeyShare();
                    oks.disableSSOWhenAuthorize();
                    oks.setTitle(getString(R.string.share));
                    oks.setText(mNews.data.subject);
                    oks.setImagePath(getCacheDir() + "/logo.png");//确保SDcard下面存在此张图片
                    oks.setUrl(NEWSURL + mNews.data.index);
                    oks.show(this);
                }
                break;
            case R.id.news_comment:
                if (mNews != null) {
                    NewsCommentsActivity.actionStart(this, mNews);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        pbNewsDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbNewsDetails.setVisibility(View.GONE);
    }

    @Override
    public void bindData(News news) {
        mNews = news;
        tvNewsDetailsTitle.setText(news.data.subject);
        if (news.data.gonggao.isEmpty()) {
            llNewsDetailsGonggao.setVisibility(View.GONE);
        } else {
            tvNewsDetailsGonggao.setText(news.data.gonggao);
        }
        if (news.data.shengao.isEmpty()) {
            llNewsDetailsShengao.setVisibility(View.GONE);
        } else {
            tvNewsDetailsShengao.setText(news.data.shengao);
        }
        if (news.data.newscome.isEmpty()) {
            llNewsDetailsLaiyuan.setVisibility(View.GONE);
        } else {
            tvNewsDetailsLaiyuan.setText(news.data.newscome);
        }
        tvNewsdetailsViewCount.setText(news.data.visitcount + "");
        if (news.data.sheying.isEmpty()) {
            llNewsDetailsSheying.setVisibility(View.GONE);
        } else {
            tvNewsDetailsSheying.setText(news.data.sheying);
        }
        wvNewsDetials.loadDataWithBaseURL("", news.data.content, "text/html", "UTF-8", "");
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

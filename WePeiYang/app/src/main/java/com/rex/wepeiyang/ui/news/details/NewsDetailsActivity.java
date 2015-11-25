package com.rex.wepeiyang.ui.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.interactor.NewsDetailsInteractor;
import com.rex.wepeiyang.interactor.NewsDetailsInteractorImpl;
import com.rex.wepeiyang.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    private int index;
    private NewsDetailsPresenter presenter;


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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.news_comment:

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
        tvNewsDetailsTitle.setText(news.data.subject);
        tvNewsDetailsGonggao.setText(news.data.gonggao);
        tvNewsDetailsShengao.setText(news.data.shengao);
        tvNewsDetailsLaiyuan.setText(news.data.newscome);
        tvNewsdetailsViewCount.setText(news.data.visitcount + "");
        tvNewsDetailsSheying.setText(news.data.sheying);
        wvNewsDetials.loadDataWithBaseURL("", news.data.content, "text/html", "UTF-8", "");
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

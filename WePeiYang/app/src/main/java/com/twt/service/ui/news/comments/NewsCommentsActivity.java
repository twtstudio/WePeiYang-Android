package com.twt.service.ui.news.comments;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.Comment;
import com.twt.service.bean.News;
import com.twt.service.interactor.NewsDetailsInteractorImpl;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class NewsCommentsActivity extends AppCompatActivity implements NewsCommentsView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rl_news_comments)
    RecyclerView rlNewsComments;
    @InjectView(R.id.et_news_comment)
    EditText etNewsComment;
    @InjectView(R.id.btn_comment_send)
    Button etCommentSend;
    @InjectView(R.id.pb_news_comment)
    ProgressBar pbNewsComment;
    private NewsCommentsPresenterImpl presenter;
    private NewsCommentsAdapter adapter = new NewsCommentsAdapter(this);

    public static void actionStart(Context context, News news) {
        Intent intent = new Intent(context, NewsCommentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", news);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comments);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        presenter = new NewsCommentsPresenterImpl(this, new NewsDetailsInteractorImpl());
        final News news = (News) getIntent().getExtras().getSerializable("news");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlNewsComments.setLayoutManager(new LinearLayoutManager(this));
        if (news != null) {
            adapter.loadItems(news.data.comments);
        }
        rlNewsComments.setAdapter(adapter);
        etCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etNewsComment.getText().toString();
                if (content.isEmpty()) {
                    etNewsComment.setError("评论为空");
                } else {
                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                    String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                    presenter.postComment(news.data.index, content, ip);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.news_primary_color));
        }

    }

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getCallback());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSendBtnClickable(boolean clickable) {
        etCommentSend.setClickable(clickable);
    }

    @Override
    public void addComments(List<Comment> comments) {
        adapter.addItems(comments);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(NewsCommentsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        pbNewsComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbNewsComment.setVisibility(View.GONE);
    }

    @Override
    public void clearEditText() {
        etNewsComment.setText("");
    }
}

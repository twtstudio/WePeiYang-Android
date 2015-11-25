package com.rex.wepeiyang.ui.news.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.News;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsCommentsActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rl_news_comments)
    RecyclerView rlNewsComments;
    @InjectView(R.id.et_news_comment)
    EditText etNewsComment;
    @InjectView(R.id.et_comment_send)
    TextView etCommentSend;
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
        News news = (News) getIntent().getExtras().getSerializable("news");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlNewsComments.setLayoutManager(new LinearLayoutManager(this));
        if (news != null) {
            adapter.loadItems(news.data.comments);
        }
        rlNewsComments.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

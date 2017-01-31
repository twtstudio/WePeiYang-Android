package com.twt.service.party.ui.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class ForumActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.ll_forum_publish)
    LinearLayout llForumPublish;
    @InjectView(R.id.ll_forum_title)
    LinearLayout llForumTitle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_forum;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.forum_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ForumActivity.class);
        context.startActivity(intent);
    }

}

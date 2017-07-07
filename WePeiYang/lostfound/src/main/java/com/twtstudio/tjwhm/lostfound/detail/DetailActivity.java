package com.twtstudio.tjwhm.lostfound.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tjwhm & liuyuesen on 2017/7/5.
 **/

public class DetailActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("物品详情");
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        Toast.makeText(this,String.valueOf(id),Toast.LENGTH_SHORT).show();
    }
}

package com.twtstudio.tjwhm.lostfound.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.success.SuccessActivity;
import com.twtstudio.tjwhm.lostfound.support.IntToType;

import butterknife.BindView;

/**
 * Created by tjwhm & liuyuesen on 2017/7/5.
 **/

public class DetailActivity extends BaseActivity implements DetailContract.DetailView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.detail_pic)
    ImageView detail_pic;
    @BindView(R.id.detail_title)
    TextView detail_title;
    @BindView(R.id.detail_time)
    TextView detail_time;
    @BindView(R.id.detail_place)
    TextView detail_place;
    @BindView(R.id.detail_type)
    TextView detail_type;
    @BindView(R.id.detail_name)
    TextView detail_name;
    @BindView(R.id.detail_phone)
    TextView detail_phone;
    @BindView(R.id.detail_rematks)
    TextView detail_remarks;

    DetailContract.DetailPresenter detailPresenter = new DetailPresenterImpl(this);

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
    protected int getToolbarMenu() {
        return R.menu.detail_menu;
    }

    @Override
    protected void setToolbarMenuClickEvent() {
        super.setToolbarMenuClickEvent();
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.detail_share) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("index", "share");
                intent.putExtras(bundle);
                intent.setClass(this, SuccessActivity.class);
                startActivity(intent);
            }
            return false;
        });
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
        detailPresenter.loadDetailData(id);
    }

    @Override
    public void setDetailData(DetailBean detailData) {
        detail_title.setText(detailData.data.title);
        detail_time.setText(detailData.data.time);
        detail_place.setText(detailData.data.place);
        detail_type.setText(IntToType.getType(detailData.data.detail_type));
        detail_name.setText(detailData.data.name);
        detail_phone.setText(detailData.data.phone);
        detail_remarks.setText(detailData.data.item_description);
    }
}

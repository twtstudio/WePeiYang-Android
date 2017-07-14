package com.twtstudio.tjwhm.lostfound.success;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Objects;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SuccessActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.success_cardView)
    CardView success_cardView;
    @BindView(R.id.share_wechatfriends)
    ImageView share_wechatfriends;
    @BindView(R.id.share_wechatzone)
    ImageView share_wechatzone;
    @BindView(R.id.share_qqfriends)
    ImageView share_qqfriends;
    @BindView(R.id.share_qzone)
    ImageView share_qzone;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_success;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("分享");
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnClickListenerForViews();
        Bundle bundle = getIntent().getExtras();
        String shareOrSuccess = bundle.getString("index");
        if (Objects.equals(shareOrSuccess, "share")) {
            success_cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        // TODO: 2017/7/4 将点击事件换为真正的分享
        if (view == share_wechatfriends) {
            Toast.makeText(this, "share_wechatfriends", Toast.LENGTH_SHORT).show();
        } else if (view == share_wechatzone) {
            Toast.makeText(this, "share_wechatzone", Toast.LENGTH_SHORT).show();
        } else if (view == share_qqfriends) {
            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.QQ)
                    .withText("hello")
                    .setCallback(umShareListener)
                    .share();
            Toast.makeText(this, "share_qqfriends", Toast.LENGTH_SHORT).show();
        } else if (view == share_qzone) {
            Toast.makeText(this, "share_qzone", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnClickListenerForViews() {
        share_wechatfriends.setOnClickListener(this);
        share_wechatzone.setOnClickListener(this);
        share_qqfriends.setOnClickListener(this);
        share_qzone.setOnClickListener(this);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
        //    Log.d("plat","platform"+platform);

            Toast.makeText(SuccessActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SuccessActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
           //     Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SuccessActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}

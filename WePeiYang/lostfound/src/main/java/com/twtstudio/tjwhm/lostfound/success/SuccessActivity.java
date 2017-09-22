package com.twtstudio.tjwhm.lostfound.success;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Objects;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SuccessActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.success_cardView)
    CardView success_cardView;
    @BindView(R2.id.share_wechatfriends)
    ImageView share_wechatfriends;
    @BindView(R2.id.share_wechatzone)
    ImageView share_wechatzone;
    @BindView(R2.id.share_qqfriends)
    ImageView share_qqfriends;
    @BindView(R2.id.share_qzone)
    ImageView share_qzone;
    UMImage image;
    UMWeb web;
    String lostOrFound;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.lf_activity_success;
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
        String imageUrl = bundle.getString("imageUrl");
        String id = bundle.getString("id");
        String place = bundle.getString("place");
        String type = bundle.getString("type");
        String title = bundle.getString("title");
        lostOrFound = bundle.getString("lostOrFound");
        if (imageUrl != null) {
            image = new UMImage(SuccessActivity.this, imageUrl);
        }
        web = new UMWeb("http://open.twtstudio.com/lostfound/detail.html#" + id);
        web.setThumb(image);
        web.setTitle(title);
        if (Objects.equals(lostOrFound, "lost")) {
            if (!Objects.equals(type, "其他")) {
                web.setDescription("我不小心在" + place + "把我的" + type + "弄丢了嘤嘤嘤，拜托大家快来帮我找到它！\n");
            } else {
                web.setDescription("我不小心在" + place + "把我的东西弄丢了嘤嘤嘤，拜托大家快来帮我找到它！\n");
            }
        } else {
            if (!Objects.equals(type, "其他")) {
                web.setDescription("又丢东西了吗？我在" + place + "捡到了" + type + "，快来把它接回家吧～\n");
            } else {
                web.setDescription("又丢东西了吗？我在" + place + "捡到了东西，快来把它接回家吧～\n");
            }
        }
        if (Objects.equals(shareOrSuccess, "share")) {
            success_cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
// TODO: 2017/8/19 微信好友和朋友圈的分享功能
        if (view == share_wechatfriends) {
            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share();

        } else if (view == share_wechatzone) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    1);

            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share();

        } else if (view == share_qqfriends) {

            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.QQ)
                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share();
        } else if (view == share_qzone) {
            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.QZONE)
                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share();
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
            Toast.makeText(SuccessActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                //     Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SuccessActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //授权成功
            Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();

            new ShareAction(SuccessActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withText("hello")
                    .setCallback(umShareListener)
                    .share();
        } else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            //授权失败
        }
    }
}

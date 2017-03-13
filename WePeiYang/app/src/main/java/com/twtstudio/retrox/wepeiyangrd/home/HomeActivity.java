package com.twtstudio.retrox.wepeiyangrd.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.home.common.CommonFragment;
import com.twtstudio.retrox.wepeiyangrd.home.news.NewsFragment;
import com.twtstudio.retrox.wepeiyangrd.home.tools.ToolsFragment;
import com.twtstudio.retrox.wepeiyangrd.home.user.UserFragment;
import com.twtstudio.retrox.wepeiyangrd.view.BottomBar;
import com.twtstudio.retrox.wepeiyangrd.view.BottomBarTab;

/**
 * Created by retrox on 2016/12/12.
 * home activity ui
 * ONLY UI
 */

public class HomeActivity extends BaseActivity {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private BaseFragment[] mFragments = new BaseFragment[4];

    private BottomBar mBottomBar;

    private AlertDialog checkTosDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CommonPrefUtil.setIsAcceptTos(false);//check

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowTransition();
        }
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            mFragments[FIRST] = CommonFragment.newInstance();
            mFragments[SECOND] = NewsFragment.newInstance();
            mFragments[THIRD] = ToolsFragment.newInstance();
            mFragments[FOURTH] = UserFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {

            mFragments[FIRST] = findFragment(CommonFragment.class);
            mFragments[SECOND] = findFragment(NewsFragment.class);
            mFragments[THIRD] = findFragment(ToolsFragment.class);
            mFragments[FOURTH] = findFragment(UserFragment.class);

        }

        initView();
    }

    private void setWindowTransition() {
        getWindow().setEnterTransition(new Fade());
        getWindow().setReenterTransition(new Fade());

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTos();
    }

    /**
     * 检查微北洋用户许可状态
     */
    private void initView() {

        checkTosDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("微北洋用户协议")
                .setMessage("继续使用微北洋即代表我授权微北洋为我查询并存储我的办公网,图书馆,自行车等账号信息\n" +
                        "详情请查看【有关第三方账号及数据的补充协议】")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonPrefUtil.setIsAcceptTos(true);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("查看条款详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("https://support.twtstudio.com/topic/5/%E6%9C%89%E5%85%B3%E7%AC%AC%E4%B8%89%E6%96%B9%E5%B8%90%E5%8F%B7%E5%8F%8A%E6%95%B0%E6%8D%AE%E7%9A%84%E8%A1%A5%E5%85%85%E5%8D%8F%E8%AE%AE");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create();

        checkTos();

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        //fake icons
        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_common))
                .addItem(new BottomBarTab(this, R.drawable.ic_news))
                .addItem(new BottomBarTab(this, R.drawable.ic_tools))
                .addItem(new BottomBarTab(this, R.drawable.ic_user));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void checkTos() {
        if (!CommonPrefUtil.getIsAcceptTos()&&!checkTosDialog.isShowing()) {
            checkTosDialog.show();
        }
    }
}

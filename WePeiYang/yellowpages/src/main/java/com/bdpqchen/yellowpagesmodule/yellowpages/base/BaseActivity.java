package com.bdpqchen.yellowpagesmodule.yellowpages.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.AppActivityManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by chen on 17-2-9.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getLayout();

    protected abstract Toolbar getToolbar();

    private Toolbar mToolbar;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnbinder = ButterKnife.bind(this);
        mToolbar = getToolbar();
        initToolbar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);

            tintManager.setStatusBarTintResource(R.color.yp_colorPrimaryDark);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppActivityManager.getActivityManager().addActivity(this);


    }

    private void initToolbar(){
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}

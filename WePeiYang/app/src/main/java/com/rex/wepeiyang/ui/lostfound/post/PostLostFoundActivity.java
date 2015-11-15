package com.rex.wepeiyang.ui.lostfound.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.TabFragmentAdapter;
import com.rex.wepeiyang.ui.lostfound.post.found.PostFoundFragment;
import com.rex.wepeiyang.ui.lostfound.post.lost.PostLostFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostLostFoundActivity extends BaseActivity implements PostLostFoundView {

    @InjectView(R.id.fl_post_lost_found)
    FrameLayout flPostLostFound;
    @InjectView(R.id.tbl_post_lost_found)
    TabLayout tblPostLostFound;
    @InjectView(R.id.vp_post_lost_found)
    ViewPager vpPostLostFound;
    private FragmentManager fragmentManager;
    public static PreEditFragment preEditFragment;
    public static EditFragment editFragment;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PostLostFoundActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lost_found);
        ButterKnife.inject(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        preEditFragment = new PreEditFragment();
        editFragment = new EditFragment();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.fl_post_lost_found, preEditFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_lost_found, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initTabs() {
        List<String> tabList = new ArrayList<>();
        tabList.add("发布丢失信息");
        tabList.add("发布捡到信息");
        tblPostLostFound.addTab(tblPostLostFound.newTab().setText(tabList.get(0)));
        tblPostLostFound.addTab(tblPostLostFound.newTab().setText(tabList.get(1)));
        tblPostLostFound.setupWithViewPager(vpPostLostFound);
        List<Fragment> fragmentList = new ArrayList<>();
        PostLostFragment postLostFragment = new PostLostFragment();
        PostFoundFragment postFoundFragment = new PostFoundFragment();
        fragmentList.add(postLostFragment);
        fragmentList.add(postFoundFragment);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpPostLostFound.setAdapter(adapter);
        tblPostLostFound.setTabsFromPagerAdapter(adapter);
    }
}

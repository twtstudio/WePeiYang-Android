package com.twt.service.ui.lostfound.post;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.twt.service.R;
import com.twt.service.bean.FoundDetails;
import com.twt.service.bean.LostDetails;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.common.TabFragmentAdapter;
import com.twt.service.ui.lostfound.post.found.AddPhotoEvent;
import com.twt.service.ui.lostfound.post.found.PostFoundFragment;
import com.twt.service.ui.lostfound.post.lost.GetPostLostContactInfoEvent;
import com.twt.service.ui.lostfound.post.lost.PostLostFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class PostLostFoundActivity extends BaseActivity implements PostLostFoundView {

    @InjectView(R.id.tbl_post_lost_found)
    TabLayout tblPostLostFound;
    @InjectView(R.id.vp_post_lost_found)
    ViewPager vpPostLostFound;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.et_lost_found_name)
    EditText etLostFoundName;
    @InjectView(R.id.et_lost_found_number)
    EditText etLostFoundNumber;
    private LostDetails lostDetails;
    private FoundDetails foundDetails;
    private static final int ADD_PHOTO = 1234;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PostLostFoundActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, LostDetails lostDetails) {
        Intent intent = new Intent(context, PostLostFoundActivity.class);
        intent.putExtra("lostDetails", lostDetails);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, FoundDetails foundDetails) {
        Intent intent = new Intent(context, PostLostFoundActivity.class);
        intent.putExtra("foundDetails", foundDetails);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lost_found);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etLostFoundName.setText(PrefUtils.getLostFoundContactName());
        etLostFoundNumber.setText(PrefUtils.getLostFoundContactNumber());
        lostDetails = (LostDetails) getIntent().getSerializableExtra("lostDetails");
        foundDetails = (FoundDetails) getIntent().getSerializableExtra("foundDetails");
        initTabs();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.lost_found_primary_color));
        }
    }

    public void onEvent(GetPostLostContactInfoEvent event) {
        String name = etLostFoundName.getText().toString();
        String number = etLostFoundNumber.getText().toString();
        if (name.isEmpty()) {
            etLostFoundName.setError("不能为空");
        } else if (number.isEmpty()) {
            etLostFoundNumber.setError("不能为空");
        } else {
            PrefUtils.setLostFoundContactName(name);
            PrefUtils.setLostFoundContactNumber(number);
            EventBus.getDefault().post(new PostLostContactInfoEvent(name, number));
        }
    }

    public void onEvent(AddPhotoEvent event) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    EventBus.getDefault().post(new AddedPhotoEvent(filePath));
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_lost_found, menu);
        return true;
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
    public void initTabs() {
        List<String> tabList = new ArrayList<>();
        tabList.add("发布丢失信息");
        tabList.add("发布捡到信息");
        tblPostLostFound.addTab(tblPostLostFound.newTab().setText(tabList.get(0)));
        tblPostLostFound.addTab(tblPostLostFound.newTab().setText(tabList.get(1)));
        tblPostLostFound.setTabMode(TabLayout.MODE_FIXED);
        List<Fragment> fragmentList = new ArrayList<>();
        PostLostFragment postLostFragment = new PostLostFragment();
        PostFoundFragment postFoundFragment = new PostFoundFragment();
        fragmentList.add(postLostFragment);
        fragmentList.add(postFoundFragment);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpPostLostFound.setAdapter(adapter);
        tblPostLostFound.setupWithViewPager(vpPostLostFound);
        tblPostLostFound.setTabsFromPagerAdapter(adapter);
        if (lostDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("lostDetails", lostDetails);
            postLostFragment.setArguments(bundle);
        } else if (foundDetails != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("foundDetails", foundDetails);
            postFoundFragment.setArguments(bundle);
            vpPostLostFound.setCurrentItem(1);
        }
    }
}

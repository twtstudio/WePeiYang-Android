package com.twtstudio.tjwhm.lostfound.release;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.detail.DetailBean;
import com.twtstudio.tjwhm.lostfound.detail.DetailContract;
import com.twtstudio.tjwhm.lostfound.detail.DetailPresenterImpl;
import com.twtstudio.tjwhm.lostfound.success.SuccessActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class ReleaseActivity extends BaseActivity
        implements View.OnClickListener, ReleaseContract.ReleaseView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.release_choose_pic)
    ImageView release_choose_pic;
    @BindView(R.id.release_title)
    EditText release_title;
    @BindView(R.id.release_time)
    EditText release_time;
    @BindView(R.id.release_place)
    EditText release_place;
    @BindView(R.id.release_contact_name)
    EditText release_contact_name;
    @BindView(R.id.release_phone)
    EditText release_phone;
    @BindView(R.id.release_remark)
    EditText release_remark;
    @BindView(R.id.release_publish_spinner)
    Spinner release_publish_spinner;
    @BindView(R.id.release_publish_res)
    TextView release_publish_res;
    @BindView(R.id.release_confirm)
    CardView release_confirm;
    @BindView(R.id.release_delete)
    CardView release_delete;
    @BindView(R.id.release_type_recycleriew)
    RecyclerView release_type_recyclerview;
    @BindView(R.id.release_cardinfo)
    CardView release_cardinfo;
    @BindView(R.id.release_cardinfo_noname)
    CardView release_cardinfo_noname;
    @BindView(R.id.release_card_num)
    EditText release_card_num;
    @BindView(R.id.release_card_name)
    EditText release_card_name;
    @BindView(R.id.release_card_num_noname)
    EditText release_card_num_noname;

    int duration = 1;
    String lostOrFound;
    ReleaseContract.ReleasePresenter releasePresenter = new ReleasePresenterImpl(this);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
    ReleaseTableAdapter tableAdapter;
    DetailContract.DetailView detailView;
    int id;
    int selectedItemPosition = 0;
    List<Uri> selectedPic = new ArrayList<>();


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_release;
    }

    @Override
    protected Toolbar getToolbarView() {
        if (Objects.equals(lostOrFound, "lost")) {
            toolbar.setTitle("发布丢失");
        } else if (Objects.equals(lostOrFound, "found")) {
            toolbar.setTitle("发布捡到");
        } else {
            toolbar.setTitle("编辑");
        }
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        lostOrFound = bundle.getString("lostOrFound");
        super.onCreate(savedInstanceState);
        release_delete.setVisibility(View.GONE);
        if (Objects.equals(lostOrFound, "editLost") || Objects.equals(lostOrFound, "editFound")) {
            release_delete.setVisibility(View.VISIBLE);
            id = bundle.getInt("id");
            selectedItemPosition = bundle.getInt("type") - 1;
            onTypeItemSelected(selectedItemPosition);
            DetailContract.DetailPresenter detailPresenter = new DetailPresenterImpl(detailView);
            detailPresenter.loadDetailDataForEdit(id, this);
        }

        initSpinner();
        release_type_recyclerview.setLayoutManager(layoutManager);
        release_type_recyclerview.setAdapter(tableAdapter);
        drawRecyclerView(selectedItemPosition);
        onTypeItemSelected(selectedItemPosition);

        release_choose_pic.setOnClickListener(view -> Matisse.from(ReleaseActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .theme(R.style.Matisse_Zhihu)
                .forResult(2));
    }

    private void initSpinner() {
        final long dateInt[] = {7, 15, 30};
        final List<String> spinnerList = new ArrayList<>();
        spinnerList.add("7天");
        spinnerList.add("15天");
        spinnerList.add("30天");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        release_publish_spinner.setAdapter(adapter);
        release_publish_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final Date dateToShow = new Date(System.currentTimeMillis() + dateInt[i] * 86400L * 1000L);
                SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                release_publish_res.setText("刊登至" + ft.format(dateToShow));
                duration = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        release_confirm.setOnClickListener(this);
        release_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == release_confirm && (Objects.equals(lostOrFound, "lost") || Objects.equals(lostOrFound, "found"))) {
            if (selectedPic.size() != 0) {
                File file = new File(handleImageOnKitKat(selectedPic.get(0)));
                releasePresenter.uploadReleaseDataWithPic(getUpdateMap(), lostOrFound, file);

            } else {
                releasePresenter.uploadReleaseData(getUpdateMap(), lostOrFound);
            }
        } else if (view == release_confirm) {
            releasePresenter.updateEditData(getUpdateMap(), lostOrFound, id);
        } else if (view == release_delete) {
            releasePresenter.delete(id);
        }
    }

    @Override
    public void successCallBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("shareOrSuccess", "success");
        intent.putExtras(bundle);
        intent.setClass(this, SuccessActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setEditData(DetailBean detailBean) {
        release_title.setText(detailBean.data.title);
        release_time.setText(detailBean.data.time);
        release_place.setText(detailBean.data.place);
        release_phone.setText(detailBean.data.phone);
        release_contact_name.setText(detailBean.data.name);
        release_remark.setText(detailBean.data.item_description);
    }

    @Override
    public void deleteSuccessCallBack() {
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        finish();
    }


    private Map<String, Object> getUpdateMap() {
        String titleString = release_title.getText().toString();
        String nameString = release_contact_name.getText().toString();
        String phoneString = release_phone.getText().toString();
        String timeString = release_time.getText().toString();
        String placeString = release_place.getText().toString();
        String remarksString = release_remark.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("title", titleString);
        map.put("time", timeString);
        map.put("place", placeString);
        map.put("name", nameString);
        map.put("detail_type", selectedItemPosition + 1);
        map.put("phone", phoneString);
        map.put("duration", duration);
        map.put("item_description", remarksString);
        if (selectedItemPosition == 0 || selectedItemPosition == 1) {
            String card_numString = release_card_num.getText().toString();
            String card_nameString = release_card_name.getText().toString();
            map.put("card_number", card_numString);
            map.put("card_name", card_nameString);
        } else if (selectedItemPosition == 9) {
            String card_numString = release_card_num_noname.getText().toString();
            map.put("card_number", card_numString);
            map.put("card_name", nameString);
        } else if (selectedItemPosition == 12) {
            map.put("other_tag", " ");
        }
        return map;
    }

    @Override
    public void drawRecyclerView(int position) {
        tableAdapter = new ReleaseTableAdapter(this, position, this);
        release_type_recyclerview.setAdapter(tableAdapter);
    }

    @Override
    public void onTypeItemSelected(int position) {
        selectedItemPosition = position;
        if (position == 0 || position == 1) {
            release_cardinfo.setVisibility(View.VISIBLE);
            release_cardinfo_noname.setVisibility(View.GONE);
        } else if (position == 9) {
            release_cardinfo_noname.setVisibility(View.VISIBLE);
            release_cardinfo.setVisibility(View.GONE);
        } else {
            release_cardinfo_noname.setVisibility(View.GONE);
            release_cardinfo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("abcdef" + String.valueOf(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && requestCode != 0) {
            selectedPic = Matisse.obtainResult(data);
            Glide.with(this).load(selectedPic.get(0)).into(release_choose_pic);
        }
    }

    private String handleImageOnKitKat(Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }
}

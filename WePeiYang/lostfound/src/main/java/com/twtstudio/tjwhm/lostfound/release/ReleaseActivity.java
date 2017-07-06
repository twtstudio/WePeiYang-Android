package com.twtstudio.tjwhm.lostfound.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.search.SearchActivity;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class ReleaseActivity extends BaseActivity
        implements View.OnClickListener, ReleaseContract.ReleaseView {

    ReleaseContract.ReleasePresenter releasePresenter = new ReleasePresenterImpl(this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_release;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("发布");
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
        super.onCreate(savedInstanceState);
        initSpinner();
    }

    private void initSpinner() {
        final long dateInt[] = {7, 15, 30};
        final List<String> spinnerList = new ArrayList<String>();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        release_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == release_confirm) {
            String titleString = release_title.getText().toString();
            int detail_type = 4;
            String nameString = release_contact_name.getText().toString();
            String phoneString = release_phone.getText().toString();
            String timeString = release_time.getText().toString();
            String placeString = release_place.getText().toString();
            int duration = 1;
            Map<String,Object> map = new HashMap<>();
            map.put("title",titleString);
            map.put("time",timeString);
            map.put("place",placeString);
            map.put("name",nameString);
            map.put("detail_type",4);
            map.put("phone",phoneString);
            map.put("duration",1);
            releasePresenter.updateReleaseData(map);
        }
    }

    @Override
    public void successCallBack() {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void turnToAuth(){
        Toast.makeText(this, "auth", Toast.LENGTH_SHORT).show();
    }
}

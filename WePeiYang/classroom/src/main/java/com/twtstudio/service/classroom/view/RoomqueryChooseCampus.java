package com.twtstudio.service.classroom.view;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.R2;
import com.twtstudio.service.classroom.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomqueryChooseCampus extends AppCompatActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.imageView)
    ImageView imageView;
    @BindView(R2.id.card1)
    CardView card1;
    @BindView(R2.id.imageView2)
    ImageView imageView2;
    @BindView(R2.id.card2)
    CardView card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomquery_choose_campus);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }
        if(PrefUtil.getIsNewCampus())
            imageView2.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.VISIBLE);
        card1.setOnClickListener((v)->{
            PrefUtil.setIsNewCampus(false);
            imageView.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
        });
        card2.setOnClickListener((v)->{
            PrefUtil.setIsNewCampus(true);
            imageView.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

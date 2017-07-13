package com.twtstudio.service.classroom.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppBar extends AppCompatActivity {


    @BindView(R2.id.appBar)
    Toolbar appBar;
    @BindView(R2.id.backButton)
    Button backButton;
    @BindView(R2.id.textview)
    TextView textview;
    @BindView(R2.id.collectButton)
    Button collectButton;
    @BindView(R2.id.positionButton)
    Button positionButton;
    @BindView(R2.id.frame1)
    FrameLayout frame1;
    @BindView(R2.id.condition1)
    TextView condition1;
    @BindView(R2.id.arrow1)
    ImageView arrow1;
    @BindView(R2.id.condition2)
    TextView condition2;
    @BindView(R2.id.arrow2)
    ImageView arrow2;
    @BindView(R2.id.condition3)
    TextView condition3;
    @BindView(R2.id.arrow3)
    ImageView arrow3;
    @BindView(R2.id.mainTable)
    LinearLayout mainTable;
    @BindView(R2.id.card)
    CardView card;
//    PopupWindowViewModel popupWindowViewModel=new PopupWindowViewModel(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomquery_app_bar);
        ButterKnife.bind(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.classroom_popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view);
        condition1.setOnClickListener((v)->{
//            popupWindowViewModel.initData(true,1);
            popupWindow.showAsDropDown(frame1);
        });

    }
}

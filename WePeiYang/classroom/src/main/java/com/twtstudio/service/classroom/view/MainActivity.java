package com.twtstudio.service.classroom.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.R2;
import com.twtstudio.service.classroom.databinding.ActivityClassroomQueryMainBinding;
import com.twtstudio.service.classroom.databinding.ClassroomPopupWindowBinding;
import com.twtstudio.service.classroom.utils.PrefUtil;
import com.twtstudio.service.classroom.utils.TimeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/classroom/main")
public class MainActivity extends RxAppCompatActivity {

    PopupWindowViewModel popupWindowViewModel;
    MainActivityViewModel viewModel = new MainActivityViewModel(this);
    ClassroomPopupWindowBinding popupBinding;
    ActivityClassroomQueryMainBinding mainBinding;
    private Animation animation, animation2;
    private int seletedTag = 0;
    private boolean isNewCampus= PrefUtil.getIsNewCampus();
    PopupWindow popupWindow;
    private boolean hasPop;
    @BindView(R2.id.condition1)
    RelativeLayout condition1;
    @BindView(R2.id.arrow1)
    ImageView arrow1;
    @BindView(R2.id.condition2)
    RelativeLayout condition2;
    @BindView(R2.id.arrow2)
    ImageView arrow2;
    @BindView(R2.id.condition3)
    RelativeLayout condition3;
    @BindView(R2.id.arrow3)
    ImageView arrow3;
    @BindView(R2.id.toparea)
    LinearLayout toparea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_query_main);
        if(!PrefUtil.getHasChosenCampus()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("选择校区")
                    .setMessage("请选择你所在的校区")
                    .setPositiveButton("北洋园校区", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PrefUtil.setIsNewCampus(true);
                            PrefUtil.setHasChosenCampus(true);
                            viewModel.iniData(46, TimeHelper.getWeekInt(), TimeHelper.getTimeInt(), CommonPrefUtil.getStudentNumber());
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("卫津路校区", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PrefUtil.setIsNewCampus(false);
                            PrefUtil.setHasChosenCampus(true);
                            viewModel.iniData(23, TimeHelper.getWeekInt(), TimeHelper.getTimeInt(), CommonPrefUtil.getStudentNumber());
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }



        hasPop = false;
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.classroom_popup_window, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_classroom_query_main);
//        MainActivityViewModel viewModel = new MainActivityViewModel(this, 46, 2, 5, CommonPrefUtil.getStudentNumber());
        setSupportActionBar(mainBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        mainBinding.setViewModel(viewModel);
        com.kelin.mvvmlight.messenger.Messenger.getDefault().send(viewModel, "setData");
        ButterKnife.bind(this);
        popupBinding = popupBinding.inflate(inflater, (ViewGroup) mainBinding.getRoot(), false);
//        setContentView(popupBinding.getRoot());
        popupWindowViewModel = new PopupWindowViewModel(viewModel);
        popupBinding.setViewModel(popupWindowViewModel);
        popupWindow.setContentView(popupBinding.getRoot());
        String uid = CommonPrefUtil.getUserId();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(8);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.classroom_rotate);//创建动画
        animation.setInterpolator(new LinearInterpolator());//
        animation.setFillAfter(!animation.getFillAfter());//
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.classroom_rotate2);//创建动画
        animation2.setInterpolator(new LinearInterpolator());//
        animation2.setFillAfter(!animation.getFillAfter());//

        popupWindow.setOnDismissListener(() -> {
            switch (seletedTag) {
                case 1:
                    disimissPopupWindow(arrow1);
                    break;
                case 2:
                    disimissPopupWindow(arrow2);
                    break;
                case 3:
                    disimissPopupWindow(arrow3);
                    break;
            }
        });
        condition1.setOnClickListener((v) -> {
            if (!hasPop) {
                popupWindowViewModel.initData(PrefUtil.getIsNewCampus(), 1);
                popupWindow.showAsDropDown(toparea);
                arrow1.startAnimation(animation);
            }
            hasPop = !hasPop;
            seletedTag = 1;
        });
        condition2.setOnClickListener((v) -> {
            if (!hasPop) {
                popupWindowViewModel.initData(PrefUtil.getIsNewCampus(), 2);
                popupWindow.showAsDropDown(toparea);
                arrow2.startAnimation(animation);
            }
            hasPop = !hasPop;
            seletedTag = 2;
        });
        condition3.setOnClickListener((v) -> {
            if (!hasPop) {
                popupWindowViewModel.initData(PrefUtil.getIsNewCampus(), 3);
                popupWindow.showAsDropDown(toparea);
                arrow3.startAnimation(animation);
            }
            hasPop = !hasPop;
            seletedTag = 3;
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_roomquery, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.room_collected) {
            Intent intent = new Intent(MainActivity.this, CollectedPage.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.campus_choose) {
            Intent intent = new Intent(MainActivity.this, RoomqueryChooseCampus.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void disimissPopupWindow(ImageView imageView) {
        if (hasPop) {
            imageView.startAnimation(animation2);
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onResume() {
        if(isNewCampus!=PrefUtil.getIsNewCampus()) {
            PopupItemViewModel.resetBuildingAndTime();
            initData();
            isNewCampus=PrefUtil.getIsNewCampus();
        }
        else
            viewModel.onRefresh();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        PopupItemViewModel.resetBuildingAndTime();
        super.onDestroy();
    }
    private void initData(){
        int building;
        if (PrefUtil.getIsNewCampus())
            building=46;
        else
            building=23;
        viewModel.condition1.set(building+"楼");
        viewModel.condition2.set("现在");
        viewModel.condition3.set("全部");
        viewModel.refreshData(building);
    }
}


package com.twtstudio.service.classroom.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.R2;
import com.twtstudio.service.classroom.databinding.ActivityClassroomQueryMainBinding;
import com.twtstudio.service.classroom.databinding.ClassroomPopupWindowBinding;
import com.twtstudio.service.classroom.model.TimeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/classroom/main")
public class MainActivity extends RxAppCompatActivity {

    PopupWindowViewModel popupWindowViewModel = new PopupWindowViewModel(this);
    ClassroomPopupWindowBinding popupBinding;
    ActivityClassroomQueryMainBinding mainBinding;
    private Animation animation, animation2;
    private int seletedTag = 0;
    PopupWindow popupWindow;
    private boolean hasPop;
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
    @BindView(R2.id.wrongTextView)
    TextView wrongTextView;
    @BindView(R2.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.mainFrame)
    FrameLayout mainFrame;
    @BindView(R2.id.toparea)
    LinearLayout toparea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_query_main);
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
        MainActivityViewModel viewModel = new MainActivityViewModel(this);
        viewModel.iniData(46, TimeHelper.getWeekInt(), TimeHelper.getTimeInt(), CommonPrefUtil.getStudentNumber(),true);
        mainBinding.setViewModel(viewModel);
        com.kelin.mvvmlight.messenger.Messenger.getDefault().send(viewModel, "setData");
        ButterKnife.bind(this);
        popupBinding = popupBinding.inflate(inflater, (ViewGroup) mainBinding.getRoot(), false);
//        setContentView(popupBinding.getRoot());
        popupBinding.setViewModel(popupWindowViewModel);
        popupWindow.setContentView(popupBinding.getRoot());
        popupWindow.setOutsideTouchable(true);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);//创建动画
        animation.setInterpolator(new LinearInterpolator());//
        animation.setFillAfter(!animation.getFillAfter());//
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate2);//创建动画
        animation2.setInterpolator(new LinearInterpolator());//
        animation2.setFillAfter(!animation.getFillAfter());//
        backButton.setOnClickListener((v) -> {
            finish();
        });
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
                popupWindowViewModel.initData(true, 1);
                popupWindow.showAsDropDown(toparea);
            }

            hasPop=!hasPop;
            seletedTag = 1;
        });

    }

    public void disimissPopupWindow(ImageView imageView) {
        if (hasPop) {
            popupWindow.dismiss();
            imageView.startAnimation(animation2);
        }
        if (!hasPop) {
//            refreshData();
            imageView.startAnimation(animation);
        }
//        hasPop = !hasPop;
    }

    @Override
    protected void onDestroy() {
        Messenger.getDefault().unregister(this);
        super.onDestroy();
    }
}


package com.twt.service.party.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.haozhang.lib.SlantedTextView;
import com.twt.service.R;
import com.twt.service.party.bean.StatusIdBean;
import com.twt.service.party.interactor.PersonalStatusInteractorImpl;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.party.ui.forum.ForumActivity;
import com.twt.service.party.ui.inquiry.InquiryActivity;
import com.twt.service.party.ui.notification.NotificationActivity;
import com.twt.service.party.ui.sign.SignActivity;
import com.twt.service.party.ui.study.StudyActivity;
import com.twt.service.party.ui.submit.SubmitActivity;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.bind.BindActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/18.
 */
public class PartyActivity extends BaseActivity implements PartyView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fam)
    FloatingActionsMenu actionsMenu;
    @InjectView(R.id.action_sign)
    FloatingActionButton actionSign;
    @InjectView(R.id.action_study)
    FloatingActionButton actionStudy;
    @InjectView(R.id.action_inquiry)
    FloatingActionButton actionInquiry;
    @InjectView(R.id.action_submit)
    FloatingActionButton actionSubmit;
    @InjectView(R.id.action_forum)
    FloatingActionButton actionForum;
    @InjectView(R.id.tv_party_home_marquee)
    TextView tvPartyHomeMarquee;
    @InjectView(R.id.bt_home_1)
    Button btHome1;
    @InjectView(R.id.bt_home_2)
    Button btHome2;
    @InjectView(R.id.bt_home_3)
    Button btHome3;
    @InjectView(R.id.bt_home_4)
    Button btHome4;
    @InjectView(R.id.bt_home_5)
    Button btHome5;
    @InjectView(R.id.bt_home_6)
    Button btHome6;
    @InjectView(R.id.bt_home_7)
    Button btHome7;
    @InjectView(R.id.bt_home_8)
    Button btHome8;
    @InjectView(R.id.bt_home_9)
    Button btHome9;
    @InjectView(R.id.bt_home_10)
    Button btHome10;
    @InjectView(R.id.bt_home_11)
    Button btHome11;
    @InjectView(R.id.hsv_1)
    HorizontalScrollView hsv1;
    @InjectView(R.id.bt_home_12)
    Button btHome12;
    @InjectView(R.id.bt_home_13)
    Button btHome13;
    @InjectView(R.id.bt_home_14)
    Button btHome14;
    @InjectView(R.id.bt_home_15)
    Button btHome15;
    @InjectView(R.id.bt_home_16)
    Button btHome16;
    @InjectView(R.id.bt_home_17)
    Button btHome17;
    @InjectView(R.id.bt_home_18)
    Button btHome18;
    @InjectView(R.id.bt_home_19)
    Button btHome19;
    @InjectView(R.id.bt_home_20)
    Button btHome20;
    @InjectView(R.id.bt_home_21)
    Button btHome21;
    @InjectView(R.id.bt_home_22)
    Button btHome22;
    @InjectView(R.id.bt_home_23)
    Button btHome23;
    @InjectView(R.id.bt_home_24)
    Button btHome24;
    @InjectView(R.id.bt_home_25)
    Button btHome25;
    @InjectView(R.id.bt_home_26)
    Button btHome26;
    @InjectView(R.id.hsv_2)
    HorizontalScrollView hsv2;
    @InjectView(R.id.bt_home_27)
    Button btHome27;
    @InjectView(R.id.bt_home_28)
    Button btHome28;
    @InjectView(R.id.bt_home_29)
    Button btHome29;
    @InjectView(R.id.bt_home_30)
    Button btHome30;
    @InjectView(R.id.bt_home_31)
    Button btHome31;
    @InjectView(R.id.tv_left_1)
    SlantedTextView tvLeft1;
    @InjectView(R.id.tv_left_2)
    SlantedTextView tvLeft2;
    @InjectView(R.id.tv_left_3)
    SlantedTextView tvLeft3;
    @InjectView(R.id.tv_left_4)
    SlantedTextView tvLeft4;
    @InjectView(R.id.tv_left_5)
    SlantedTextView tvLeft5;
    @InjectView(R.id.tv_left_6)
    SlantedTextView tvLeft6;
    @InjectView(R.id.tv_left_7)
    SlantedTextView tvLeft7;
    @InjectView(R.id.tv_left_8)
    SlantedTextView tvLeft8;
    @InjectView(R.id.tv_left_9)
    SlantedTextView tvLeft9;
    @InjectView(R.id.tv_left_10)
    SlantedTextView tvLeft10;
    @InjectView(R.id.tv_left_11)
    SlantedTextView tvLeft11;
    @InjectView(R.id.tv_left_12)
    SlantedTextView tvLeft12;
    @InjectView(R.id.tv_left_13)
    SlantedTextView tvLeft13;
    @InjectView(R.id.tv_left_14)
    SlantedTextView tvLeft14;
    @InjectView(R.id.tv_left_15)
    SlantedTextView tvLeft15;
    @InjectView(R.id.tv_left_16)
    SlantedTextView tvLeft16;
    @InjectView(R.id.tv_left_17)
    SlantedTextView tvLeft17;
    @InjectView(R.id.tv_left_18)
    SlantedTextView tvLeft18;
    @InjectView(R.id.tv_left_19)
    SlantedTextView tvLeft19;
    @InjectView(R.id.tv_left_20)
    SlantedTextView tvLeft20;
    @InjectView(R.id.tv_left_21)
    SlantedTextView tvLeft21;
    @InjectView(R.id.tv_left_22)
    SlantedTextView tvLeft22;
    @InjectView(R.id.tv_left_23)
    SlantedTextView tvLeft23;
    @InjectView(R.id.tv_left_24)
    SlantedTextView tvLeft24;
    @InjectView(R.id.tv_left_25)
    SlantedTextView tvLeft25;
    @InjectView(R.id.tv_left_26)
    SlantedTextView tvLeft26;
    @InjectView(R.id.tv_left_27)
    SlantedTextView tvLeft27;
    @InjectView(R.id.tv_left_28)
    SlantedTextView tvLeft28;
    @InjectView(R.id.tv_left_29)
    SlantedTextView tvLeft29;
    @InjectView(R.id.tv_left_30)
    SlantedTextView tvLeft30;
    @InjectView(R.id.tv_left_31)
    SlantedTextView tvLeft31;

    private int width;
    @InjectView(R.id.ll_home_1)
    LinearLayout llHome1;
    @InjectView(R.id.ll_home_2)
    LinearLayout llHome2;
    @InjectView(R.id.white_view)
    View whiteView;
    private static final int TYPE_NO = 0;
    private static final int TYPE_DOING = 1;
    private static final int TYPE_FINISH = 2;

    private static final String TAG = "PartyActivity";

    private PartyPresenter presenter;

    private String status;

    List<Button> buttons;

    List<SlantedTextView> leftText;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_home;
    }

    @Override
    public void preInitView() {
        buttons = new ArrayList<>();
        buttons.add(btHome1);
        buttons.add(btHome2);
        buttons.add(btHome3);
        buttons.add(btHome4);
        buttons.add(btHome5);
        buttons.add(btHome6);
        buttons.add(btHome7);
        buttons.add(btHome8);
        buttons.add(btHome9);
        buttons.add(btHome10);
        buttons.add(btHome11);
        buttons.add(btHome12);
        buttons.add(btHome13);
        buttons.add(btHome14);
        buttons.add(btHome15);
        buttons.add(btHome16);
        buttons.add(btHome17);
        buttons.add(btHome18);
        buttons.add(btHome19);
        buttons.add(btHome20);
        buttons.add(btHome21);
        buttons.add(btHome22);
        buttons.add(btHome23);
        buttons.add(btHome24);
        buttons.add(btHome25);
        buttons.add(btHome26);
        buttons.add(btHome27);
        buttons.add(btHome28);
        buttons.add(btHome29);
        buttons.add(btHome30);
        buttons.add(btHome31);
        leftText = new ArrayList<>();
        leftText.add(tvLeft1);
        leftText.add(tvLeft2);
        leftText.add(tvLeft3);
        leftText.add(tvLeft4);
        leftText.add(tvLeft5);
        leftText.add(tvLeft6);
        leftText.add(tvLeft7);
        leftText.add(tvLeft8);
        leftText.add(tvLeft9);
        leftText.add(tvLeft10);
        leftText.add(tvLeft11);
        leftText.add(tvLeft12);
        leftText.add(tvLeft13);
        leftText.add(tvLeft14);
        leftText.add(tvLeft15);
        leftText.add(tvLeft16);
        leftText.add(tvLeft17);
        leftText.add(tvLeft18);
        leftText.add(tvLeft19);
        leftText.add(tvLeft20);
        leftText.add(tvLeft21);
        leftText.add(tvLeft22);
        leftText.add(tvLeft23);
        leftText.add(tvLeft24);
        leftText.add(tvLeft25);
        leftText.add(tvLeft26);
        leftText.add(tvLeft27);
        leftText.add(tvLeft28);
        leftText.add(tvLeft29);
        leftText.add(tvLeft30);
        leftText.add(tvLeft31);


        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        setScrollToMid(hsv1,llHome1);
        setScrollToMid(hsv2,llHome2);
        actionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                whiteView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                whiteView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void initView() {
        presenter = new PartyPresenterImpl(this, new PersonalStatusInteractorImpl());
        if("".equals(PrefUtils.getPrefUserNumber()) || "".equals(PrefUtils.getPrefUserRealname())){
            presenter.loadUserInformation();
        }
        else {
            presenter.loadPersonalStatus();
        }

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return R.menu.menu_party_home;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PartyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void startBindActivity() {
        BindActivity.actionStart(this, NextActivity.Party);
        finish();
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(this, NextActivity.Party);
        finish();
    }

    //floatbarmenu的点击事件
    @OnClick({R.id.action_forum, R.id.action_inquiry, R.id.action_sign, R.id.action_study, R.id.action_submit})
    void onMenuClick(View v) {
        switch (v.getId()) {
            case R.id.action_forum:
                ForumActivity.actionStart(this);
                break;
            case R.id.action_inquiry:
                InquiryActivity.actionStart(this);
                break;
            case R.id.action_submit:
                SubmitActivity.actionStart(this);
                break;
            case R.id.action_study:
                StudyActivity.actionStart(this);
                break;
            case R.id.action_sign:
                SignActivity.actionStart(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_notification:
                NotificationActivity.actionStart(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void toastMsg(String msg) {
        if (msg != null) {
            Toast.makeText(PartyActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void gotInformation() {
        presenter.loadPersonalStatus();
    }

    @Override
    public void bindData(List<StatusIdBean> ids) {
        status = "申请人";
        for (StatusIdBean s : ids) {
            if (s.getStatus() == TYPE_FINISH) {
                buttons.get(s.getId()).setBackgroundResource(R.drawable.shape_button_home_green);
                buttons.get(s.getId()).setTextColor(getResources().getColor(R.color.myTextPrimaryColorGreen));
                leftText.get(s.getId()).setVisibility(View.VISIBLE);
                switch (s.getId()){
                    case 9:
                        status = "入党积极分子";
                        break;
                    case 11:
                        status = "发展对象";
                        break;
                    case 19:
                        status = "预备党员";
                        break;
                    case 30:
                        status = "正式党员";
                        break;
                    default:
                        break;
                }
            }
            if (s.getStatus() == TYPE_DOING) {
                buttons.get(s.getId()).setBackgroundResource(R.drawable.shape_button_home_red);
                buttons.get(s.getId()).setTextColor(getResources().getColor(R.color.myTextPrimaryColorRed));
            }
        }
        tvPartyHomeMarquee.setText("您好"
                +PrefUtils.getPrefUserRealname()
                +"，您处于"
                + status
                +"状态。");
    }

    @OnClick({R.id.bt_home_1, R.id.bt_home_2, R.id.bt_home_3, R.id.bt_home_4, R.id.bt_home_5, R.id.bt_home_6, R.id.bt_home_7, R.id.bt_home_8, R.id.bt_home_9, R.id.bt_home_10, R.id.bt_home_11, R.id.bt_home_12, R.id.bt_home_13, R.id.bt_home_14, R.id.bt_home_15, R.id.bt_home_16, R.id.bt_home_17, R.id.bt_home_18, R.id.bt_home_19, R.id.bt_home_20, R.id.bt_home_21, R.id.bt_home_22, R.id.bt_home_23, R.id.bt_home_24, R.id.bt_home_25, R.id.bt_home_26, R.id.bt_home_27, R.id.bt_home_28, R.id.bt_home_29, R.id.bt_home_30, R.id.bt_home_31})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_home_1:
                break;
            case R.id.bt_home_2:
                break;
            case R.id.bt_home_3:
                break;
            case R.id.bt_home_4:
                break;
            case R.id.bt_home_5:
                break;
            case R.id.bt_home_6:
                break;
            case R.id.bt_home_7:
                break;
            case R.id.bt_home_8:
                break;
            case R.id.bt_home_9:
                break;
            case R.id.bt_home_10:
                break;
            case R.id.bt_home_11:
                break;
            case R.id.bt_home_12:
                break;
            case R.id.bt_home_13:
                break;
            case R.id.bt_home_14:
                break;
            case R.id.bt_home_15:
                break;
            case R.id.bt_home_16:
                break;
            case R.id.bt_home_17:
                break;
            case R.id.bt_home_18:
                break;
            case R.id.bt_home_19:
                break;
            case R.id.bt_home_20:
                break;
            case R.id.bt_home_21:
                break;
            case R.id.bt_home_22:
                break;
            case R.id.bt_home_23:
                break;
            case R.id.bt_home_24:
                break;
            case R.id.bt_home_25:
                break;
            case R.id.bt_home_26:
                break;
            case R.id.bt_home_27:
                break;
            case R.id.bt_home_28:
                break;
            case R.id.bt_home_29:
                break;
            case R.id.bt_home_30:
                break;
            case R.id.bt_home_31:
                break;
        }
    }
    private void setScrollToMid(final HorizontalScrollView sv, final LinearLayout inner){
        ViewTreeObserver vto = inner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sv.smoothScrollBy((inner.getWidth() - width) / 2, 0);
            }
        });
    }
}

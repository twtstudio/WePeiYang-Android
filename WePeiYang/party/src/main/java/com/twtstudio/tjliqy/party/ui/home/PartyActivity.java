package com.twtstudio.tjliqy.party.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.haozhang.lib.SlantedTextView;
import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.StatusIdBean;
import com.twtstudio.tjliqy.party.interactor.PersonalStatusInteractorImpl;
import com.twtstudio.tjliqy.party.support.PrefUtils;
import com.twtstudio.tjliqy.party.ui.BaseActivity;
import com.twtstudio.tjliqy.party.ui.inquiry.InquiryActivity;
import com.twtstudio.tjliqy.party.ui.sign.SignActivity;
import com.twtstudio.tjliqy.party.ui.study.StudyActivity;
import com.twtstudio.tjliqy.party.ui.submit.SubmitActivity;
import com.twtstudio.tjliqy.party.ui.submit.detail.SubmitDetailActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/18.
 */
@Route(path = "/party/main")
public class PartyActivity extends BaseActivity implements PartyView {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.fam)
    FloatingActionsMenu actionsMenu;
    @BindView(R2.id.action_sign)
    FloatingActionButton actionSign;
    @BindView(R2.id.action_study)
    FloatingActionButton actionStudy;
    @BindView(R2.id.action_inquiry)
    FloatingActionButton actionInquiry;
    @BindView(R2.id.action_submit)
    FloatingActionButton actionSubmit;
    //    @BindView(R.id.action_forum)
//    FloatingActionButton actionForum;
    @BindView(R2.id.tv_party_home_marquee)
    TextView tvPartyHomeMarquee;
    @BindView(R2.id.bt_home_1)
    Button btHome1;
    @BindView(R2.id.bt_home_2)
    Button btHome2;
    @BindView(R2.id.bt_home_3)
    Button btHome3;
    @BindView(R2.id.bt_home_4)
    Button btHome4;
    @BindView(R2.id.bt_home_5)
    Button btHome5;
    @BindView(R2.id.bt_home_6)
    Button btHome6;
    @BindView(R2.id.bt_home_7)
    Button btHome7;
    @BindView(R2.id.bt_home_8)
    Button btHome8;
    @BindView(R2.id.bt_home_9)
    Button btHome9;
    @BindView(R2.id.bt_home_10)
    Button btHome10;
    @BindView(R2.id.bt_home_11)
    Button btHome11;
    @BindView(R2.id.hsv_1)
    HorizontalScrollView hsv1;
    @BindView(R2.id.bt_home_12)
    Button btHome12;
    @BindView(R2.id.bt_home_13)
    Button btHome13;
    @BindView(R2.id.bt_home_14)
    Button btHome14;
    @BindView(R2.id.bt_home_15)
    Button btHome15;
    @BindView(R2.id.bt_home_16)
    Button btHome16;
    @BindView(R2.id.bt_home_17)
    Button btHome17;
    @BindView(R2.id.bt_home_18)
    Button btHome18;
    @BindView(R2.id.bt_home_19)
    Button btHome19;
    @BindView(R2.id.bt_home_20)
    Button btHome20;
    @BindView(R2.id.bt_home_21)
    Button btHome21;
    @BindView(R2.id.bt_home_22)
    Button btHome22;
    @BindView(R2.id.bt_home_23)
    Button btHome23;
    @BindView(R2.id.bt_home_24)
    Button btHome24;
    @BindView(R2.id.bt_home_25)
    Button btHome25;
    @BindView(R2.id.bt_home_26)
    Button btHome26;
    @BindView(R2.id.hsv_2)
    HorizontalScrollView hsv2;
    @BindView(R2.id.bt_home_27)
    Button btHome27;
    @BindView(R2.id.bt_home_28)
    Button btHome28;
    @BindView(R2.id.bt_home_29)
    Button btHome29;
    @BindView(R2.id.bt_home_30)
    Button btHome30;
    @BindView(R2.id.bt_home_31)
    Button btHome31;
    @BindView(R2.id.tv_left_1)
    SlantedTextView tvLeft1;
    @BindView(R2.id.tv_left_2)
    SlantedTextView tvLeft2;
    @BindView(R2.id.tv_left_3)
    SlantedTextView tvLeft3;
    @BindView(R2.id.tv_left_4)
    SlantedTextView tvLeft4;
    @BindView(R2.id.tv_left_5)
    SlantedTextView tvLeft5;
    @BindView(R2.id.tv_left_6)
    SlantedTextView tvLeft6;
    @BindView(R2.id.tv_left_7)
    SlantedTextView tvLeft7;
    @BindView(R2.id.tv_left_8)
    SlantedTextView tvLeft8;
    @BindView(R2.id.tv_left_9)
    SlantedTextView tvLeft9;
    @BindView(R2.id.tv_left_10)
    SlantedTextView tvLeft10;
    @BindView(R2.id.tv_left_11)
    SlantedTextView tvLeft11;
    @BindView(R2.id.tv_left_12)
    SlantedTextView tvLeft12;
    @BindView(R2.id.tv_left_13)
    SlantedTextView tvLeft13;
    @BindView(R2.id.tv_left_14)
    SlantedTextView tvLeft14;
    @BindView(R2.id.tv_left_15)
    SlantedTextView tvLeft15;
    @BindView(R2.id.tv_left_16)
    SlantedTextView tvLeft16;
    @BindView(R2.id.tv_left_17)
    SlantedTextView tvLeft17;
    @BindView(R2.id.tv_left_18)
    SlantedTextView tvLeft18;
    @BindView(R2.id.tv_left_19)
    SlantedTextView tvLeft19;
    @BindView(R2.id.tv_left_20)
    SlantedTextView tvLeft20;
    @BindView(R2.id.tv_left_21)
    SlantedTextView tvLeft21;
    @BindView(R2.id.tv_left_22)
    SlantedTextView tvLeft22;
    @BindView(R2.id.tv_left_23)
    SlantedTextView tvLeft23;
    @BindView(R2.id.tv_left_24)
    SlantedTextView tvLeft24;
    @BindView(R2.id.tv_left_25)
    SlantedTextView tvLeft25;
    @BindView(R2.id.tv_left_26)
    SlantedTextView tvLeft26;
    @BindView(R2.id.tv_left_27)
    SlantedTextView tvLeft27;
    @BindView(R2.id.tv_left_28)
    SlantedTextView tvLeft28;
    @BindView(R2.id.tv_left_29)
    SlantedTextView tvLeft29;
    @BindView(R2.id.tv_left_30)
    SlantedTextView tvLeft30;
    @BindView(R2.id.tv_left_31)
    SlantedTextView tvLeft31;

    private int width;
    @BindView(R2.id.ll_home_1)
    LinearLayout llHome1;
    @BindView(R2.id.ll_home_2)
    LinearLayout llHome2;
    @BindView(R2.id.white_view)
    View whiteView;
    private static final int TYPE_NO = 0;
    private static final int TYPE_DOING = 1;
    private static final int TYPE_FINISH = 2;

    private static final String TAG = "PartyActivity";

    private PartyPresenter presenter;

    private String status;

    private String sign_status;

    private String submitType;

    private int submitId;
    List<Button> buttons;

    List<SlantedTextView> leftText;

    Boolean[] clickable;

    int[] buttonIds = {R.id.bt_home_1, R.id.bt_home_2, R.id.bt_home_3, R.id.bt_home_4, R.id.bt_home_5, R.id.bt_home_6, R.id.bt_home_7, R.id.bt_home_8, R.id.bt_home_9, R.id.bt_home_10, R.id.bt_home_11, R.id.bt_home_12, R.id.bt_home_13, R.id.bt_home_14, R.id.bt_home_15, R.id.bt_home_16, R.id.bt_home_17, R.id.bt_home_18, R.id.bt_home_19, R.id.bt_home_20, R.id.bt_home_21, R.id.bt_home_22, R.id.bt_home_23, R.id.bt_home_24, R.id.bt_home_25, R.id.bt_home_26, R.id.bt_home_27, R.id.bt_home_28, R.id.bt_home_29, R.id.bt_home_30, R.id.bt_home_31};

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
        setScrollToMid(hsv1, llHome1);
        setScrollToMid(hsv2, llHome2);
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

        clickable = new Boolean[31];
        for (int i = 0; i < 31; i++) {
            clickable[i] = false;
        }

    }

    @Override
    public void initView() {
        presenter = new PartyPresenterImpl(this, new PersonalStatusInteractorImpl());
        if ("".equals(PrefUtils.getPrefUserNumber()) || "".equals(PrefUtils.getPrefUserRealname())) {
            presenter.loadUserInformation();
        } else {
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
        return 0;
//        return R.menu.menu_party_home;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PartyActivity.class);
        context.startActivity(intent);
    }

    //floatbarmenu的点击事件
    @OnClick({R2.id.action_inquiry, R2.id.action_sign, R2.id.action_study, R2.id.action_submit})
    void onMenuClick(View v) {
        int id = v.getId();
        if (id == R.id.action_inquiry) {
            InquiryActivity.actionStart(this);
        } else if (id == R.id.action_submit) {
            SubmitActivity.actionStart(this, submitId, submitType, sign_status);
        } else if (id == R.id.action_study) {
            StudyActivity.actionStart(this);
        } else if (id == R.id.action_sign) {
            SignActivity.actionStart(this);
        }
    }

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case R.id.action_notification:
//                NotificationActivity.actionStart(this);
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
                switch (s.getId()) {
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
                switch (s.getId()) {
                    case 0:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 26:
                        submitId = s.getId();
                        submitType = s.getType();
                        sign_status = buttons.get(s.getId()).getText().toString();
                        break;
                }
                clickable[s.getId()] = true;
            }
        }
        setMarquee();
    }

    private void setMarquee() {
        String text = "您好"
                + PrefUtils.getPrefUserRealname()
                + "，您处于"
                + status
                + "状态。";
        if (sign_status != null) {
            text += "您现在可以" + sign_status;
        }
        tvPartyHomeMarquee.setText(text);
    }

    @Override
    public void setMsg(String msg) {
        tvPartyHomeMarquee.setText(msg);
    }

    @OnClick({R2.id.bt_home_1, R2.id.bt_home_2, R2.id.bt_home_3, R2.id.bt_home_4, R2.id.bt_home_5, R2.id.bt_home_6, R2.id.bt_home_7, R2.id.bt_home_8, R2.id.bt_home_9, R2.id.bt_home_10, R2.id.bt_home_11, R2.id.bt_home_12, R2.id.bt_home_13, R2.id.bt_home_14, R2.id.bt_home_15, R2.id.bt_home_16, R2.id.bt_home_17, R2.id.bt_home_18, R2.id.bt_home_19, R2.id.bt_home_20, R2.id.bt_home_21, R2.id.bt_home_22, R2.id.bt_home_23, R2.id.bt_home_24, R2.id.bt_home_25, R2.id.bt_home_26, R2.id.bt_home_27, R2.id.bt_home_28, R2.id.bt_home_29, R2.id.bt_home_30, R2.id.bt_home_31})
    public void onClick(View view) {
        int clickButtonId = 0;
        for (; clickButtonId < 31; clickButtonId++) {
            if (view.getId() == buttonIds[clickButtonId]) {
                break;
            }
        }

//        RCaster caster = new RCaster(R.class,R2.class);
//        Logger.d(caster.cast(view.getId()));
        
        if (clickable[clickButtonId]) {
            switch (clickButtonId) {
                case 0:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 1:
                    StudyActivity.actionStart(this);
                    break;
                case 2:
                    InquiryActivity.actionStart(this);
                    break;
                case 3:
                    break;
                case 4:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 5:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 6:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 7:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 14:
                    break;
                case 15:
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 19:
                    break;
                case 20:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 21:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 22:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 23:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 24:
                    break;
                case 25:
                    SubmitDetailActivity.actionStart(this, submitType, sign_status);
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 29:
                    break;
                case 30:
                    break;
            }
        }

    }

    private void setScrollToMid(final HorizontalScrollView sv, final LinearLayout inner) {
        ViewTreeObserver vto = inner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sv.smoothScrollBy((inner.getWidth() - width) / 2, 0);
            }
        });
    }

    private void toastSorryMsg() {
        Toast.makeText(PartyActivity.this, "请于网页版完成相关操作", Toast.LENGTH_SHORT).show();
    }
}

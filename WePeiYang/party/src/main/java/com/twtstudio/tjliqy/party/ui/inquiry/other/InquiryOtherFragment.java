package com.twtstudio.tjliqy.party.ui.inquiry.other;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.OtherScoreInfo;
import com.twtstudio.tjliqy.party.interactor.InquiryInteractorImpl;
import com.twtstudio.tjliqy.party.support.PrefUtils;
import com.twtstudio.tjliqy.party.ui.BaseFragment;
import com.twtstudio.tjliqy.party.ui.inquiry.appeal.AppealActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryOtherFragment extends BaseFragment implements OtherView {

    private static final String ARG_PAGE = "ARG_PAGE";

    public static final String TYPE_APPLICANT = "applicant";

    public static final String TYPE_ACADEMY = "academy";

    public static final String TYPE_PROBATIONARY = "probationary";

    @BindView(R2.id.ll_inquiry)
    LinearLayout linearLayout;
    @BindView(R2.id.bt_inquiry_to_appeal)
    Button btInquiryToAppeal;
    @BindView(R2.id.tv_inquiry_name)
    TextView tvInquiryName;
    @BindView(R2.id.tv_inquiry_sno)
    TextView tvInquirySno;
    @BindView(R2.id.tv_inquiry_entry_id)
    TextView tvInquiryEntryId;
    @BindView(R2.id.tv_inquiry_time)
    TextView tvInquiryTime;
    @BindView(R2.id.tv_inquiry_practicegrade)
    TextView tvInquiryPracticegrade;
    @BindView(R2.id.tv_inquiry_articalgrade)
    TextView tvInquiryArticalgrade;
    @BindView(R2.id.ll_inquiry_testgrade)
    LinearLayout llInquiryTestgrade;
    @BindView(R2.id.tv_inquiry_testgrade)
    TextView tvInquiryTestgrade;
    @BindView(R2.id.tv_inquiry_ispassed)
    TextView tvInquiryIspassed;
    @BindView(R2.id.tv_inquiry_status)
    TextView tvInquiryStatus;
    @BindView(R2.id.ll_inquiry_practicegrade)
    LinearLayout llInquiryPracticegrade;
    @BindView(R2.id.tv_inquiry_passmust)
    TextView tvInquiryPassmust;
    @BindView(R2.id.ll_inquiry_passmust)
    LinearLayout llInquiryPassmust;
    @BindView(R2.id.tv_inquiry_passchoose)
    TextView tvInquiryPasschoose;
    @BindView(R2.id.ll_inquiry_passchoose)
    LinearLayout llInquiryPasschoose;
    @BindView(R2.id.ll_inquiry_entry_id)
    LinearLayout llInquiryEntryId;
    @BindView(R2.id.ll_inquiry_status)
    LinearLayout llInquiryStatus;
    @BindView(R2.id.tv_error_msg)
    TextView tvErrorMsg;
    private OtherPresenter presenter;

    private int test_id;

    private boolean b_clickable = false;

    public static InquiryOtherFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InquiryOtherFragment pageFragment = new InquiryOtherFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_inquiry_page2;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {
        presenter = new OtherPresenterImpl(this, new InquiryInteractorImpl());
        switch ((int) this.getArguments().get(ARG_PAGE)) {
            case 2:
                presenter.getGrade(TYPE_APPLICANT);
                break;
            case 3:
                presenter.getGrade(TYPE_ACADEMY);
                break;
            case 4:
                presenter.getGrade(TYPE_PROBATIONARY);
                break;
        }
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void create() {

    }

    @OnClick(R2.id.bt_inquiry_to_appeal)
    public void onClick(View v) {
        if (b_clickable) {
            switch (this.getArguments().getInt(ARG_PAGE)) {
                case 2:
                    AppealActivity.actionStart(getActivity(), TYPE_APPLICANT, test_id);
                    break;
                case 3:
                    AppealActivity.actionStart(getActivity(), TYPE_ACADEMY, test_id);
                    break;
                case 4:
                    AppealActivity.actionStart(getActivity(), TYPE_PROBATIONARY, test_id);
            }
        } else {
            toastMsg("无法申诉");
        }

    }

    @Override
    public void bindData(OtherScoreInfo scoreInfo) {
        if (linearLayout != null) {
            linearLayout.setVisibility(View.VISIBLE);
            tvErrorMsg.setVisibility(View.GONE);
            tvInquiryName.setText(PrefUtils.getPrefUserRealname());
            tvInquirySno.setText(PrefUtils.getPrefUserNumber());
            test_id = scoreInfo.getTest_id();
            if (scoreInfo.getShenshu() == 1) {
                btInquiryToAppeal.setBackgroundResource(R.drawable.shape_button_red);
                btInquiryToAppeal.setClickable(true);
                btInquiryToAppeal.setText("申诉");
                b_clickable = true;
            }
            switch ((int) this.getArguments().get(ARG_PAGE)) {
                case 2:
                    llInquiryPracticegrade.setVisibility(View.GONE);
                    tvInquiryEntryId.setText(scoreInfo.getTest_name());
                    tvInquiryTime.setText(scoreInfo.getEntry_time());
                    //Api封装好像有点问题,把笔试成绩封装到了practicegrade
                    tvInquiryTestgrade.setText(scoreInfo.getEntry_practicegrade());
                    tvInquiryArticalgrade.setText(scoreInfo.getEntry_articlegrade());
                    tvInquiryStatus.setText(scoreInfo.getEntry_status());
                    tvInquiryIspassed.setText(scoreInfo.getEntry_ispassed());
                    break;
                case 3:
                    tvInquiryEntryId.setText(scoreInfo.getTest_name());
                    tvInquiryTime.setText(scoreInfo.getEntry_time());
                    tvInquiryPracticegrade.setText(scoreInfo.getEntry_practicegrade());
                    tvInquiryArticalgrade.setText(scoreInfo.getEntry_articlegrade());
                    tvInquiryTestgrade.setText(scoreInfo.getEntry_testgrade());
                    tvInquiryStatus.setText(scoreInfo.getEntry_status());
                    tvInquiryIspassed.setText(scoreInfo.getEntry_ispassed());
                    break;
                case 4:
                    llInquiryTestgrade.setVisibility(View.GONE);
                    llInquiryPassmust.setVisibility(View.VISIBLE);
                    llInquiryPasschoose.setVisibility(View.VISIBLE);
                    llInquiryEntryId.setVisibility(View.GONE);
                    llInquiryStatus.setVisibility(View.GONE);

                    tvInquiryTime.setText(scoreInfo.getEntry_time());
                    tvInquiryPracticegrade.setText(scoreInfo.getEntry_practicegrade());
                    tvInquiryArticalgrade.setText(scoreInfo.getEntry_articlegrade());
                    tvInquiryPasschoose.setText(scoreInfo.getPass_choose());
                    tvInquiryPassmust.setText(scoreInfo.getPass_must());
                    tvInquiryTestgrade.setText(scoreInfo.getEntry_testgrade());
                    tvInquiryIspassed.setText(scoreInfo.getEntry_isallpassed());
            }
        }
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        tvErrorMsg.setText(errorMsg);
    }
}

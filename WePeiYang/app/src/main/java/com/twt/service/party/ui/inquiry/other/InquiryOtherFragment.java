package com.twt.service.party.ui.inquiry.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.bean.OtherScoreInfo;
import com.twt.service.party.interactor.InquiryInteractorImpl;
import com.twt.service.party.ui.BaseFragment;
import com.twt.service.party.ui.inquiry.appeal.AppealActivity;
import com.twt.service.support.PrefUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryOtherFragment extends BaseFragment implements OtherView {

    private static final String ARG_PAGE = "ARG_PAGE";

    public static final String TYPE_APPLICANT = "applicant";

    public static final String TYPE_ACADEMY = "academy";

    public static final String TYPE_PROBATIONARY = "probationary";

    @InjectView(R.id.bt_inquiry_to_appeal)
    Button btInquiryToAppeal;
    @InjectView(R.id.tv_inquiry_name)
    TextView tvInquiryName;
    @InjectView(R.id.tv_inquiry_sno)
    TextView tvInquirySno;
    @InjectView(R.id.tv_inquiry_entry_id)
    TextView tvInquiryEntryId;
    @InjectView(R.id.tv_inquiry_time)
    TextView tvInquiryTime;
    @InjectView(R.id.tv_inquiry_practicegrade)
    TextView tvInquiryPracticegrade;
    @InjectView(R.id.tv_inquiry_articalgrade)
    TextView tvInquiryArticalgrade;
    @InjectView(R.id.ll_inquiry_testgrade)
    LinearLayout llInquiryTestgrade;
    @InjectView(R.id.tv_inquiry_testgrade)
    TextView tvInquiryTestgrade;
    @InjectView(R.id.tv_inquiry_ispassed)
    TextView tvInquiryIspassed;
    @InjectView(R.id.tv_inquiry_status)
    TextView tvInquiryStatus;
    @InjectView(R.id.ll_inquiry_practicegrade)
    LinearLayout llInquiryPracticegrade;
    @InjectView(R.id.tv_inquiry_passmust)
    TextView tvInquiryPassmust;
    @InjectView(R.id.ll_inquiry_passmust)
    LinearLayout llInquiryPassmust;
    @InjectView(R.id.tv_inquiry_passchoose)
    TextView tvInquiryPasschoose;
    @InjectView(R.id.ll_inquiry_passchoose)
    LinearLayout llInquiryPasschoose;
    @InjectView(R.id.ll_inquiry_entry_id)
    LinearLayout llInquiryEntryId;
    @InjectView(R.id.ll_inquiry_status)
    LinearLayout llInquiryStatus;
    @InjectView(R.id.tv_error_msg)
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

    @OnClick(R.id.bt_inquiry_to_appeal)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_inquiry_to_appeal:
                if(b_clickable) {
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
                }else {
                    toastMsg("无法申诉");
                }

        }
    }

    @Override
    public void bindData(OtherScoreInfo scoreInfo) {
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

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        tvErrorMsg.setVisibility(View.VISIBLE);
        tvErrorMsg.setText(errorMsg);
    }
}

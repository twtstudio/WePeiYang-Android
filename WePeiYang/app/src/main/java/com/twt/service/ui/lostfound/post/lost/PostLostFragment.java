package com.twt.service.ui.lostfound.post.lost;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.LostDetails;
import com.twt.service.interactor.LostInteractorImpl;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.LostType;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.lostfound.post.SetContactInfoEvent;
import com.twt.service.ui.lostfound.post.event.LostId;
import com.twt.service.ui.lostfound.post.event.PostLostContactInfoEvent;
import com.twt.service.ui.lostfound.post.lost.event.FailureEvent;
import com.twt.service.ui.lostfound.post.lost.event.GetPostLostContactInfoEvent;
import com.twt.service.ui.lostfound.post.lost.event.SuccessEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/7.
 */
public class PostLostFragment extends BaseFragment implements View.OnClickListener, PostLostView, DatePickerDialog.OnDateSetListener {


    private static ImageView ivTagChosed;
    @InjectView(R.id.choose_mobile_phone)
    ImageView chooseMobilePhone;
    @InjectView(R.id.tag_mobile_phone)
    FrameLayout tagMobilePhone;
    @InjectView(R.id.choose_bank_card)
    ImageView chooseBankCard;
    @InjectView(R.id.tag_bank_card)
    FrameLayout tagBankCard;
    @InjectView(R.id.choose_id_card)
    ImageView chooseIdCard;
    @InjectView(R.id.tag_id_card)
    FrameLayout tagIdCard;
    @InjectView(R.id.choose_key)
    ImageView chooseKey;
    @InjectView(R.id.tag_key)
    FrameLayout tagKey;
    @InjectView(R.id.choose_backpack)
    ImageView chooseBackpack;
    @InjectView(R.id.tag_backpack)
    FrameLayout tagBackpack;
    @InjectView(R.id.choose_computer_pag)
    ImageView chooseComputerPag;
    @InjectView(R.id.tag_computer_pag)
    FrameLayout tagComputerPag;
    @InjectView(R.id.choose_wallet)
    ImageView chooseWallet;
    @InjectView(R.id.tag_wallet)
    FrameLayout tagWallet;
    @InjectView(R.id.choose_watch)
    ImageView chooseWatch;
    @InjectView(R.id.tag_watch)
    FrameLayout tagWatch;
    @InjectView(R.id.choose_udisk)
    ImageView chooseUdisk;
    @InjectView(R.id.tag_udisk)
    FrameLayout tagUdisk;
    @InjectView(R.id.choose_cup)
    ImageView chooseCup;
    @InjectView(R.id.tag_cup)
    FrameLayout tagCup;
    @InjectView(R.id.choose_books)
    ImageView chooseBooks;
    @InjectView(R.id.tag_books)
    FrameLayout tagBooks;
    @InjectView(R.id.iv_post_lost_found_tag)
    ImageView ivPostLostFoundTag;
    @InjectView(R.id.tv_others_tag)
    TextView tvOthersTag;
    //@InjectView(R.id.et_other_tag)
    //EditText etOtherTag;
    //@InjectView(R.id.vsc_tag_others)
    //ViewSwitcher vscTagOthers;
    @InjectView(R.id.tag_others)
    FrameLayout tagOthers;
    //@InjectView(R.id.post_lost_date)
    //Button postLostDate;
    @InjectView(R.id.et_post_lost_detail)
    EditText etPostLostDetail;
    @InjectView(R.id.btn_post_lost_submit)
    Button btnPostLostSubmit;
    @InjectView(R.id.btn_post_lost_change)
    Button btnPostLostChange;
    @InjectView(R.id.choose_others)
    ImageView chooseOthers;
    @InjectView(R.id.et_post_lost_title)
    EditText etPostLostTitle;
    @InjectView(R.id.btn_lost_time)
    Button btnLostTime;
    @InjectView(R.id.et_lost_position)
    EditText etLostPosition;
    @InjectView(R.id.pb_post_lost)
    ProgressBar pbPostLost;
    private int lost_type;
    private PostLostPresenter presenter;
    private String name;
    private String number;
    private String title;
    private String time;
    private String place;
    private String content;
    private boolean isEditObject = false;
    private static final String EDITTEXT_EMPTY_ERROR = "不能为空";
    private static final String IS_NOT_AN_EDIT_OBJECT = "不能修改未发布的项目，请点击发布";
    private static final String PLEASE_CHOOSE_DATE = "请选择时间";
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_lost, container, false);
        ButterKnife.inject(this, view);
        tagMobilePhone.setOnClickListener(this);
        tagBankCard.setOnClickListener(this);
        tagIdCard.setOnClickListener(this);
        tagKey.setOnClickListener(this);
        tagBackpack.setOnClickListener(this);
        tagComputerPag.setOnClickListener(this);
        tagWallet.setOnClickListener(this);
        tagWatch.setOnClickListener(this);
        tagUdisk.setOnClickListener(this);
        tagCup.setOnClickListener(this);
        tagBooks.setOnClickListener(this);
        tagOthers.setOnClickListener(this);
        btnLostTime.setOnClickListener(this);
        btnPostLostChange.setOnClickListener(this);
        btnPostLostSubmit.setOnClickListener(this);
        presenter = new PostLostPresenterImpl(this, new LostInteractorImpl());
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = getArguments().getInt("id");
            if (id > 0) {
                presenter.getLostDetails(id);
                isEditObject = true;
            }
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tag_mobile_phone:
                clearTagState();
                chooseMobilePhone.setVisibility(View.VISIBLE);
                ivTagChosed = chooseMobilePhone;
                lost_type = LostType.MOBILE_PHONE;
                break;
            case R.id.tag_bank_card:
                clearTagState();
                chooseBankCard.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBankCard;
                lost_type = LostType.BANK_CARD;
                break;

            case R.id.tag_id_card:
                clearTagState();
                chooseIdCard.setVisibility(View.VISIBLE);
                ivTagChosed = chooseIdCard;
                lost_type = LostType.ID_CARD;
                break;
            case R.id.tag_key:
                clearTagState();
                chooseKey.setVisibility(View.VISIBLE);
                ivTagChosed = chooseKey;
                lost_type = LostType.KEY;
                break;

            case R.id.tag_backpack:
                clearTagState();
                chooseBackpack.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBackpack;
                lost_type = LostType.BACKPACK;
                break;
            case R.id.tag_computer_pag:
                clearTagState();
                chooseComputerPag.setVisibility(View.VISIBLE);
                ivTagChosed = chooseComputerPag;
                lost_type = LostType.COMPUTER_PAG;
                break;

            case R.id.tag_wallet:
                clearTagState();
                chooseWallet.setVisibility(View.VISIBLE);
                ivTagChosed = chooseWallet;
                lost_type = LostType.WALLET;
                break;
            case R.id.tag_watch:
                clearTagState();
                chooseWatch.setVisibility(View.VISIBLE);
                ivTagChosed = chooseWatch;
                lost_type = LostType.WATCH;
                break;
            case R.id.tag_udisk:
                clearTagState();
                chooseUdisk.setVisibility(View.VISIBLE);
                ivTagChosed = chooseUdisk;
                lost_type = LostType.UDISK;
                break;
            case R.id.tag_cup:
                clearTagState();
                chooseCup.setVisibility(View.VISIBLE);
                ivTagChosed = chooseCup;
                lost_type = LostType.CUP;
                break;

            case R.id.tag_books:
                clearTagState();
                chooseBooks.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBooks;
                lost_type = LostType.BOOK;
                break;
            case R.id.tag_others:
                clearTagState();
                chooseOthers.setVisibility(View.VISIBLE);
                ivTagChosed = chooseOthers;
                lost_type = LostType.OTHERS;
                break;
            case R.id.btn_lost_time:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
                dpd.show(getActivity().getFragmentManager(),"选择日期");
                break;
            case R.id.btn_post_lost_submit:
                title = etPostLostTitle.getText().toString();
                place = etLostPosition.getText().toString();
                content = etPostLostDetail.getText().toString();
                if (title.isEmpty()) {
                    etPostLostTitle.setError(EDITTEXT_EMPTY_ERROR);
                } else if (place.isEmpty()) {
                    etLostPosition.setError(EDITTEXT_EMPTY_ERROR);
                } else if (content.isEmpty()) {
                    etPostLostDetail.setError(EDITTEXT_EMPTY_ERROR);
                } else if (time == null || time.isEmpty()) {
                    toastMessage(PLEASE_CHOOSE_DATE);
                } else {
                    EventBus.getDefault().post(new GetPostLostContactInfoEvent());
                }
                break;
            case R.id.btn_post_found_change:
                if (isEditObject) {
                    title = etPostLostTitle.getText().toString();
                    place = etLostPosition.getText().toString();
                    content = etPostLostDetail.getText().toString();
                    if (title.isEmpty()) {
                        etPostLostTitle.setError(EDITTEXT_EMPTY_ERROR);
                    } else if (place.isEmpty()) {
                        etLostPosition.setError(EDITTEXT_EMPTY_ERROR);
                    } else if (content.isEmpty()) {
                        etPostLostDetail.setError(EDITTEXT_EMPTY_ERROR);
                    } else if (time == null || time.isEmpty()) {
                        toastMessage(PLEASE_CHOOSE_DATE);
                    } else {
                        EventBus.getDefault().post(new GetPostLostContactInfoEvent());
                    }
                } else {
                    toastMessage(IS_NOT_AN_EDIT_OBJECT);
                }
        }

    }

    /*
     * 从我的丢失列表中点击item会post LostId，从而调用该方法
     */


    public void onEvent(PostLostContactInfoEvent event) {
        name = event.getName();
        number = event.getNumber();
        if (!isEditObject) {
            presenter.postLost(PrefUtils.getToken(), title, name, time, place, number, content, lost_type + "");
        } else {
            presenter.editLost(PrefUtils.getToken(), id, title, name, time, place, number, content, lost_type);
        }
    }

    public void onEvent(SuccessEvent event) {
        presenter.onSuccess();
    }

    public void onEvent(FailureEvent event) {
        RetrofitError error = event.getError();
        presenter.onFailure(error);
    }

    public void onEvent(LostDetails lostDetails) {
        bindData(lostDetails);
    }

    public void onEvent(TokenRefreshSuccessEvent event) {
        PrefUtils.setToken(event.getRefreshedToken().data);
        presenter.postLost(event.getRefreshedToken().data, title, name, time, place, number, content, lost_type + "");
    }

    public void onEvent(TokenRefreshFailureEvent event) {
        toastMessage("请重新登录");
        startLoginActivity();
    }

    private void clearTagState() {
        if (ivTagChosed == null) {
            return;
        } else {
            ivTagChosed.setVisibility(View.GONE);
        }
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        pbPostLost.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbPostLost.setVisibility(View.GONE);
    }

    @Override
    public void setSubmitClickable(boolean clickable) {
        btnPostLostSubmit.setClickable(clickable);
    }

    @Override
    public void setChangeClickable(boolean clickable) {
        btnPostLostChange.setClickable(clickable);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(getActivity(), NextActivity.PostLostFound);
    }

    @Override
    public void bindData(LostDetails lostDetails) {
        lost_type = lostDetails.data.lost_type;
        switch (lost_type) {
            case LostType.OTHERS:
                clearTagState();
                chooseOthers.setVisibility(View.VISIBLE);
                ivTagChosed = chooseOthers;
                break;
            case LostType.BANK_CARD:
                clearTagState();
                chooseBankCard.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBankCard;
                break;
            case LostType.ID_CARD:
                clearTagState();
                chooseIdCard.setVisibility(View.VISIBLE);
                ivTagChosed = chooseIdCard;
                break;
            case LostType.KEY:
                clearTagState();
                chooseKey.setVisibility(View.VISIBLE);
                ivTagChosed = chooseKey;
                break;
            case LostType.BACKPACK:
                clearTagState();
                chooseBackpack.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBackpack;
                break;
            case LostType.COMPUTER_PAG:
                clearTagState();
                chooseComputerPag.setVisibility(View.VISIBLE);
                ivTagChosed = chooseComputerPag;
                break;
            case LostType.WATCH:
                clearTagState();
                chooseWatch.setVisibility(View.VISIBLE);
                ivTagChosed = chooseWatch;
                break;
            case LostType.UDISK:
                clearTagState();
                chooseUdisk.setVisibility(View.VISIBLE);
                ivTagChosed = chooseUdisk;
                break;
            case LostType.CUP:
                clearTagState();
                chooseCup.setVisibility(View.VISIBLE);
                ivTagChosed = chooseCup;
                break;
            case LostType.BOOK:
                clearTagState();
                chooseBooks.setVisibility(View.VISIBLE);
                ivTagChosed = chooseBooks;
                break;
            case LostType.MOBILE_PHONE:
                clearTagState();
                chooseMobilePhone.setVisibility(View.VISIBLE);
                ivTagChosed = chooseMobilePhone;
                break;
            case LostType.WALLET:
                clearTagState();
                chooseWallet.setVisibility(View.VISIBLE);
                ivTagChosed = chooseWallet;
                break;
        }
        name = lostDetails.data.name;
        title = lostDetails.data.title;
        place = lostDetails.data.place;
        time = lostDetails.data.time;
        number = lostDetails.data.phone;
        content = lostDetails.data.content;
        etPostLostTitle.setText(title);
        etLostPosition.setText(place);
        btnLostTime.setText(time);
        etPostLostDetail.setText(content);
        EventBus.getDefault().post(new SetContactInfoEvent(name, number));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        time = year + "/" + monthOfYear + "/" + dayOfMonth;
        btnLostTime.setText(time);
    }
}

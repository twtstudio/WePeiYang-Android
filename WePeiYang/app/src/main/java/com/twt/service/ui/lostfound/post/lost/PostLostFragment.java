package com.twt.service.ui.lostfound.post.lost;

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

import com.twt.service.R;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.LostType;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/7.
 */
public class PostLostFragment extends BaseFragment implements View.OnClickListener {


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
    @InjectView(R.id.et_lost_time)
    EditText etLostTime;
    @InjectView(R.id.et_lost_position)
    EditText etLostPosition;
    @InjectView(R.id.pb_post_lost)
    ProgressBar pbPostLost;
    private int lost_type;
    private PostLostPresenterImpl presenter;

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
        btnPostLostChange.setOnClickListener(this);
        btnPostLostSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
            case R.id.btn_post_lost_submit:
                String title = etPostLostTitle.getText().toString();
                String time = etLostTime.getText().toString();
                String place = etLostPosition.getText().toString();
                String content = etPostLostDetail.getText().toString();
                if (title.isEmpty()) {
                    etPostLostTitle.setError("不能为空");
                } else if (time.isEmpty()) {
                    etLostTime.setError("不能为空");
                } else if (place.isEmpty()) {
                    etLostPosition.setError("不能为空");
                } else if (content.isEmpty()) {
                    etPostLostDetail.setError("不能为空");
                } else {

                }
                break;
        }

    }

    private void clearTagState() {
        if (ivTagChosed == null) {
            return;
        } else {
            ivTagChosed.setVisibility(View.GONE);
        }
    }
}

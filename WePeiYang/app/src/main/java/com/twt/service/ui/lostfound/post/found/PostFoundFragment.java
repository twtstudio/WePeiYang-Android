package com.twt.service.ui.lostfound.post.found;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twt.service.R;
import com.twt.service.bean.FoundDetails;
import com.twt.service.interactor.FoundInteractorImpl;
import com.twt.service.support.ImageResizer;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.lostfound.post.SetContactInfoEvent;
import com.twt.service.ui.lostfound.post.event.AddedPhotoEvent;
import com.twt.service.ui.lostfound.post.event.PostFoundContactInfoEvent;
import com.twt.service.ui.lostfound.post.found.event.AddPhotoEvent;
import com.twt.service.ui.lostfound.post.found.event.GetPostFoundContactInfoEvent;
import com.twt.service.ui.lostfound.post.found.event.PostFoundFailureEvent;
import com.twt.service.ui.lostfound.post.found.event.PostFoundSuccessEvent;
import com.twt.service.ui.lostfound.post.found.event.UploadFailureEvent;
import com.twt.service.ui.lostfound.post.found.event.UploadSuccessEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Rex on 2015/8/7.
 */
public class PostFoundFragment extends Fragment implements View.OnClickListener, PostFoundView, DatePickerDialog.OnDateSetListener {
    @InjectView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @InjectView(R.id.iv_photo_added)
    ImageView ivPhotoAdded;
    @InjectView(R.id.btn_post_found_submit)
    Button btnPostFoundSubmit;
    @InjectView(R.id.btn_post_found_change)
    Button btnPostFoundChange;
    @InjectView(R.id.iv_delete_added_photo)
    ImageView ivDeleteAddedPhoto;
    @InjectView(R.id.btn_post_found_time)
    Button btnPostFoundTime;
    @InjectView(R.id.et_post_found_place)
    EditText etPostFoundPlace;
    @InjectView(R.id.et_post_found_details)
    EditText etPostFoundDetails;
    @InjectView(R.id.pb_post_found)
    ProgressBar pbPostFound;
    @InjectView(R.id.et_post_found_title)
    EditText etPostFoundTitle;
    private File mUploadPhoto;
    private PostFoundPresenter presenter;
    private String name;
    private String number;
    private String title;
    private String time;
    private String place;
    private String details;
    private int id;
    private String found_pic;
    private static final String EDITTEXT_EMPTY_ERROR = "不能为空";
    private static final String PLEASE_CHOOSE_DATE = "请选择时间";
    private boolean isAnEditObject = false;
    private boolean isPhotoChanged = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_found, container, false);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        ivAddPhoto.setOnClickListener(this);
        btnPostFoundSubmit.setOnClickListener(this);
        btnPostFoundChange.setOnClickListener(this);
        ivDeleteAddedPhoto.setOnClickListener(this);
        btnPostFoundTime.setOnClickListener(this);
        presenter = new PostFoundPresenterImpl(this, new FoundInteractorImpl());
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = getArguments().getInt("id");
            if (id > 0) {
                presenter.getFoundDetails(id);
                isAnEditObject = true;
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

    public void onEvent(AddedPhotoEvent event) {
        String filePath = event.getFilePath();
        mUploadPhoto = new File(filePath);
        Bitmap addedBitmap = ImageResizer.decodeBitmapFromFile(filePath, ivPhotoAdded.getWidth(), ivPhotoAdded.getHeight());
        ivPhotoAdded.setImageBitmap(addedBitmap);
        ivDeleteAddedPhoto.setVisibility(View.VISIBLE);
    }

    public void onEvent(UploadSuccessEvent event) {
        if (!isAnEditObject) {
            presenter.postFinally(event.getUploads().get(0).url);
        } else {
            presenter.editFinally(event.getUploads().get(0).url);
        }
    }

    public void onEvent(UploadFailureEvent event) {
        presenter.onFailure(event.getError());
    }

    public void onEvent(PostFoundSuccessEvent event) {
        presenter.onSuccess();
    }

    public void onEvent(PostFoundFailureEvent event) {
        presenter.onFailure(event.getError());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_photo:
                isPhotoChanged = true;
                EventBus.getDefault().post(new AddPhotoEvent());
                break;
            case R.id.btn_post_found_submit:
                if (isAnEditObject) {
                    toastMessage("已发布的物品，请点击修改");
                } else {
                    getPostFoundInfo();
                }
                break;
            case R.id.btn_post_found_change:
                if (isAnEditObject) {
                    getPostFoundInfo();
                } else {
                    toastMessage("未发布的物品，请点击发布");
                }

                break;
            case R.id.iv_delete_added_photo:
                mUploadPhoto = null;
                ivPhotoAdded.setImageResource(android.R.color.transparent);
                ivDeleteAddedPhoto.setVisibility(View.GONE);
                break;
            case R.id.btn_post_found_time:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
                dpd.show(getActivity().getFragmentManager(), "选择日期");
                break;
        }
    }

    private void getPostFoundInfo() {
        title = etPostFoundTitle.getText().toString();
        place = etPostFoundPlace.getText().toString();
        details = etPostFoundDetails.getText().toString();
        if (title.isEmpty()) {
            etPostFoundTitle.setError(EDITTEXT_EMPTY_ERROR);
        } else if (time.isEmpty()) {
            toastMessage(PLEASE_CHOOSE_DATE);
        } else if (place.isEmpty()) {
            etPostFoundPlace.setError(EDITTEXT_EMPTY_ERROR);
        } else if (details.isEmpty()) {
            etPostFoundDetails.setError(EDITTEXT_EMPTY_ERROR);
        } else {
            EventBus.getDefault().post(new GetPostFoundContactInfoEvent());
        }
    }

    public void onEvent(PostFoundContactInfoEvent event) {
        name = event.getName();
        number = event.getNumber();
        if (!isAnEditObject) {
            presenter.postFound(PrefUtils.getToken(), title, name, time, place, number, details, mUploadPhoto);
        } else {
            if (isPhotoChanged) {
                presenter.editFound(PrefUtils.getToken(), id, title, name, time, place, number, details, mUploadPhoto);
            } else {
                presenter.editFound(PrefUtils.getToken(), id, title, name, time, place, number, details, found_pic);
            }
        }
    }


    public void onEvent(FoundDetails foundDetails) {
        bindData(foundDetails);
    }

    @Override
    public void showProgress() {
        pbPostFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbPostFound.setVisibility(View.GONE);
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindData(FoundDetails foundDetails) {
        found_pic = foundDetails.data.found_pic;
        Picasso.with(getActivity()).load(found_pic).into(ivPhotoAdded);
        ivDeleteAddedPhoto.setVisibility(View.VISIBLE);
        name = foundDetails.data.name;
        number = foundDetails.data.phone;
        title = foundDetails.data.title;
        time = foundDetails.data.time;
        place = foundDetails.data.place;
        details = foundDetails.data.content;
        etPostFoundTitle.setText(title);
        btnPostFoundTime.setText(time);
        etPostFoundPlace.setText(place);
        etPostFoundDetails.setText(details);
        EventBus.getDefault().post(new SetContactInfoEvent(name, number));
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
    public void setSubmitClickable(boolean clickable) {
        btnPostFoundSubmit.setClickable(clickable);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        time = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
        btnPostFoundTime.setText(time);
    }
}

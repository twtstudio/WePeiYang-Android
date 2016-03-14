package com.twt.service.ui.lostfound.post.found;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.interactor.FoundInteractorImpl;
import com.twt.service.support.ImageResizer;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.lostfound.post.AddedPhotoEvent;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Rex on 2015/8/7.
 */
public class PostFoundFragment extends BaseFragment implements View.OnClickListener, PostFoundView {
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
    @InjectView(R.id.et_post_found_time)
    EditText etPostFoundTime;
    @InjectView(R.id.et_post_found_place)
    EditText etPostFoundPlace;
    @InjectView(R.id.et_post_found_details)
    EditText etPostFoundDetails;
    @InjectView(R.id.pb_post_found)
    ProgressBar pbPostFound;
    private File mUploadPhoto;
    private PostFoundPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_found, container, false);
        ButterKnife.inject(this, view);
        ivAddPhoto.setOnClickListener(this);
        btnPostFoundSubmit.setOnClickListener(this);
        btnPostFoundChange.setOnClickListener(this);
        ivDeleteAddedPhoto.setOnClickListener(this);
        presenter = new PostFoundPresenterImpl(this, new FoundInteractorImpl());
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
        presenter.postFinally(event.getUpload().data.get(0).url);
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
                EventBus.getDefault().post(new AddPhotoEvent());
                break;
            case R.id.btn_post_found_submit:
                break;
            case R.id.btn_post_found_change:
                break;
            case R.id.iv_delete_added_photo:
                mUploadPhoto = null;
                ivPhotoAdded.setImageResource(android.R.color.transparent);
                ivDeleteAddedPhoto.setVisibility(View.GONE);
                break;
        }
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
    public void finishActivity() {
        getActivity().finish();
    }
}

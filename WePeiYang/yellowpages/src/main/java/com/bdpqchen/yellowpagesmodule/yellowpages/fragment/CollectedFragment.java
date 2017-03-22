package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ExpandableListViewCollectedAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ListUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PhoneUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import rx.Subscriber;


/**
 * Created by chen on 17-2-23.
 */

public class CollectedFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener, CollectedFragmentCallBack{

    public static final int REQUEST_CODE_CALL_PHONE = 11;
    private static final int REQUEST_CODE_WRITE_PHONE = 79;

    public static String[] groupStrings = {"我的收藏"};

    public String[][] childStrings1;

    private CollectedFragmentCallBack collectedFragmentCallBack;
    private static ExpandableListView mExpandableListView;
    private static ExpandableListViewCollectedAdapter mAdapter;
    private String callPhoneNum= "";
    private Context mContext;
    private String mWritePhoneName = "";
    private String mWritePhoneNum = "";
    private CollectedFragment mFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_expandable_list_view, container, false);
        mFragment = this;
        mContext = getContext();
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expand_list_view);
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnGroupCollapseListener(this);
        mExpandableListView.setOnGroupExpandListener(this);
        mExpandableListView.setOnChildClickListener(this);
        collectedFragmentCallBack = this;
        mAdapter = new ExpandableListViewCollectedAdapter(getContext(), collectedFragmentCallBack);
        mExpandableListView.setAdapter(mAdapter);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        getCollectedData();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    PhoneUtils.ringUp(mContext, callPhoneNum);
                }else{
                    ToastUtils.show(getActivity(), "请在权限管理中开启微北洋拨打电话权限");
                }
                break;
            case REQUEST_CODE_WRITE_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    PhoneUtils.insertContact(mContext, mWritePhoneName, mWritePhoneNum);
                }else{
                    ToastUtils.show(getActivity(), "请在权限管理中开启微北洋新建联系人权限");
                }
        }
    }

    @Override
    public void callPhone(String phoneNum) {
        this.callPhoneNum = phoneNum;
        PhoneUtils.permissionCheck(mContext, phoneNum, REQUEST_CODE_CALL_PHONE, mFragment);
    }

    @Override
    public void saveToContact(String name, String phone) {
        this.mWritePhoneName = name;
        this.mWritePhoneNum = phone;
        PhoneUtils.permissionCheck(mContext, name, phone, REQUEST_CODE_WRITE_PHONE, mFragment);
    }

    public static void getCollectedData(){
        Subscriber subscriber = new Subscriber<List<Phone>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Log.i("getCollectedDataList", "onError()");
            }

            @Override
            public void onNext(List<Phone> phones) {
                mAdapter.addAllData(groupStrings, phones);
                ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
                HomeActivity.setProgressBarDismiss();
            }
        };
        DatabaseClient.getInstance().getCollectedData(subscriber);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        if (mAdapter.getChildrenCount(0) != 0) {
            ListUtils.setCollapseListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
            ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (mAdapter.getChildrenCount(0) != 0) {
            ListUtils.setExpandedListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
            ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        }
    }


}

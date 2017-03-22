package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.R2;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity.INTENT_TOOLBAR_TITLE;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.tv_title1)
    TextView mTv1;
    @BindView(R2.id.department1)
    LinearLayout mDepartment1;
    @BindView(R2.id.tv_title2)
    TextView mTv2;
    @BindView(R2.id.department2)
    LinearLayout mDepartment2;
    @BindView(R2.id.tv_title3)
    TextView mTv3;
    @BindView(R2.id.department3)
    LinearLayout mDepartment3;
    @BindView(R2.id.tv_title4)
    TextView mTv4;
    @BindView(R2.id.department4)
    LinearLayout mDepartment4;
    @BindView(R2.id.tv_title5)
    TextView mTv5;
    @BindView(R2.id.department5)
    LinearLayout mDepartment5;
    @BindView(R2.id.tv_title6)
    TextView mTv6;
    @BindView(R2.id.department6)
    LinearLayout mDepartment6;
    private String[] titles = new String[]{"1895综合服务大厅", "图书馆", "维修服务中心", "智能自行车", "学生宿舍管理中心", "天大医院"};
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_department, container, false);
//        ButterKnife.inject(this, view);
        //the ButterKnife can not inject in a library. so, I had to add those fucking codes....
        //https://github.com/JakeWharton/butterknife/issues/100

        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void startActivityWithTitle(int i) {
        String title = String.valueOf(titles[i - 1]);
        Intent intent = new Intent(getContext(), DepartmentActivity.class);
        intent.putExtra(INTENT_TOOLBAR_TITLE, title);
        Logger.i(title);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

    }

    @OnClick({R2.id.department1, R2.id.department2, R2.id.department3, R2.id.department4, R2.id.department5, R2.id.department6})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.department1) {
            startActivityWithTitle(1);
        } else if (viewId == R.id.department2) {
            startActivityWithTitle(2);
        } else if (viewId == R.id.department3) {
            startActivityWithTitle(3);
        } else if (viewId == R.id.department4) {
            startActivityWithTitle(4);
        } else if (viewId == R.id.department5) {
            startActivityWithTitle(5);
        } else if (viewId == R.id.department6) {
            startActivityWithTitle(6);
        }
    }
}

package com.twt.service.network.view.spy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twt.service.network.modle.StatusBean;
import com.twt.service.network.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by chen on 2017/7/10.
 */

public class SpyFragment extends Fragment {
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private List<StatusBean.data> mStatusList;
    private Context mContext;
    private SpyAdapter mSpyAdapter;
    private int size = 32;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spy_main, container, false);
        view = binding.getRoot();
        init(view);
        setSrlListener();
        setSmrvListener();
        return view;
    }

    public void init(View view) {
        mContext = this.getActivity();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.fragment_spy_smrw);
        mSpyAdapter = new SpyAdapter(mContext, mStatusList);
        mSwipeMenuRecyclerView.setAdapter(mSpyAdapter);
        mSwipeMenuRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mSwipeMenuRecyclerView.setItemViewSwipeEnabled(true);
        mSwipeMenuRecyclerView.setLongPressDragEnabled(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_apy_srl);
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(R.color.net_icon_background)
                    .setImage(R.drawable.network_spy_wifi_off)
                    .setWidth(size)
                    .setHeight(size)
                    .setText("下线")
                    .setTextColor(R.color.net_white)
                    .setTextSize(16);
            swipeRightMenu.addMenuItem(addItem); //添加一个按钮到右侧菜单
        }
    };

    private void setSrlListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    private void setSmrvListener() {
        mSwipeMenuRecyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                Collections.swap(mStatusList, fromPosition, toPosition);
                mSpyAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onItemDismiss(int position) {
                mStatusList.remove(position);
                mSpyAdapter.notifyItemRemoved(position);
                Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

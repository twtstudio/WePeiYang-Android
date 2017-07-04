package com.twtstudio.tjwhm.lostfound.mylist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twtstudio.tjwhm.lostfound.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/4.
 **/

public class MylistFragment extends Fragment {
    @BindView(R.id.mylist_recyclerView)
    RecyclerView mylist_recyclerView;
    private MylistTableAdapter tableAdapter;
    private LinearLayoutManager layoutManager;
    private MylistBean mylistBean;

    public static MylistFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("index",type);
        MylistFragment fragment = new MylistFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mylist, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new LinearLayoutManager(getActivity());
        mylist_recyclerView.setLayoutManager(layoutManager);
        tableAdapter = new MylistTableAdapter(mylistBean, getActivity());
        mylist_recyclerView.setAdapter(tableAdapter);
        return view;
    }
}

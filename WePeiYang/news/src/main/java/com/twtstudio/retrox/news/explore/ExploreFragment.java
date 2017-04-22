package com.twtstudio.retrox.news.explore;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.api.PicApi;
import com.twtstudio.retrox.news.api.PicProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import me.yokeyword.fragmentation.SupportFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 22/04/2017.
 */

public class ExploreFragment extends SupportFragment {
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private PicApi picApi = new PicProvider().getPicApi();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        virtualLayoutManager = new VirtualLayoutManager(this.getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(5, 20);
        recyclerView.setRecycledViewPool(recycledViewPool);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);

        delegateAdapter.addAdapter(new ToolsAdapter(getActivity()));

        SingleLayoutHelper feedbackLayoutHelper = new SingleLayoutHelper();
        feedbackLayoutHelper.setBgColor(Color.WHITE);
        feedbackLayoutHelper.setMarginBottom(32);
        SingleItem feedbackItem = new SingleItem(getActivity(),feedbackLayoutHelper,R.layout.item_explore_feedback);
        feedbackItem.setOnClickListener(v -> {
            Toasty.info(getActivity(),"feedback!", Toast.LENGTH_SHORT).show();
        });
        delegateAdapter.addAdapter(feedbackItem);

        delegateAdapter.addAdapter(new SingleItem(getActivity(), new SingleLayoutHelper(), R.layout.item_explore_gallery_header));

        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setGap(8);
        gridLayoutHelper.setAutoExpand(true);
        gridLayoutHelper.setBgColor(Color.WHITE);
        GalleryIndexAdapter galleryIndexAdapter = new GalleryIndexAdapter(getActivity(), gridLayoutHelper, new ArrayList<>());

        delegateAdapter.addAdapter(galleryIndexAdapter);
        picApi.getGalleryIndex().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(galleryIndexAdapter::refreshData, Throwable::printStackTrace);

        SingleLayoutHelper headerLayoutHelper = new SingleLayoutHelper();
        headerLayoutHelper.setMarginTop(32);
        SingleItem gridHeader = new SingleItem(getActivity(), headerLayoutHelper, R.layout.item_explore_vista_header);
        delegateAdapter.addAdapter(gridHeader);

        GridLayoutHelper vistaGridHelper = new GridLayoutHelper(3);
        vistaGridHelper.setBgColor(Color.WHITE);
        vistaGridHelper.setAutoExpand(true);
        vistaGridHelper.setGap(8);

        VistaAdapter vistaAdapter = new VistaAdapter(getActivity(), vistaGridHelper, new ArrayList<>());
        delegateAdapter.addAdapter(vistaAdapter);
        picApi.getFangcunPic(1, 0).subscribeOn(Schedulers.io())
                .map(it -> it.data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    Collections.shuffle(it, new Random(System.currentTimeMillis()));
                    vistaAdapter.refreshData(it.subList(0, 5));
                }, Throwable::printStackTrace);

        VistaSingleItemFooter gridFooter = new VistaSingleItemFooter(getActivity(), new SingleLayoutHelper());
        delegateAdapter.addAdapter(gridFooter);

        return view;

    }
}

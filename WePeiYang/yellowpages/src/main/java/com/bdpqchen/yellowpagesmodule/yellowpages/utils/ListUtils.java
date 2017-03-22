package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

/**
 * Created by cdc on 16-11-26.
 */

public class ListUtils {

    private static ListUtils mInstance;
    public static ListUtils getInstance(){
        if(mInstance == null){
            mInstance = new ListUtils();
        }
        return mInstance;
    }

    /** 动态改变listView的高度 */
    public void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // params.height = 80 * (listAdapter.getCount() - 1);
        // params.height = 80 * (listAdapter.getCount());
        int listHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = listHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
        /*ViewGroup.LayoutParams params1 = linearLayout.getLayoutParams();
        int beforeHeight = linearLayout.getHeight();
        params1.height = beforeHeight + listHeight;
        ((ViewGroup.MarginLayoutParams)params1).setMargins(0, 0, 0, 0);*/
//        linearLayout.setLayoutParams(params1);
//        Log.d("listview height origin", String.valueOf(linearLayout.getHeight()));

    }

    /**
     * 可扩展listview展开时调用
     *
     * @param listView
     * @param groupPosition
     */
    public static void setExpandedListViewHeightBasedOnChildren(
            ExpandableListView listView, int groupPosition) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }
        View listItem = listAdapter.getChildView(groupPosition, 0, true, null,
                listView);
        listItem.measure(0, 0);
        int appendHeight = 0;
        for (int i = 0; i < listAdapter.getChildrenCount(groupPosition); i++) {
            appendHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        Log.d(TAG, "Expand params.height" + params.height);
        int listHeight = params.height + appendHeight;
        params.height = listHeight;
        listView.setLayoutParams(params);
        /*ViewGroup.LayoutParams params1 = linearLayout.getLayoutParams();
        int beforeHeight = linearLayout.getHeight();
        params1.height = beforeHeight + listHeight;
        ((ViewGroup.MarginLayoutParams)params1).setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(params1);*/
//        Log.i("linearLayoutheight", String.valueOf(linearLayout.getHeight()));


    }

    /**
     * 可扩展listview收起时调用
     *
     * @param listView
     * @param groupPosition
     */
    public static void setCollapseListViewHeightBasedOnChildren(
            ExpandableListView listView, int groupPosition) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }
        View listItem = listAdapter.getChildView(groupPosition, 0, true, null,
                listView);
        listItem.measure(0, 0);
        int appendHeight = 0;
        for (int i = 0; i < listAdapter.getChildrenCount(groupPosition); i++) {
            appendHeight += listItem.getMeasuredHeight();
        }
        /*Log.d(TAG,
                "Collapse childCount="
                        + listAdapter.getChildrenCount(groupPosition));*/
        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height -= appendHeight;
//        listView.setLayoutParams(params);
        int listHeight = params.height - appendHeight;
        params.height = listHeight;
        listView.setLayoutParams(params);
        /*ViewGroup.LayoutParams params1 = linearLayout.getLayoutParams();
        int beforeHeight = linearLayout.getHeight();
        params1.height = beforeHeight + listHeight;
        ((ViewGroup.MarginLayoutParams)params1).setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(params1);*/
//        Log.i("linearLayout height", String.valueOf(linearLayout.getHeight()));

    }


}

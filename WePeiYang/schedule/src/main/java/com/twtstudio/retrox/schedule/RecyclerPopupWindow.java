package com.twtstudio.retrox.schedule;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;


import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

/**
 * Created by cmj on 2016/4/7.
 */
public class RecyclerPopupWindow extends PopupWindow implements RecyclerPopupWindowAdapter.OnItemClickListener, PopupWindow.OnDismissListener {
    private List<WeekItem> items;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private RecyclerPopupWindowAdapter recyclerPopupWindowAdapter;
    private CallBack mCallBack;
    //本次点击位置
    private int position = -1;
    //上次点击位置
    private int prePosition = -1;
    //是否点击列表项
    private boolean isClickItem;
    //

    public RecyclerPopupWindow(List<WeekItem> items, int currentWeek) {
        this.items = items;
        //获取之前点击的位置
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).isActive()) {
                prePosition = i;
                break;
            }
        }
        if (prePosition == -1){
            if(currentWeek==-1)
                currentWeek=1;
            prePosition = currentWeek - 1;

            //todo 这里出现了数组越界问题 暂时做了捕获处理
            try {
                items.get(currentWeek - 1).setActive(true);
            }catch (Exception e){
                CrashReport.postCatchedException(e);
            }
        }
    }

    /**
     * 弹出向上或下的PopupWindow
     *
     * @param context       上下文
     * @param anchor        锚点
     * @param window_width  窗口宽度
     * @param window_height 窗口高度
     */
    public void showPopupWindow(Context context, View anchor, int window_width, int window_height) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(contentView, window_width, window_height, true);
        //点击window外让window消失必须设置背景,但是不能够提供其他伴随操作，比如让其他控件的隐藏，消失等
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);

        //设置window的消失时TODO
        popupWindow.setOnDismissListener(this);
        //实例化RecyclerView
        recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_function_wash_time);
        //为RecyclerView设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //为RecyclerView设置Adapater
        recyclerPopupWindowAdapter = new RecyclerPopupWindowAdapter(items);
        recyclerPopupWindowAdapter.setOnItemClickListener(this);
        //为RecyclerView设置适配器
        recyclerView.setAdapter(recyclerPopupWindowAdapter);
        //设置默认滑到当前周
        recyclerView.scrollToPosition(prePosition - 1);
        //设置动画从上往下弹出
        popupWindow.setAnimationStyle(R.style.Popwindow_Anim_Down);
        //显示popupWindow，在anchor下方，x无偏移，y无偏移
        popupWindow.showAsDropDown(anchor, 0, 0);
    }
    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onItemClick(int pos) {
        position = pos;
        isClickItem = true;
        //改变选择项并关闭窗口
        changePos(true);
    }

    /**
     * 点击改变选择项并决定是否关闭window
     *
     * @param isCloseWindow 是否要关闭window
     */
    private void changePos(boolean isCloseWindow) {
        //如果两次点击不同上次选择的取消
        if (position != prePosition && prePosition != -1) {
            items.get(prePosition).setActive(false);
            recyclerPopupWindowAdapter.notifyItemChanged(prePosition);
        }
        //更新当前位置的选择项
        if (position >= 0) {
            items.get(position).setActive(true);
            recyclerPopupWindowAdapter.notifyItemChanged(position);
        }
        if (isCloseWindow) {
            //动画消失后再关闭
            (new Handler()).postDelayed(() -> {
                //回调并关闭window
                mCallBack.callback(items.get(position).getTitle(),items.get(position).getWeek_num());
                destroyPopWindow();
            }, 450);
        }
    }

    @Override
    public void onDismiss() {
        //点击的是window外面，回调返回-1说明，时间不用改变
        if (!isClickItem) {
            mCallBack.callback("-1",-1);
        }
    }


    private void destroyPopWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public interface CallBack {
        void callback(String value, int currentWeek);
    }
}

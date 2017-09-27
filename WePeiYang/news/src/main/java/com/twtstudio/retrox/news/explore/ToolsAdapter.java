package com.twtstudio.retrox.news.explore;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.twtstudio.retrox.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by retrox on 22/04/2017.
 */

public class ToolsAdapter extends DelegateAdapter.Adapter<ToolsAdapter.ToolItemHolder> {
    private Context context;
    private List<ToolItem> toolItemList = new ArrayList<>();

    public ToolsAdapter(Context context) {
        this.context = context;
        initList();
    }

    private void initList(){
        toolItemList.add(new ToolItem("/schedule/main",R.drawable.ic_main_schedule,"课程表"));
        toolItemList.add(new ToolItem("/gpa/main",R.drawable.ic_main_gpa,"GPA"));
        toolItemList.add(new ToolItem("/bike/main",R.drawable.ic_main_bike,"自行车"));
        toolItemList.add(new ToolItem("/party/main",R.drawable.ic_main_party,"党建"));
        toolItemList.add(new ToolItem("/read/main",R.drawable.ic_main_read,"阅读"));
        toolItemList.add(new ToolItem("/fellowSearch/main",R.drawable.ic_main_fellow_search,"老乡查询"));
        toolItemList.add(new ToolItem("/yellowPage/main",R.drawable.ic_main_yellowpage,"黄页"));
        toolItemList.add(new ToolItem("/classroom/main",R.drawable.ic_main_classroom_query,"自习室查询"));
        toolItemList.add(new ToolItem("/lostfound/main",R.drawable.ic_main_lostfound,"失物招领"));
        toolItemList.add(new ToolItem("/network/main",R.drawable.ic_main_network,"一键上网"));

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setMarginTop(32);
        gridLayoutHelper.setBgColor(Color.WHITE);
        return gridLayoutHelper;
    }

    @Override
    public ToolItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explore_tool,parent,false);
        return new ToolItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ToolItemHolder holder, int position) {
        ToolItem item = toolItemList.get(position);
        holder.textView.setText(item.title);
        Glide.with(context).load(item.imageRes).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(item.actUrl)
                        .navigation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return toolItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 5;
    }

    static class ToolItemHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;

        public ToolItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

    }

    private class ToolItem{
        public String actUrl;
        @DrawableRes
        public int imageRes;
        public String title;

        public ToolItem(String actUrl, @DrawableRes int imageRes, String title) {
            this.actUrl = actUrl;
            this.imageRes = imageRes;
            this.title = title;
        }
    }
}

package com.rex.wepeiyang.ui.lostfound.post.lost;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.support.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/10.
 */
public class PostLostTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener listener;
    private ArrayList<Integer> tagImageList = new ArrayList<Integer>(Arrays.asList(R.mipmap.icon_mobile_phone, R.mipmap.icon_bank_card, R.mipmap.icon_id_card, R.mipmap.icon_key, R.mipmap.icon_udisk, R.mipmap.icon_cup, R.mipmap.icon_books, R.mipmap.icon_others));
    private ArrayList<String> tagTextList = new ArrayList<String>(Arrays.asList("手机", "银行卡", "饭卡$身份证", "钥匙", "书包", "电脑报", "钱包", "手表&饰品", "U盘&硬盘", "水杯", "书", "其他"));

    public PostLostTagAdapter(Context context) {
        this.context = context;
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_post_lost_found_tag)
        ImageView ivTag;
        @InjectView(R.id.iv_post_lost_found_label)
        ImageView ivLabel;
        @InjectView(R.id.tv_post_lost_found_tag)
        TextView tvTag;


        View rootView;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            rootView = itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_lost_found_tag_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Drawable itemImage = ResourceHelper.getDrawable(tagImageList.get(position));
        String itemText = tagTextList.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.ivTag.setImageDrawable(itemImage);
        itemHolder.tvTag.setText(itemText);

    }

    @Override
    public int getItemCount() {
        return tagImageList.size();
    }
}
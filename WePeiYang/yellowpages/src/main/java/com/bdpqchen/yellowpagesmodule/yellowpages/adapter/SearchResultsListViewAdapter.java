package com.bdpqchen.yellowpagesmodule.yellowpages.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.R2;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragmentCallBack;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PhoneUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.TextFormatUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsListViewAdapter extends BaseAdapter {

    private Context mContext;

    private static List<SearchResult> searchResultList;
    private CollectedFragmentCallBack mFragmentCallBack;
    private OnItemClickListener mItemsOnClickListener;

    public interface OnItemClickListener {
        void onClick(SearchResult searchResult);
    }

    public SearchResultsListViewAdapter(Context context, List<SearchResult> resultList, CollectedFragmentCallBack callBack) {
        this.mContext = context;
        searchResultList = resultList;
        mFragmentCallBack = callBack;
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    public void swapData(List<SearchResult> resultList) {
        searchResultList = resultList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == searchResultList) {
            return 0;
        }
        return searchResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class NormalViewHolder {
        @BindView(R2.id.tv_item_collected_name)
        TextView tvTitle;
        @BindView(R2.id.iv_item_children_icon_phone)
        ImageView ivPhone;
        @BindView(R2.id.tv_item_collected_phone)
        TextView tvPhone;
        @BindView(R2.id.iv_item_children_icon_collected)
        ImageView ivCollected;
        @BindView(R2.id.iv_item_children_icon_uncollected)
        ImageView ivUncollected;
        @BindView(R2.id.rl_item_search_result)
        RelativeLayout rlItem;
        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NormalViewHolder holder;
        if (searchResultList.size() != 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent, false);
                holder = new NormalViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (NormalViewHolder) convertView.getTag();
            }
            final SearchResult results = searchResultList.get(position);
            if (mItemsOnClickListener != null) {
                holder.rlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemsOnClickListener.onClick(searchResultList.get(position));
                    }
                });
            }
            if (results.isCollected == 0) {
                holder.ivUncollected.setVisibility(View.VISIBLE);
                holder.ivCollected.setVisibility(View.INVISIBLE);
            } else {
                holder.ivUncollected.setVisibility(View.INVISIBLE);
                holder.ivCollected.setVisibility(View.VISIBLE);
            }
            holder.tvTitle.setText(results.name);
            holder.tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.tvPhone.setText(results.phone);
            holder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(new String[]{"复制到剪切板", "保存到通讯录", "号码/名称错了？"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Logger.i(String.valueOf(which));
                            if (which == 0) {
                                PhoneUtils.copyToClipboard(mContext, results.phone);
                            } else if (which == 1) {
                                mFragmentCallBack.saveToContact(results.name, results.phone);
                            } else if (which == 2) {
                                PhoneUtils.feedbackPhone(mContext, results.name, results.phone);
                            }
                        }
                    });
                    builder.show();
                }
            });

            holder.ivCollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.ZoomOut).duration(400).playOn(holder.ivCollected);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.ivCollected.setVisibility(View.GONE);
                            holder.ivUncollected.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    DataManager.updateCollectState(results.name, results.phone, false);
                    ToastUtils.show((Activity) mContext, "收藏已取消");
                    CollectedFragment.getCollectedData();

                }
            });
            holder.ivUncollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivCollected.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(400).playOn(holder.ivCollected);
                    holder.ivUncollected.setVisibility(View.GONE);
                    DataManager.updateCollectState(results.name, results.phone, false);
                    CollectedFragment.getCollectedData();
                }
            });

            holder.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String resultsNum = TextFormatUtils.getPhoneNum(results.phone);
                    mFragmentCallBack.callPhone(resultsNum);
                }
            });

        }

        return convertView;
    }



}

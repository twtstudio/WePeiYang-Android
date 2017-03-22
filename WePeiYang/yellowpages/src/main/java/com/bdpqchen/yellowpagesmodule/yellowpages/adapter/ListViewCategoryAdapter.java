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
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PhoneUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.TextFormatUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bdpqchen on 17-3-3.
 */

public class ListViewCategoryAdapter extends BaseAdapter {

    private Context mContext;

    private static List<Phone> phoneList;
    private CollectedFragmentCallBack mFragmentCallBack;

    public ListViewCategoryAdapter(Context context, List<Phone> phones, CollectedFragmentCallBack callBack) {
        this.mContext = context;
        phoneList = phones;
        mFragmentCallBack = callBack;
    }

    public void updateDataSet(List<Phone> phones) {
        phoneList = phones;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        if (null == phoneList) {
            return 0;
        }
        return phoneList.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneList.get(position);
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
        RelativeLayout rlItemSearchResult;

        public NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NormalViewHolder holder;
        if (phoneList.size() != 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent, false);
                holder = new NormalViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (NormalViewHolder) convertView.getTag();
            }
            final Phone phone = phoneList.get(position);
            if (phone.getIsCollected() == 0) {
                holder.ivUncollected.setVisibility(View.VISIBLE);
                holder.ivCollected.setVisibility(View.INVISIBLE);
            } else {
                holder.ivUncollected.setVisibility(View.INVISIBLE);
                holder.ivCollected.setVisibility(View.VISIBLE);
            }
            holder.tvTitle.setText(phone.getName());
            holder.tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.tvPhone.setText(phone.getPhone());
            holder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(new String[]{"复制到剪切板", "保存到通讯录", "号码/名称错了？"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Logger.i(String.valueOf(which));
                            if (which == 0) {
                                PhoneUtils.copyToClipboard(mContext, phone.getPhone());
                            } else if (which == 1) {
                                mFragmentCallBack.saveToContact(phone.getName(), phone.getPhone());
                            } else if (which == 2) {
                                PhoneUtils.feedbackPhone(mContext, phone.getName(), phone.getPhone());
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
                    DataManager.updateCollectState(phone.getName(), phone.getPhone(), false);
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
                    DataManager.updateCollectState(phone.getName(), phone.getPhone(), false);
                    CollectedFragment.getCollectedData();
                }
            });

            holder.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNum = TextFormatUtils.getPhoneNum(phone.getPhone());
                    mFragmentCallBack.callPhone(phoneNum);
                }
            });

        }

        return convertView;
    }




}

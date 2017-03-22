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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.R2;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
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
 * Created by bdpqchen on 17-3-1.
 */

public class ExpandableListViewCollectedAdapter extends BaseExpandableListAdapter {
    private static String[] mGroupArray;
    private static List<Phone> mChildList;

    private Context mContext;
    private CollectedFragmentCallBack mFragmentCallBack;

    public ExpandableListViewCollectedAdapter(Context context, CollectedFragmentCallBack callBack) {
        mContext = context;
        mFragmentCallBack = callBack;
    }

    public void addAllData(String[] groups, List<Phone> phoneList) {
        if (groups != null) {
            mGroupArray = groups;
        }
        if (phoneList != null) {
            mChildList = phoneList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (mGroupArray != null) {
            return mGroupArray.length;
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mChildList != null) {
            return mChildList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupArray[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_group, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupViewHolder.ivDropRight.setVisibility(View.GONE);
            groupViewHolder.ivDropDown.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.ivDropRight.setVisibility(View.VISIBLE);
            groupViewHolder.ivDropDown.setVisibility(View.GONE);
        }
        groupViewHolder.tvGroupName.setText(mGroupArray[groupPosition]);
        groupViewHolder.tvGroupLength.setText(mChildList.size() + "");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (mChildList.size() != 0) {
            final Phone phone = mChildList.get(childPosition);
            final ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent, false);
                childViewHolder = new ChildViewHolder(convertView);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvChildTitle.setText(phone.getName());
            childViewHolder.tvChildPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            childViewHolder.tvChildPhone.setText(phone.getPhone());
            childViewHolder.tvChildPhone.setOnClickListener(new View.OnClickListener() {
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
            childViewHolder.ivCollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.ZoomOut).duration(400).playOn(childViewHolder.ivCollected);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            childViewHolder.ivCollected.setVisibility(View.GONE);
                            childViewHolder.ivUncollected.setVisibility(View.VISIBLE);
                        }
                    }, 300);

                    DataManager.updateCollectState(phone.getName(), phone.getPhone(), false);
                    ToastUtils.show((Activity) mContext, "收藏已取消");
                }
            });
            childViewHolder.ivUncollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewHolder.ivCollected.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(400).playOn(childViewHolder.ivCollected);
                    childViewHolder.ivUncollected.setVisibility(View.GONE);
                    DataManager.updateCollectState(phone.getName(), phone.getPhone(), false);
                }
            });

            childViewHolder.ivCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNum = TextFormatUtils.getPhoneNum(phone.getPhone());
                    mFragmentCallBack.callPhone(phoneNum);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if (mChildList.size() == 0) {
            return false;
        }
        return true;
    }

    public static class ChildViewHolder {
        @BindView(R2.id.tv_item_collected_name)
        TextView tvChildTitle;
        @BindView(R2.id.iv_item_children_icon_phone)
        ImageView ivCallPhone;
        @BindView(R2.id.tv_item_collected_phone)
        TextView tvChildPhone;
        @BindView(R2.id.iv_item_children_icon_collected)
        ImageView ivCollected;
        @BindView(R2.id.iv_item_children_icon_uncollected)
        ImageView ivUncollected;
        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public static class GroupViewHolder {
        @BindView(R2.id.iv_drop_right)
        ImageView ivDropRight;
        @BindView(R2.id.iv_drop_down)
        ImageView ivDropDown;
        @BindView(R2.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R2.id.tv_group_length)
        TextView tvGroupLength;
        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

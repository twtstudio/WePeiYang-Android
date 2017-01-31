package com.twt.service.ui.gpa.evalution;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.twt.service.R;
import com.twt.service.bean.Gpa;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;

import butterknife.InjectView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.twt.service.ui.gpa.GpaActivity.presenter;

/**
 * Created by tjliqy on 2017/1/12.
 */

public class EvaluateAdapter extends BaseAdapter<Gpa.Data.Term.Course> {


    private EvaluatePresenter mPresenter;
    private int selectedPosition = -1;
    private Activity mActivity;

    EvaluateAdapter(Context context, Activity activity, EvaluatePresenter presenter) {
        super(context);
        mActivity = activity;
        mPresenter = presenter;
    }

    static class EvalutionViewHolder extends BaseViewHolder {

        @InjectView(R.id.tv_title)
        TextView mTvTitle;

        public EvalutionViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new EvalutionViewHolder(inflater.inflate(R.layout.item_gpa_evalution, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        EvalutionViewHolder viewHolder = (EvalutionViewHolder) holder;
        Gpa.Data.Term.Course course = mDataSet.get(holder.getAdapterPosition());
        viewHolder.mTvTitle.setText(course.name);
        viewHolder.itemView.setOnClickListener(view -> {
            // TODO: 2017/1/12 跳转
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View layout = inflater.inflate(R.layout.dialog_gpa_evalute, (ViewGroup) view.findViewById(R.id.dialog));
            RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.rb_star);
            selectedPosition = holder.getAdapterPosition();
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.customView(layout, false)
                    .positiveText("确定")
                    .onPositive((dialog, which) -> mPresenter.postEvaluate(course.evaluate.lesson_id,course.evaluate.union_id,course.evaluate.course_id,course.evaluate.term,(int)ratingBar.getRating()))
                    .negativeText("取消")
                    .neutralText("进入详细评价")
                    .onNeutral((dialog, which) -> EvaluateDetailActivity.onActionStart(mActivity,course,(int)ratingBar.getRating()))
                    .title("评价" + course.name)
            .show();
        });

    }

    public void deleteCourse(){
        mDataSet.remove(selectedPosition);
        notifyItemRemoved(selectedPosition);
    }
}

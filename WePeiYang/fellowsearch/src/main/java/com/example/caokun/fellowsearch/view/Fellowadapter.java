package com.example.caokun.fellowsearch.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caokun.fellowsearch.R;
import com.example.caokun.fellowsearch.common.ui.BaseAdapter;
import com.example.caokun.fellowsearch.common.ui.BaseViewHolder;
import com.example.caokun.fellowsearch.model.Student;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caokun on 2017/2/20.
 */

public class Fellowadapter extends BaseAdapter<Student>{
    public Fellowadapter(Context context){
        super(context);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new FellowViewHolder(inflater.inflate(R.layout.studentcontent, parent, false));
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position){
        FellowViewHolder fellowViewHolder=(FellowViewHolder)holder;
        Student item=mDataSet.get(position);
        fellowViewHolder.nmae.setText(item.getNmae());
        fellowViewHolder.institute.setText(item.getInstitute());
        fellowViewHolder.year.setText(item.getYear()+"级");
//        fellowViewHolder.province.setText(item.getProvince());
        fellowViewHolder.senior.setText(item.getSenior());
        fellowViewHolder.major.setText(item.getMajor());
    }
    public void refreshItems(List<Student> students){
        mDataSet.clear();
        mDataSet.addAll(students);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return mDataSet==null?0:mDataSet.size();
    }

    public  class FellowViewHolder extends BaseViewHolder{
        TextView nmae;
        TextView year;
        TextView province;
        TextView senior;
        TextView institute;
        TextView major;
        public FellowViewHolder(View view){
            super(view);
            nmae=(TextView)view.findViewById(R.id.name);
            year=(TextView)view.findViewById(R.id.year);
//            province=(TextView)view.findViewById(R.id.province);
            senior=(TextView)view.findViewById(R.id.senior);
            institute=(TextView)view.findViewById(R.id.institute);
            major=(TextView)view.findViewById(R.id.major);

        }
    }
}

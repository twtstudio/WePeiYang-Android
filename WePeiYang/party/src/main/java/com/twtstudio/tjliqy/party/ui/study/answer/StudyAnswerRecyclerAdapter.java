package com.twtstudio.tjliqy.party.ui.study.answer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by tjliqy on 2016/8/25.
 */
public class StudyAnswerRecyclerAdapter extends RecyclerView.Adapter<StudyAnswerRecyclerAdapter.MyViewHolder>{

    private Context context;

    private int[] isDone;

    private StudyAnswerBridge bridge;

    public StudyAnswerRecyclerAdapter(Context context, StudyAnswerBridge bridge) {
        this.context = context;
        this.bridge = bridge;
    }

    public void changeStatus(int[] isDone){
        this.isDone = new int[isDone.length];
//        this.isDone = isDone;
        for (int i = 0; i < isDone.length; i++) {
            this.isDone[i] = isDone[i];
        }
        notifyDataSetChanged();
    }
    public void changeItemStatus(int position,int pos){
        isDone[position] = pos;
        notifyItemChanged(position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_party_answer, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.button.setText((position+1)+"");
        if (isDone[position] != 0 ){
            holder.button.setBackgroundResource(R.drawable.shape_button_circle_green);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bridge.click(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (isDone == null){
            return 0;
        }
        return isDone.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.bt_answer)
        Button button;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

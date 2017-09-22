package com.twtstudio.tjwhm.lostfound.release;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/8.
 **/

public class ReleaseTableAdapter extends RecyclerView.Adapter {
    Context context;
    int positionSelected;
    ReleaseContract.ReleaseView releaseView;

    public ReleaseTableAdapter(Context context, int positionSelected, ReleaseContract.ReleaseView releaseView) {
        this.context = context;
        this.positionSelected = positionSelected;
        this.releaseView = releaseView;
    }

    public class ReleaseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.release_item_type)
        TextView release_item_type;
        @BindView(R2.id.release_item_cardview)
        CardView release_item_cardview;

        public ReleaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.lf_item_release_type, parent, false);

        return new ReleaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReleaseViewHolder viewHolder = (ReleaseViewHolder) holder;
        viewHolder.release_item_type.setText(Utils.getType(position + 1));
        viewHolder.release_item_cardview.setCardBackgroundColor(Color.parseColor("#dfdfdf"));
        if (position == positionSelected) {
            viewHolder.release_item_cardview.setCardBackgroundColor(Color.parseColor("#00a1e9"));
        }
        viewHolder.itemView.setOnClickListener(view -> {
            releaseView.drawRecyclerView(position);
            releaseView.onTypeItemSelected(position);
        });

    }

    @Override
    public int getItemCount() {
        return 13;
    }
}

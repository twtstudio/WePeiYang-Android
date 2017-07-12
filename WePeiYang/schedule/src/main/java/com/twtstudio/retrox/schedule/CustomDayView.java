package com.twtstudio.retrox.schedule;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.State;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.DayView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by ldf on 17/6/26.
 */

public class CustomDayView extends DayView {

    private TextView dateTv;
    private View selectedBackground;
    private View todayBackground;
    private RelativeLayout relativeLayout;
    private final CalendarDate today = new CalendarDate();
    private RxAppCompatActivity rxAppCompatActivity;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomDayView(Context context, int layoutResource,RxAppCompatActivity rxAppCompatActivity) {
        super(context, layoutResource);
        this.rxAppCompatActivity=rxAppCompatActivity;
        dateTv = (TextView) findViewById(R.id.date);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
        relativeLayout=(RelativeLayout)findViewById(R.id.relative);
        DisplayMetrics metrics = new DisplayMetrics();

       rxAppCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float width=(metrics.widthPixels-2*(metrics.widthPixels/20))/7;
        ViewGroup.LayoutParams params=relativeLayout.getLayoutParams();
        params.width= (int) Math.abs(width);
        relativeLayout.setLayoutParams(params);
    }

    @Override
    public void refreshContent() {
        renderToday(day.getDate());
        renderSelect(day.getState());
        super.refreshContent();
    }



    private void renderSelect(State state) {
        if (state == State.SELECT) {
            selectedBackground.setVisibility(VISIBLE);
            dateTv.setTextColor(Color.WHITE);
        } else if(state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#d5d5d5"));
        } else {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#111111"));
        }
    }

    private void renderToday(CalendarDate date) {
        if(date != null) {
            if(date.equals(today)) {
                dateTv.setText(Integer.toString(today.getDay()));
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }
    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context , layoutResource,rxAppCompatActivity);
    }


}

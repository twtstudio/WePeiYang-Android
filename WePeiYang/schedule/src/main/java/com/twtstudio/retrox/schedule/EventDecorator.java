package com.twtstudio.retrox.schedule;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by zhangyulong on 7/2/17.
 */

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CircleBackGroundSpan());
        view.setDaysDisabled(false);
    }


}
 class CircleBackGroundSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint paint = new Paint();
        paint.setColor(ResourceHelper.getColor(R.color.calendar_selected_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        c.drawCircle((right - left)/2, (bottom - top) / 2 + 2, dip2px(18), paint);

    }
     public static int px2dip(int pxValue)
     {
         final float scale = Resources.getSystem().getDisplayMetrics().density;
         return (int) (pxValue / scale + 0.5f);
     }


     public static float dip2px(float dipValue)
     {
         final float scale = Resources.getSystem().getDisplayMetrics().density;
         return  (dipValue * scale + 0.5f);
     }
 }

package com.twt.service.ui.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ClassTable.Data.Course> dataSet = new ArrayList<>();
    private int currentWeek;

    public ScheduleAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_course_number)
        TextView tvCourseNumber;
        @InjectView(R.id.tv_monday_course)
        TextView tvMondayCourse;
        @InjectView(R.id.tv_tuesday_course)
        TextView tvTuesdayCourse;
        @InjectView(R.id.tv_wendesday_course)
        TextView tvWendesdayCourse;
        @InjectView(R.id.tv_thursday_course)
        TextView tvThursdayCourse;
        @InjectView(R.id.tv_friday_course)
        TextView tvFridayCourse;
        @InjectView(R.id.tv_saturday_course)
        TextView tvSaturdayCourse;
        @InjectView(R.id.tv_sunday_course)
        TextView tvSundayCourse;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ItemHolder(inflater.inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        switch (position) {
            case 0:
                itemHolder.tvCourseNumber.setText("1-2");
                setRowCourse(itemHolder, 1);
                break;
            case 1:
                itemHolder.tvCourseNumber.setText("3-4");
                setRowCourse(itemHolder, 3);
                break;
            case 2:
                itemHolder.tvCourseNumber.setText("5-6");
                setRowCourse(itemHolder, 5);
                break;
            case 3:
                itemHolder.tvCourseNumber.setText("7-8");
                setRowCourse(itemHolder, 7);
                break;
            case 4:
                itemHolder.tvCourseNumber.setText("9-10");
                setRowCourse(itemHolder, 9);
                break;
            case 5:
                itemHolder.tvCourseNumber.setText("11-12");
                setRowCourse(itemHolder, 11);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void bindData(ClassTable classTable) {
        currentWeek = classTable.data.week;
        dataSet.clear();
        dataSet.addAll(classTable.data.data);
        notifyDataSetChanged();
    }

    private void setRowCourse(ItemHolder itemHolder, int courseNum) {
        for (ClassTable.Data.Course course : dataSet) {
            int startWeek = Integer.parseInt(course.week.start);
            int endWeek = Integer.parseInt(course.week.end);
            if (currentWeek >= startWeek && currentWeek <= endWeek) {
                for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
                    if (arrange.week.equals("单双周") || (arrange.week.equals("单周") && currentWeek % 2 == 1) || (arrange.week.equals("双周") && currentWeek % 2 == 0)) {
                        int start = Integer.parseInt(arrange.start);
                        int day = Integer.parseInt(arrange.day);
                        if (start == courseNum) {
                            switch (day) {
                                case 1:
                                    itemHolder.tvMondayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 2:
                                    itemHolder.tvTuesdayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 3:
                                    itemHolder.tvWendesdayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 4:
                                    itemHolder.tvThursdayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 5:
                                    itemHolder.tvFridayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 6:
                                    itemHolder.tvSaturdayCourse.setText(course.coursename + "@" + arrange.room);
                                    break;
                                case 7:
                                    itemHolder.tvSundayCourse.setText(course.coursename + "@" + arrange.room);
                            }
                        }
                    }
                }
            }
        }
    }
}

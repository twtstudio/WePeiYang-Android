package course.labs.classroomquery.homePage;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;

import course.labs.classroomquery.R;
import course.labs.classroomquery.common.RecyclerAdapter;

/**
 * Created by asus on 2017/1/25.
 */
public class ButtonClickListner  {
    private int i;
    private Button[] myButtons;
    private RecyclerAdapter mAdapter;
    private boolean[] checkState;
    private Context mContext;
    private int buttonType;//0为教学楼，1为时间，2为筛选条件
    private int size;
    private GradientDrawable drawable;
    private GradientDrawable drawable2;


    public  ButtonClickListner(Context context,int i, Button[] myButtons, boolean[] checkstate,int buttonType,int size){
        super();
        this.myButtons = myButtons;
        this.i = i;
        this.checkState = checkstate;
        this.mContext = context;
        this.buttonType = buttonType;
        this.size = size;
        drawable = new GradientDrawable();
        drawable.setCornerRadius(15);
        drawable.setStroke(3,Color.parseColor("#9932CC"));
        drawable.setColor(Color.parseColor("#00000000"));
        drawable2 = new GradientDrawable();
        drawable2.setCornerRadius(15);

        drawable2.setColor(Color.parseColor("#00000000"));
    }

    public void onClick(){
        if(buttonType==0) {
            if (checkState[i]) {
                checkState[i] = false;
                myButtons[i].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                myButtons[i].setBackgroundDrawable(drawable2);
            } else {
                if (i == 3) {
                    for (int j = 1; j < size; j++) {
                        if (j != 3) {
                            checkState[j] = false;
                            myButtons[j].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                            myButtons[j].setBackgroundDrawable(drawable2);
                        }
                    }
                    myButtons[3].setTextColor(mContext.getResources().getColor(R.color.purple));
                    myButtons[i].setBackgroundDrawable(drawable);
                    checkState[3] = true;
                } else {
                    if(!checkState[3]) {
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);
                    }
                    else{
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);
                        checkState[3] = false;
                        myButtons[3].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                        myButtons[3].setBackgroundDrawable(drawable2);
                    }
                }
            }
        }
        else if(buttonType==1){
            if (checkState[i]) {
                checkState[i] = false;
                myButtons[i].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                myButtons[i].setBackgroundDrawable(drawable2);
            } else {
                if (i == 1) {
                    for (int j = 0; j < size; j++) {
                        if (j != 1) {
                            checkState[j] = false;
                            myButtons[j].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                            myButtons[j].setBackgroundDrawable(drawable2);
                        }
                    }
                    myButtons[1].setTextColor(mContext.getResources().getColor(R.color.purple));
                    myButtons[i].setBackgroundDrawable(drawable);
                    checkState[1] = true;
                } else {
                    if(!checkState[1]) {
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);
                    }
                    else{
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);
                        checkState[1] = false;
                        myButtons[1].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                        myButtons[1].setBackgroundDrawable(drawable2);
                    }
                }
            }
        }
        else{
            if (checkState[i]) {
                checkState[i] = false;
                myButtons[i].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                myButtons[i].setBackgroundDrawable(drawable2);
            } else {

                if (i == 0) {
                    for (int j = 1; j < size; j++) {

                            checkState[j] = false;
                            myButtons[j].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                            myButtons[j].setBackgroundDrawable(drawable2);

                    }
                    myButtons[0].setTextColor(mContext.getResources().getColor(R.color.purple));
                    myButtons[0].setBackgroundDrawable(drawable);
                    checkState[0] = true;
                } else {
                    if(!checkState[0]) {
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);

                    }else{
                        checkState[i] = true;
                        myButtons[i].setTextColor(mContext.getResources().getColor(R.color.purple));
                        myButtons[i].setBackgroundDrawable(drawable);
                        checkState[0] = false;
                        myButtons[0].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                        myButtons[0].setBackgroundDrawable(drawable2);
                    }
                    if(i==1){
                        checkState[2] = false;
                        myButtons[2].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                        myButtons[2].setBackgroundDrawable(drawable2);
                    }
                    if(i==2){
                        checkState[1] = false;
                        myButtons[1].setTextColor(mContext.getResources().getColor(R.color.darkgray));
                        myButtons[1].setBackgroundDrawable(drawable2);
                    }

                }

            }
        }
    }
}

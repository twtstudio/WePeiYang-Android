package com.twt.service.party.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.support.ResourceHelper;

/**
 * Created by tjliqy on 2016/9/5.
 */
public class StatusText extends TextView {

    public static final int MODE_UNABLE = 0;
    public static final int MODE_DOING = 1;
    public static final int MODE_DONE = 2;

    private Paint mPaint;
    private Paint mSolidPaint;
    private TextPaint mTextPaint;
    private int mMode = MODE_UNABLE;
    private float mSlantedLength = 40;

    public StatusText(Context context) {
        super(context);
    }

    public StatusText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatusText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs){
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StatusText, 0, 0);

        if (array.hasValue(R.styleable.StatusText_status)) {
            mMode = array.getInt(R.styleable.StatusText_status, 0);
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mPaint.setColor(ResourceHelper.getColor(R.color.myButtonColorGreen));

        mSolidPaint = new Paint();
        mSolidPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16);
        mTextPaint.setColor(ResourceHelper.getColor(R.color.white));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSolid(canvas);

        if (mMode == MODE_DONE){
            drawBackground(canvas);
            drawText(canvas);
        }
    }


    private void drawSolid(Canvas canvas){
        int h = getHeight();
        int w = getWidth();
        switch (mMode){
            case MODE_UNABLE:
                mSolidPaint.setColor(ResourceHelper.getColor(R.color.myTextPrimaryColorGray));
                break;
            case MODE_DOING:
                mSolidPaint.setColor(ResourceHelper.getColor(R.color.myTextPrimaryColorRed));
                break;
            case MODE_DONE:
                mSolidPaint.setColor(ResourceHelper.getColor(R.color.myTextPrimaryColorGreen));
                break;
        }
        canvas.drawLine(0,0,w,0,mSolidPaint);
        canvas.drawLine(0,0,0,h,mSolidPaint);
        canvas.drawLine(0,h,w,h,mSolidPaint);
        canvas.drawLine(0,w,w,h,mSolidPaint);
    }
    private void drawBackground(Canvas canvas){
        Path path = new Path();
        int h = getHeight();
        int w = h;

        path.lineTo(0,h);
        path.lineTo(w,h);
        path.close();

        canvas.drawPath(path,mPaint);
        canvas.save();
    }

    private void drawText(Canvas canvas){
        int w = (int) (canvas.getWidth() - mSlantedLength / 2);
        int h = (int) (canvas.getHeight() - mSlantedLength / 2);
        float[] xy = calculateXY(canvas,w, h);
        float toX = xy[0];
        float toY = xy[1];
        float centerX = xy[2];
        float centerY = xy[3];
        float angle = xy[4];

        canvas.rotate(angle, centerX , centerY );

        canvas.drawText("已完成", toX, toY, mTextPaint);
    }

    private float[] calculateXY(Canvas canvas,int w, int h) {
        float[] xy = new float[5];
        Rect rect = null;
        RectF rectF = null;
        int offset = (int) (mSlantedLength / 2);

        rect = new Rect(0, 0, w, h);
        rectF = new RectF(rect);
        rectF.right = mTextPaint.measureText("已完成", 0, "已完成".length());
        rectF.bottom = mTextPaint.descent() - mTextPaint.ascent();
        rectF.left += (rect.width() - rectF.right) / 2.0f;
        rectF.top += (rect.height() - rectF.bottom) / 2.0f;
        xy[0] = rectF.left;
        xy[1] = rectF.top - mTextPaint.ascent();
        xy[2] = w / 2;
        xy[3] = h / 2;
        xy[4] = -45;

        return xy;
    }
}

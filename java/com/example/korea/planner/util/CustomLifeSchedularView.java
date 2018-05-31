package com.example.korea.planner.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.korea.planner.R;
import com.example.korea.planner.data.LifeSchedularDataList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korea on 2017-03-28.
 *
 */

public class CustomLifeSchedularView extends View {
    private Paint pnt;
    private Paint basic;
    private Paint pntText;
    private Path pathText;
    private RectF rect, rectText;
    private int width;
    private float centerX, centerY, radius;
    private List<LifeSchedularDataList> list;
    private String text = null;
    private int where;

    public CustomLifeSchedularView(Context context) {
        super(context);

        basic = new Paint();
        pnt = new Paint();
        pntText = new Paint();

        rect = new RectF();
        rectText = new RectF();
        list = new ArrayList<>();
        pathText = new Path();
    }

    public CustomLifeSchedularView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLifeSchedularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.text = attrs.getAttributeValue(null, "text");
        basic = new Paint();
        pnt = new Paint();
        pntText = new Paint();

        rect = new RectF();
        rectText = new RectF();
        list = new ArrayList<>();
        pathText = new Path();
    }
    public void setTing(List<LifeSchedularDataList> list, int where) {
        this.list = list;
        this.where = where;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = 0;
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED: //mode가 셋팅되지 않은 크기가 넘어올때
                heightSize = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST: //wrap_content
                heightSize = 20;
                break;
            case MeasureSpec.EXACTLY://fill_parent, match_parent
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                heightSize = heightMeasureSpec;
                break;
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                widthSize = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                widthSize = 100;
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                widthSize = widthMeasureSpec;
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //셋팅 화면
        if (where == 1) {
            pntText.setTextSize(40);
            pntText.setTextAlign(Paint.Align.CENTER);
            pntText.setColor(Color.BLACK);
            basic.setColor(Color.BLACK);
            basic.setStyle(Paint.Style.STROKE);
            basic.setStrokeWidth(3);
            basic.setAntiAlias(true);
            //pathText.moveTo(getMeasuredWidth()/2,getMeasuredHeight()/2);
            rect.set(50, 50, getMeasuredWidth() - 50, getMeasuredWidth() - 50);
            rectText.set(100, 100, getMeasuredWidth() - 100, getMeasuredWidth() - 100);
            canvas.drawColor(getResources().getColor(R.color.materialPrimary_pupple50));
            centerX = getMeasuredWidth() / 2;
            centerY = getMeasuredHeight() / 2;
            radius = getMeasuredWidth() / 2 - 30;
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 10, basic);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 50, basic);
            if (list.size() > 0) {
                for (LifeSchedularDataList item : list) {
                    float start = ((item.getBeforeHour() * 60) + item.getBeforeMin()) - 360;
                    float last = ((item.getAfterHour() * 60) + item.getAfterMin()) - 360;
                    //background  그리는중
                    pnt.setColor(getResources().getColor(item.getColor()));
                    pnt.setStyle(Paint.Style.FILL);
                    if (item.getBeforeHour() <= item.getAfterHour()) {
                        canvas.drawArc(rect, start / 1440 * 360, (last - start) / 1440 * 360, true, pnt);
                    } else {
                        canvas.drawArc(rect, start / 1440 * 360, (1440 - start + last) / 1440 * 360, true, pnt);
                    }
                }
                //text 그리기
                for (int i = 0; i < list.size(); i++) {
                    float start = ((list.get(i).getBeforeHour() * 60) + list.get(i).getBeforeMin()) - 360;
                    float last = ((list.get(i).getAfterHour() * 60) + list.get(i).getAfterMin()) - 360;
                    int length = list.get(i).getTitle().length();
                    String content = null;
                    if (list.get(i).getBeforeHour() <= list.get(i).getAfterHour()) {
                        pathText.addArc(rectText, start / 1440 * 360, (last - start) / 1440 * 360);
                        if (length <= Math.abs((list.get(i).getAfterHour() - list.get(i).getBeforeHour()) * 2)) {
                            content = list.get(i).getTitle();
                        } else {
                            content = list.get(i).getTitle().
                                    substring(0, Math.abs((list.get(i).getAfterHour() - list.get(i).getBeforeHour()) * 2));
                        }
                        canvas.drawTextOnPath(content, pathText, 0, 30, pntText);
                    } else {
                        pathText.addArc(rectText, start / 1440 * 360, (1440 - start + last) / 1440 * 360);
                        if (length <= Math.abs((list.get(i).getAfterHour() - (list.get(i).getBeforeHour() - 24)) * 2)) {
                            content = list.get(i).getTitle();
                        } else {
                            content = list.get(i).getTitle().
                                    substring(0, Math.abs((list.get(i).getAfterHour() - (list.get(i).getBeforeHour() - 24)) * 2));
                        }
                        canvas.drawTextOnPath(content, pathText, 0, 30, pntText);
                    }
                    pathText.reset();
                }

            }
            rectText.set(30, 30, getMeasuredWidth() - 30, getMeasuredWidth() - 30);
            if (getMeasuredWidth() > 100) {
                for (int i = 0; i < 24; i++) {
                    pathText.addArc(rectText, -95 + 15 * i, 10);
                    canvas.drawTextOnPath("" + i, pathText, 0, 10, pntText);
                    pathText.reset();
                }
            }
        } else if (where == 0) {//main item에서는
            basic.setColor(Color.BLACK);
            basic.setStyle(Paint.Style.STROKE);
            basic.setStrokeWidth(3);
            basic.setAntiAlias(true);
            //pathText.moveTo(getMeasuredWidth()/2,getMeasuredHeight()/2);
            rect.set(30, 30, getMeasuredWidth() - 30, getMeasuredWidth() - 30);
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 10, basic);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 30, basic);
            for (int i = 0; i < list.size(); i++) {
                float start = ((list.get(i).getBeforeHour() * 60) + list.get(i).getBeforeMin()) - 360;
                float last = ((list.get(i).getAfterHour() * 60) + list.get(i).getAfterMin()) - 360;
                pnt.setColor(getResources().getColor(list.get(i).getColor()));
                pnt.setStyle(Paint.Style.FILL);
                if (list.get(i).getBeforeHour() <= list.get(i).getAfterHour()) {
                    canvas.drawArc(rect, start / 1440 * 360, (last - start) / 1440 * 360, true, pnt);
                } else {
                    canvas.drawArc(rect, start / 1440 * 360, (1440 - start + last) / 1440 * 360, true, pnt);
                }
            }
        } else if (where == 2) {
            pntText.setTextSize(20);
            pntText.setTextAlign(Paint.Align.CENTER);
            pntText.setColor(Color.BLACK);
            basic.setColor(Color.BLACK);
            basic.setStyle(Paint.Style.STROKE);
            basic.setStrokeWidth(3);
            basic.setAntiAlias(true);
            //pathText.moveTo(getMeasuredWidth()/2,getMeasuredHeight()/2);
            rect.set(50, 50, getMeasuredWidth() - 50, getMeasuredWidth() - 50);
            rectText.set(60, 60, getMeasuredWidth() - 60, getMeasuredWidth() - 60);
            //canvas.drawColor(getResources().getColor(R.color.materialPrimary_pupple50));
            centerX = getMeasuredWidth() / 2;
            centerY = getMeasuredWidth() / 2;
            radius = getMeasuredWidth() / 2 - 30;
            pnt.setColor(Color.WHITE);
            pnt.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 10, pnt);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 10, basic);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - 50, basic);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    float start = ((list.get(i).getBeforeHour() * 60) + list.get(i).getBeforeMin()) - 360;
                    float last = ((list.get(i).getAfterHour() * 60) + list.get(i).getAfterMin()) - 360;
                    double startAngle = ((start) / 1440 * 360);
                    double latAngle = ((last) / 1440 * 360);
                    float x = (float) (Math.cos(startAngle) * radius);
                    float y = (float) (Math.sin(startAngle) * radius);
                    //background  그리는중
                    pnt.setColor(getResources().getColor(list.get(i).getColor()));
                    pnt.setStyle(Paint.Style.FILL);
                    if (list.get(i).getBeforeHour() <= list.get(i).getAfterHour()) {
                        canvas.drawArc(rect, start / 1440 * 360, (last - start) / 1440 * 360, true, pnt);
                    } else {
                        canvas.drawArc(rect, start / 1440 * 360, (1440 - start + last) / 1440 * 360, true, pnt);
                    }
                }
                //text 그리기
                for (int i = 0; i < list.size(); i++) {
                    float start = ((list.get(i).getBeforeHour() * 60) + list.get(i).getBeforeMin()) - 360;
                    float last = ((list.get(i).getAfterHour() * 60) + list.get(i).getAfterMin()) - 360;
                    int length = list.get(i).getTitle().length();
                    String content = null;
                    if (list.get(i).getBeforeHour() <= list.get(i).getAfterHour()) {
                        pathText.addArc(rectText, start / 1440 * 360, (last - start) / 1440 * 360);
                        if (length <= Math.abs((list.get(i).getAfterHour() - list.get(i).getBeforeHour()) * 2)) {
                            content = list.get(i).getTitle();
                        } else {
                            content = list.get(i).getTitle().
                                    substring(0, Math.abs((list.get(i).getAfterHour() - list.get(i).getBeforeHour()) * 2));
                        }
                        canvas.drawTextOnPath(content, pathText, 0, 30, pntText);
                    } else {
                        pathText.addArc(rectText, start / 1440 * 360, (1440 - start + last) / 1440 * 360);
                        if (length <= Math.abs((list.get(i).getAfterHour() - (list.get(i).getBeforeHour() - 24)) * 2)) {
                            content = list.get(i).getTitle();
                        } else {
                            content = list.get(i).getTitle().
                                    substring(0, Math.abs((list.get(i).getAfterHour() - (list.get(i).getBeforeHour() - 24)) * 2));
                        }
                        canvas.drawTextOnPath(content, pathText, 0, 30, pntText);
                    }
                    pathText.reset();
                }

            }
            rectText.set(30, 30, getMeasuredWidth() - 30, getMeasuredWidth() - 30);
            if (getMeasuredWidth() > 100) {
                for (int i = 0; i < 24; i++) {
                    pathText.addArc(rectText, -95 + 15 * i, 10);
                    canvas.drawTextOnPath("" + i, pathText, 0, 10, pntText);
                    pathText.reset();
                }
            }
        }
    }
}

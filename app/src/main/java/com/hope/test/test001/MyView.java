package com.hope.test.test001;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by han on 2016/3/22.
 */
public class MyView extends View implements View.OnTouchListener
{
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public MyView(Context context) {
        super(context);
        this.viewInit();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.viewInit();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.viewInit();
    }

    private void viewInit(){
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(this.x1!=-1 && this.x2!=-1){
            Paint p = new Paint();
            p.setColor(Color.BLACK);

            canvas.drawRect(Math.min(x1,x2),Math.min(y1,y2),Math.max(x1, x2),Math.max(y1, y2),p);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("MotionEvent.ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("MotionEvent.ACTION_UP");
                this.clear();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("MotionEvent.ACTION_MOVE");
                this.setValue(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                System.out.println("MotionEvent.ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                System.out.println("MotionEvent.ACTION_POINTER_UP");
                break;
        }

        System.out.println("count:"+event.getPointerCount());
        this.invalidate();
        return true;
    }

    private void setValue(MotionEvent event)
    {
        int count = event.getPointerCount();
        if(count>1){
            this.x1 = event.getX(0);
            this.y1 = event.getY(0);
            this.x2 = event.getX(1);
            this.y2 = event.getY(1);
        }
    }

    private void clear(){
        this.x1 = -1;
        this.y1 = -1;
        this.x2 = -1;
        this.y2 = -1;
    }
}

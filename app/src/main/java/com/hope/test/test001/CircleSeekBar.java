package com.hope.test.test001;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by han on 2016/3/18.
 */
public class CircleSeekBar extends View implements View.OnTouchListener
{
    private int xCenter = 0;
    private int yCenter = 0;

    private int radius = 0;

    private float mx = -1;
    private float my = -1;

    private float sx = -1;
    private float sy = -1;

    private boolean floating = false;

    private int angle = 0;

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //System.out.println("MyView(Context context, AttributeSet attrs, int defStyleAttr)");
        this.viewInit();
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //System.out.println("MyView(Context context, AttributeSet attrs)");
        this.viewInit();
    }

    public CircleSeekBar(Context context) {
        super(context);
        //System.out.println("MyView(Context context)");
        this.viewInit();
    }

    private void viewInit(){
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(this.floating)
        {
            this.mx = event.getX();
            this.my = event.getY();
        }

        this.invalidate();

        return true;
    }

    private void drawAngle(Canvas canvas,double angle)
    {
        int width = this.getWidth();
        int height = this.getHeight();

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(Math.min(width, height) / 2);

        Paint.FontMetricsInt pm = p.getFontMetricsInt();
        int baseline = (this.getMeasuredHeight() - pm.bottom + pm.top)/2 - pm.top;
        canvas.drawText((int)angle + "", width / 2, baseline, p);
    }

    private void drawCircleButton(Canvas canvas,double angle)
    {
        this.sx = (float)(this.xCenter + Math.sin(angle / 180 * Math.PI)*(this.radius-40));
        this.sy = (float) (this.yCenter - Math.cos(angle / 180 * Math.PI)*(this.radius-40));

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);

        canvas.drawCircle(sx, sy, 40, p);

        if(!this.floating)
        {
            p.setTextSize(40);
            p.setColor(Color.GRAY);
            p.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt pm = p.getFontMetricsInt();
            int baseline = (int)((40 - pm.bottom + pm.top)/2 - pm.top + this.sy - 20);
            canvas.drawText((int)angle + "", this.sx, baseline, p);
        }
    }

    private void drawSector(Canvas canvas,double angle)
    {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.GRAY);

        RectF rect =  new RectF(
                this.xCenter - this.radius,
                this.yCenter - this.radius,
                this.xCenter + this.radius,
                this.yCenter + this.radius);

        canvas.drawArc(rect, 270, (float) angle, true, p);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.radius = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight())/2;
        this.xCenter = this.getMeasuredWidth()/2;
        this.yCenter = this.getMeasuredHeight()/2;

        if(this.mx == -1 && this.my == -1)
        {
            this.my = 0;
            this.mx = this.getMeasuredWidth()/2;
        }

        double mh = this.my - this.yCenter;
        double mw = this.mx - this.xCenter;

        double angle = (Math.atan(mh/mw)/Math.PI*180);

        this.angle = (int)angle;

        if(mw >=0 )
            angle += 90;
        else
            angle += 270;

        this.drawSector(canvas,angle);
        this.drawCircleButton(canvas,angle);

        if(this.floating)
            this.drawAngle(canvas,angle);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            System.out.println("sx:"+this.sx+" sy:"+this.sy);
            RectF rect = new RectF(this.sx-40,this.sy-40,this.sx+40,this.sy+40);
            System.out.println(rect);
            System.out.println("event "+event.getX()+" "+event.getY());
            if(rect.contains(event.getX(), event.getY())){
                this.floating = true;
            }

            System.out.println("down");
        } else if(action == MotionEvent.ACTION_UP){
            System.out.println("up");
            this.floating = false;
        }

        return false;
    }

    public int getAngle()
    {
        return this.angle;
    }
}

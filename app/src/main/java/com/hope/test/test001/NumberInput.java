package com.hope.test.test001;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by han on 2016/3/23.
 */
public class NumberInput extends HorizontalScrollView implements InputViewOnClickListener
{
    private static final int[] NUM_BUTTON_IDS = {
            R.id.uid_num_0,R.id.uid_num_1,R.id.uid_num_2,
            R.id.uid_num_3,R.id.uid_num_4,R.id.uid_num_5,
            R.id.uid_num_6,R.id.uid_num_7,R.id.uid_num_8,
            R.id.uid_num_9};

    private static final LinearLayout.LayoutParams INPUT_VIEW_PARAMS = new LinearLayout.LayoutParams(100,120);

    private LinearLayout layout;
    private List<InputView> views;

    private InputView selectedInputViewItem;

    private int leftMaxCount = 0;

    private int rightCount = 0;

    private TextView radixPointView;

    public NumberInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.viewInit();
    }

    public NumberInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.viewInit();
    }

    public NumberInput(Context context) {
        super(context);
        this.viewInit();
    }

    private void viewInit()
    {
        this.dialogInit();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        this.layout = new LinearLayout(this.getContext());
        this.layout.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(this.layout, params);

        this.views = new ArrayList<InputView>();

        InputView view  = new InputView(this.getContext());
        view.setListener(this);
        this.views.add(view);
        this.layout.addView(view, INPUT_VIEW_PARAMS);

        this.radixPointView = new TextView(this.getContext());
        this.radixPointView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        this.radixPointView.setTextSize(40);
        this.radixPointView.setText(".");

    }

    private AlertDialog dialog;

    private void dialogInit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("AlertDialog");

        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.number_input_dialog,null,false);

        for(int id:NUM_BUTTON_IDS)
        {
            view.findViewById(id).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(v instanceof Button){
                        NumberInput self = NumberInput.this;
                        Button button = (Button)v;
                        self.selectedInputViewItem.setNum(Integer.parseInt(button.getText().toString()));

                        self.addInputView(self.selectedInputViewItem);
                        self.dialog.cancel();
                    }
                }
            });
        }

        view.findViewById(R.id.uid_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberInput.this.dialog.cancel();
            }
        });

        builder.setView(view);
        this.dialog = builder.create();
    }

    private void addInputView(InputView selectedItem)
    {
        if((this.leftMaxCount==0 || this.leftMaxCount>this.views.size()-this.rightCount)
                && this.views.indexOf(selectedItem)==0)
        {
            InputView v  = new InputView(this.getContext());
            v.setListener(this);
            this.views.add(0,v);
            this.layout.addView(v,0,INPUT_VIEW_PARAMS);
        }
    }

    @Override
    public void onClickInputView(InputView view) {
        this.selectedInputViewItem = view;
        this.dialog.show();
    }

    @Override
    public void deleteInputView(InputView view) {
        //view.startAnimation();
        TranslateAnimation animation = new TranslateAnimation(0,0,0,this.getMeasuredHeight());
        animation.setDuration(700);
        view.startAnimation(animation);

        this.views.remove(view);
        this.layout.removeView(view);

        if(this.views.get(0).isInput())
        {
            InputView v  = new InputView(this.getContext());
            v.setListener(this);
            this.views.add(0,v);
            this.layout.addView(v,0,INPUT_VIEW_PARAMS);
        }
    }

    public class InputView extends View implements OnTouchListener
    {
        private float x;
        private float y;

        private int num = 0;
        private boolean input = false;
        private boolean delete = true;

        private InputViewOnClickListener listener;

        public InputView(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }

        public InputView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setOnTouchListener(this);
        }

        public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.setOnTouchListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();

            p.setAntiAlias(true);
            p.setStrokeWidth(2);
            if(!input)
                p.setStyle(Paint.Style.STROKE);

            p.setColor(Color.BLACK);


            canvas.drawRoundRect(new RectF(5,5,this.getMeasuredWidth()-5,getMeasuredHeight()-5),10,10,p);

            Paint p1 = new Paint();
            p1.setTextAlign(Paint.Align.CENTER);
            p1.setTextSize(this.getMeasuredHeight() - 10);

            if(input)
                p1.setColor(Color.WHITE);
            else
                p1.setColor(Color.GRAY);

            Paint.FontMetricsInt pm = p1.getFontMetricsInt();
            int baseline = (this.getMeasuredHeight() - pm.bottom + pm.top)/2 - pm.top;
            canvas.drawText(num + "", this.getMeasuredWidth() / 2, baseline, p1);

        }

        public void setListener(InputViewOnClickListener listener){
            this.listener = listener;
        }

        public void setNum(int num){
            this.input = true;
            this.num = num;
            this.invalidate();
        }

        public void setDelete(boolean delete){
            this.delete = delete;
        }

        public boolean isInput(){
            return this.input;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    this.x = x;
                    this.y = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if(this.x==x && this.y==y)
                        this.listener.onClickInputView(this);

                    if(this.input && Math.abs(y-this.y)>this.getMeasuredHeight()/2 && this.delete)
                        this.listener.deleteInputView(this);
                    break;
            }

            return true;
        }
    }

    public void setLeftMaxCount(int count)
    {
        this.leftMaxCount = (count>0? count:0);
    }

    public void setRightCount(int count)
    {
        int ncount = (count>0? count:0);

        if(ncount == this.rightCount)
            return;

        if(this.rightCount>ncount){
            for(int i=0;i<(this.rightCount-ncount);i++){
                View view = this.views.get(this.views.size()-1);
                this.layout.removeView(view);
                this.views.remove(view);
            }

            if(ncount == 0){
                this.layout.removeView(this.radixPointView);
            }

        } else {
            if(this.rightCount == 0){
                this.layout.addView(this.radixPointView,INPUT_VIEW_PARAMS);
            }

            for(int i=0;i<(ncount-this.rightCount);i++){
                InputView view  = new InputView(this.getContext());
                view.setListener(this);
                view.setDelete(false);
                this.views.add(view);
                this.layout.addView(view, INPUT_VIEW_PARAMS);
            }
        }

        this.rightCount = ncount;
    }
}

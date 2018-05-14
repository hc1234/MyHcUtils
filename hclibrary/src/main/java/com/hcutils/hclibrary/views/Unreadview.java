package com.hcutils.hclibrary.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcutils.hclibrary.R;

public class Unreadview extends RelativeLayout{
    Boolean ismove=false;
    View view;
    TextView unread;
    unreadcallbck unreadcallbck;
    public interface unreadcallbck{
        void unreadmove(TextView v);
    }
    public Unreadview(Context context) {
        this(context,null);
    }

    public Unreadview(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public Unreadview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(context).inflate(R.layout.unread_layout,null);
        unread= (TextView) view.findViewById(R.id.unread_number);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        view.setLayoutParams(lp);
        addView(view,getChildCount());
    }
    public Unreadview setUnreadVisibility(int visibi){
        if(view!=null){
            view.setVisibility(visibi);
        }
        return this;
    }
    public Unreadview setUnreadcount(String number){
        if(view!=null){
            unread.setText(number);
        }
        return this;
    }
    public Unreadview getUnreadCallback(unreadcallbck unreadcallbck){
        this.unreadcallbck=unreadcallbck;
        return this;

    }
    public Unreadview setUnreadTextColor(int color){
        if(view!=null){
            unread.setTextColor(getResources().getColor(color));
        }
        return this;
    }
    public Unreadview setUnreadBackground(int drawble){
        if(view!=null){
            unread.setBackgroundResource(drawble);
        }
        return this;
    }

    public Unreadview setUnreadmove(Boolean ismove){
        this.ismove=ismove;
        if(ismove){
            unread.setOnTouchListener(onTouchListener);
        }else {
            unread.setOnTouchListener(null);
        }
        return this;
    }


    int lastX,lastY;
    float vx,vy;
    int x, y;
    OnTouchListener onTouchListener=new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

             x= (int) event.getX();
             y= (int) event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    vx=v.getX();
                    vy=v.getY();
                    lastX=x;
                    lastY=y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int offsetX = x - lastX;
                    int offsetY = y - lastY;
                    ((View)v.getParent()).scrollBy(-offsetX, -offsetY);

                    break;

                case MotionEvent.ACTION_UP:
                        ((View)v.getParent()).scrollTo((int) vx, (int) vy);
                        if(unreadcallbck!=null){
                            unreadcallbck.unreadmove(unread);
                        }

                    break;

            }

            return true;
        }
    };
}

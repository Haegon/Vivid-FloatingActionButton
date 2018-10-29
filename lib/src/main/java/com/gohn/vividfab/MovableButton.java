package com.gohn.vividfab;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class MovableButton extends FloatingActionButton implements View.OnTouchListener {

    private final static int ANCHOR_RT = 1 << 0;
    private final static int ANCHOR_LT = 1 << 1;
    private final static int ANCHOR_RB = 1 << 2;
    private final static int ANCHOR_LB = 1 << 3;

    public interface StatusListener {
        void onOpened(float x, float y, boolean isRight);

        void onClosed();
    }

    private final static float CLICK_DRAG_TOLERANCE = 15;

    private int anchors = ANCHOR_RT | ANCHOR_LT | ANCHOR_RB | ANCHOR_LB;
    private float downRawX, downRawY;
    private float dX, dY;
    private Context context;
    private boolean isOpened = false;
    private float oldX;
    private float oldY;
    private StatusListener listener;

    public MovableButton(Context context) {
        super(context);
        init(context);
    }

    public MovableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MovableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOnTouchListener(this);
    }

    public void setStatusListener(StatusListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View viewParent;
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("MovableFAB", "ACTION_DOWN");
                downRawX = motionEvent.getRawX();
                downRawY = motionEvent.getRawY();
                dX = view.getX() - downRawX;
                dY = view.getY() - downRawY;

                return true;

            case MotionEvent.ACTION_MOVE:
                if (isOpened) {
                    return true;
                }

                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();

                viewParent = (View) view.getParent();
                int parentWidth = viewParent.getWidth();
                int parentHeight = viewParent.getHeight();

                float newX = motionEvent.getRawX() + dX;
                newX = Math.max(0, newX);
                newX = Math.min(parentWidth - viewWidth, newX);

                float newY = motionEvent.getRawY() + dY;
                newY = Math.max(0, newY);
                newY = Math.min(parentHeight - viewHeight, newY);

                view.setX(newX);
                view.setY(newY);

                return true;

            case MotionEvent.ACTION_UP:
                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - downRawX;
                float upDY = upRawY - downRawY;


                if (getLength(upDX, upDY) > CLICK_DRAG_TOLERANCE) {
                    return true;
                }

                viewParent = (View) view.getParent();

                double maxLength = getLength(view.getBottom(), view.getRight());

                if (!isOpened) {
                    open(view, viewParent);
                } else {
                    if (listener != null) {
                        listener.onClosed();
                    }
                }

                isOpened = !isOpened;

                return false;

            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    public void open(View view, View viewParent) {
        oldX = view.getX();
        oldY = view.getY();

        float finalX;
        final float finalY = view.getBottom() - view.getHeight();

        float fab_margin = dp2px(context, 16);
        boolean isRight;

        if (view.getX() > viewParent.getWidth() / 2 - view.getWidth() / 2) {
            finalX = viewParent.getRight() - view.getWidth();
            finalX = Math.max(0, finalX) - fab_margin;
            isRight = true;
        } else {  //view near Left
            finalX = viewParent.getLeft();
            finalX = Math.min(viewParent.getWidth() - view.getWidth(), finalX) + fab_margin;
            isRight = false;
        }

        double maxLength = getLength(view.getBottom(), view.getRight());
        double length = getLength(finalX - oldX, finalY - oldY);

        final float x = finalX;
        final float y = finalY;
        final boolean isR = isRight;

        view.animate()
                .x(finalX)
                .y(finalY)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (listener != null) {
                            listener.onOpened(x, y, isR);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    public void close() {
        float currentX = this.getX();
        float currentY = this.getY();

        double maxLength = getLength(this.getBottom(), this.getRight());
        double length = getLength(currentX - oldX, currentY - oldY);

        this.animate()
                .x(oldX)
                .y(oldY)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isOpened = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }

    public double getLength(float x, float y) {
        return Math.sqrt(Math.pow(x, 2f) + Math.pow(y, 2f));
    }

    public int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
package com.gohn.vividfab;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ArcButton extends FloatingActionButton {

    public interface ArcStatusListener {
        void onOpenFinished();

        void onCloseFinished();
    }

    private Context context;
    private boolean isOpened = false;
    private float oldX;
    private float oldY;

    private ArcStatusListener listener;

    public ArcButton(Context context) {
        super(context);
        init(context);
    }

    public ArcButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setVisibility(INVISIBLE);
    }

    public void setArcStatusListener(ArcStatusListener listener) {
        this.listener = listener;
    }

    public void open(float distance, int totalCount, int index, float x, float y, boolean isRight) {
        if (isOpened) return;
        isOpened = true;

        setVisibility(VISIBLE);

        if (distance <= 0) distance = dp2px(context, 120);

        setX(x);
        setY(y);

        float angle = index == 0 ? 0f : 90f / (float) (totalCount - 1) * index;

        if (totalCount == 1 && index == 0) {
            angle = 45f;
        } else if (totalCount == 2) {
            if (index == 0) {
                angle = 15f;
            } else if (index == 1) {
                angle = 75f;
            }
        }

        oldX = getX();
        oldY = getY();

        float finalX = isRight ?
                getX() - distance * (float) Math.cos(Math.toRadians((double) angle)) :
                getX() + distance * (float) Math.cos(Math.toRadians((double) angle));
        float finalY = getY() - distance * (float) Math.sin(Math.toRadians((double) angle));

        double length = getLength(finalX - oldX, finalY - oldY);

        animate()
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
                            listener.onOpenFinished();
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
        if (!isOpened) return;

        animate()
                .x(oldX)
                .y(oldY)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setVisibility(INVISIBLE);
                        isOpened = false;
                        if (listener != null) {
                            listener.onCloseFinished();
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

    public boolean isOpened() {
        return isOpened;
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
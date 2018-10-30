package com.gohn.vividfab;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

class ArcButton extends FloatingActionButton {

    public interface ArcStatusListener {
        void onOpenFinished();

        void onCloseFinished();
    }

    private Context context;
    private boolean isOpened = false;
    private float oldX;
    private float oldY;
    private VividFab vividFab;

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

    public void setVividFab(VividFab vividFab) {
        this.vividFab = vividFab;
    }

    public void setArcStatusListener(ArcStatusListener listener) {
        this.listener = listener;
    }

    public void open(float distance, int totalCount, int index, float x, float y, CornerPosition cornerPosition) {
        if (isOpened) return;
        isOpened = true;

        setVisibility(VISIBLE);

        if (distance <= 0) distance = Utils.dp2px(context, 120);

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

        float finalX = 0;
        float finalY = 0;

        switch (cornerPosition) {
            case RIGHT_BOTTOM:
                finalX = getX() - distance * (float) Math.cos(Math.toRadians((double) angle));
                finalY = getY() - distance * (float) Math.sin(Math.toRadians((double) angle));
                break;
            case LEFT_BOTTOM:
                finalX = getX() + distance * (float) Math.cos(Math.toRadians((double) angle));
                finalY = getY() - distance * (float) Math.sin(Math.toRadians((double) angle));
                break;
            case RIGHT_TOP:
                finalX = getX() - distance * (float) Math.cos(Math.toRadians((double) angle));
                finalY = getY() + distance * (float) Math.sin(Math.toRadians((double) angle));
                break;
            case LEFT_TOP:
                finalX = getX() + distance * (float) Math.cos(Math.toRadians((double) angle));
                finalY = getY() + distance * (float) Math.sin(Math.toRadians((double) angle));
                break;
        }


        double length = Utils.getLength(finalX - oldX, finalY - oldY);

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
}
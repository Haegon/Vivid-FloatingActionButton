package com.gohn.vividfab;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

class MovableButton extends FloatingActionButton implements View.OnTouchListener {
    public interface StatusListener {
        void onOpened(float x, float y, CornerPosition cornerPosition);

        void onClosed();
    }

    private final static float CLICK_DRAG_TOLERANCE = 15;
    private final static float CLICK_DRAG_ALLOWANCE = 50;

    private WallPosition wallPosition = WallPosition.ALL;
    private float downRawX, downRawY;
    private float dX, dY;
    private Context context;
    private boolean isOpened = false;
    private float oldX;
    private float oldY;
    private StatusListener listener;
    private boolean isForceCancelled = false;

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

                double r = Utils.getLength(Math.abs(view.getX() - newX), Math.abs(view.getY() - newY));
                if (isOpened && r < Utils.dp2px(context, CLICK_DRAG_ALLOWANCE)) {
                    return true;
                } else if (isOpened && r > Utils.dp2px(context, CLICK_DRAG_ALLOWANCE)) {
                    if (listener != null) {
                        listener.onClosed();
                    }
                    isOpened = false;
                    isForceCancelled = true;
                }

                view.setX(newX);
                view.setY(newY);

                return true;

            case MotionEvent.ACTION_UP:
                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - downRawX;
                float upDY = upRawY - downRawY;


                if (Utils.getLength(upDX, upDY) > CLICK_DRAG_TOLERANCE) {
                    return true;
                }

                viewParent = (View) view.getParent();

                double maxLength = Utils.getLength(view.getBottom(), view.getRight());

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
        if (isOpened) return;

        oldX = view.getX();
        oldY = view.getY();

        float finalX = view.getRight() - view.getHeight();
        float finalY = view.getBottom() - view.getHeight();

        switch (wallPosition) {
            case ALL:
                finalX = 0;
                finalY = 0;
                break;
            case RIGHT:
                finalX = view.getRight() - view.getWidth();
                finalY = 0;
                break;
            case LEFT:
                finalX = viewParent.getWidth() - view.getRight();
                finalY = 0;
                break;
            case TOP:
                finalX = 0;
                finalY = viewParent.getHeight() - view.getBottom();
                break;
            case BOTTOM:
                finalX = 0;
                finalY = view.getBottom() - view.getHeight();
                break;
        }

        float fab_margin = Utils.dp2px(context, 16);
        boolean isRight = false;
        boolean isBottom = false;

        if (wallPosition == WallPosition.ALL || wallPosition == WallPosition.TOP || wallPosition == WallPosition.BOTTOM) {
            if (view.getX() > viewParent.getWidth() / 2 - view.getWidth() / 2) {
                finalX = viewParent.getRight() - view.getWidth();
                finalX = Math.max(0, finalX) - fab_margin;
                isRight = true;
            } else {
                finalX = viewParent.getLeft();
                finalX = Math.min(viewParent.getWidth() - view.getWidth(), finalX) + fab_margin;
                isRight = false;
            }
            if (wallPosition == WallPosition.BOTTOM) isBottom = true;
            else if (wallPosition == WallPosition.TOP) isBottom = false;
        }

        if (wallPosition == WallPosition.ALL || wallPosition == WallPosition.RIGHT || wallPosition == WallPosition.LEFT) {
            if (view.getY() > viewParent.getHeight() / 2 - view.getHeight() / 2) {
                finalY = viewParent.getBottom() - view.getHeight();
                finalY = Math.min(viewParent.getHeight() - view.getHeight(), finalY) - fab_margin;
                isBottom = true;
            } else {
                finalY = viewParent.getTop();
                finalY = Math.max(0, finalY) + fab_margin;
                isBottom = false;
            }
            if (wallPosition == WallPosition.RIGHT) isRight = true;
            else if (wallPosition == WallPosition.LEFT) isRight = false;
        }

        CornerPosition cornerPosition = null;
        if (isRight && isBottom) {
            cornerPosition = CornerPosition.RIGHT_BOTTOM;
        } else if (isRight && !isBottom) {
            cornerPosition = CornerPosition.RIGHT_TOP;
        } else if (!isRight && isBottom) {
            cornerPosition = CornerPosition.LEFT_BOTTOM;
        } else if (!isRight && !isBottom) {
            cornerPosition = CornerPosition.LEFT_TOP;
        }


        double maxLength = Utils.getLength(view.getBottom(), view.getRight());
        double length = Utils.getLength(finalX - oldX, finalY - oldY);

        final float x = finalX;
        final float y = finalY;

        final CornerPosition cp = cornerPosition;

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
                            listener.onOpened(x, y, cp);
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
        if (!isOpened && isForceCancelled) {
            return;
        }

        float currentX = getX();
        float currentY = getY();

        double maxLength = Utils.getLength(getBottom(), getRight());
        double length = Utils.getLength(currentX - oldX, currentY - oldY);

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

    public void setStickWallPosition(WallPosition wallPosition) {
        this.wallPosition = wallPosition;
        invalidate();
    }
}
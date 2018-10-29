package com.gohn.vividfab;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class VividFab extends FrameLayout {
    Context context;
    FrameLayout arcViewGroup;
    List<ArcButton> arcButtons = new ArrayList<>();
    MovableButton movableButton;
    float distance;
    WallPosition wallPosition;

    public VividFab(Context context) {
        this(context, null);
        this.context = context;
        init();
    }

    public VividFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        arcViewGroup = new FrameLayout(context);
        LayoutParams paramsArcViewGroup = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        arcViewGroup.setLayoutParams(paramsArcViewGroup);
        arcViewGroup.setClickable(false);
        arcViewGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                arcViewGroup.setClickable(true);
                for (ArcButton arcButton : arcButtons) {
                    arcButton.close();
                }
            }
        });
        addView(arcViewGroup);

        movableButton = new MovableButton(context);
        LayoutParams paramsFAB = new LayoutParams(Utils.dp2px(context, 56), Utils.dp2px(context, 56));
        paramsFAB.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        paramsFAB.setMargins(Utils.dp2px(context, 16), Utils.dp2px(context, 16), Utils.dp2px(context, 16), Utils.dp2px(context, 16));
        movableButton.setLayoutParams(paramsFAB);
//        movableButton.setImageResource(R.drawable.baseline_apps_white_24);
        addView(movableButton);

        movableButton.setStatusListener(new MovableButton.StatusListener() {
            @Override
            public void onOpened(float x, float y, CornerPosition cornerPosition) {
                arcViewGroup.setClickable(true);
                for (ArcButton arcButton : arcButtons) {
                    int index = arcButtons.indexOf(arcButton);
                    arcButton.open(distance, arcButtons.size(), index, x, y, cornerPosition);

                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#00000000"), Color.parseColor("#64000000"));
                    colorAnimation.setDuration(300);
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            arcViewGroup.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();
                }
            }

            @Override
            public void onClosed() {
                arcViewGroup.setClickable(false);
                for (ArcButton arcButton : arcButtons) {
                    arcButton.close();
                }
            }
        });
    }

    public WallPosition getStickWallPosition() {
        return wallPosition;
    }

    public void setStickWallPosition(WallPosition wallPosition) {
        movableButton.setStickWallPosition(wallPosition);
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setMovableButtonImage(@DrawableRes int resourceId) {
        movableButton.setImageResource(resourceId);
    }

    public void setMovableButtonColor(int color) {
        movableButton.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void addArcItem(ArcButton arcButton) {
        arcButton.setVividFab(this);
        arcButton.setArcStatusListener(new ArcButton.ArcStatusListener() {
            @Override
            public void onOpenFinished() {

            }

            @Override
            public void onCloseFinished() {
                for (ArcButton arcButton : arcButtons) {
                    if (arcButton.isOpened()) return;
                }

                movableButton.close();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#64000000"), Color.parseColor("#00000000"));
                colorAnimation.setDuration(300);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        arcViewGroup.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
            }
        });
        arcButtons.add(arcButton);
        arcViewGroup.addView(arcButton);
    }

    public static ArcButton makeArcButton(Context context) {
        ArcButton arcButton = new ArcButton(context);
        LayoutParams paramsArc = new LayoutParams(Utils.dp2px(context, 50), Utils.dp2px(context, 50));
        arcButton.setCustomSize(Utils.dp2px(context, 50));
        arcButton.setLayoutParams(paramsArc);

        return arcButton;
    }
}
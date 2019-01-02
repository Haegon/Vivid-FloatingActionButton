package com.gohn.vividfab.sample;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.gohn.vividfab.VividFab;
import com.gohn.vividfab.WallPosition;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String PLAY = "play";
    private final static String FF = "ff";
    private final static String REW = "rew";
    private final static String PAUSE = "pause";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VividFab vividFab = findViewById(R.id.float_button);
        vividFab.setStickWallPosition(WallPosition.ALL);
        vividFab.setMovableButtonImage(R.drawable.baseline_apps_white_24);
        vividFab.setDistance(dp2px(this, 120));
        vividFab.addArcItem(getArcButton(PLAY, android.R.drawable.ic_media_play, this));
        vividFab.addArcItem(getArcButton(FF, android.R.drawable.ic_media_ff, this));
        vividFab.addArcItem(getArcButton(REW, android.R.drawable.ic_media_rew, this));
        vividFab.addArcItem(getArcButton(PAUSE, android.R.drawable.ic_media_pause, this));

        findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vividFab.setStickWallPosition(WallPosition.ALL);
            }
        });
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vividFab.setStickWallPosition(WallPosition.RIGHT);
            }
        });
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vividFab.setStickWallPosition(WallPosition.LEFT);
            }
        });
        findViewById(R.id.top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vividFab.setStickWallPosition(WallPosition.TOP);
            }
        });
        findViewById(R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vividFab.setStickWallPosition(WallPosition.BOTTOM);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch ((String) view.getTag()) {
            case PLAY:
                Toast.makeText(this, PLAY + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case FF:
                Toast.makeText(this, FF + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case REW:
                Toast.makeText(this, REW + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case PAUSE:
                Toast.makeText(this, PAUSE + " clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private FloatingActionButton getArcButton(String tag, @DrawableRes int resourceId, View.OnClickListener onClickListener) {
        FloatingActionButton fab = VividFab.makeArcButton(this);
        fab.setImageResource(resourceId);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#876543")));
        fab.setOnClickListener(onClickListener);
        fab.setTag(tag);

        return fab;
    }


    public int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}

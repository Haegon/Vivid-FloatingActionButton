package com.gohn.vividfab.sample;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.gohn.vividfab.ArcButton;
import com.gohn.vividfab.VividFab;
import com.gohn.vividfab.WallPosition;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VividFab vividFab = findViewById(R.id.float_button);
        vividFab.setStickWallPosition(WallPosition.RIGHT);
        vividFab.setMovableButtonImage(R.drawable.baseline_apps_white_24);
        vividFab.setDistance(dp2px(this, 120));
        vividFab.addArcItem(getArcButton("aaa", R.drawable.baseline_apps_white_24, this));
        vividFab.addArcItem(getArcButton("bbb", R.drawable.baseline_apps_white_24, this));
        vividFab.addArcItem(getArcButton("ccc", R.drawable.baseline_apps_white_24, this));
        vividFab.addArcItem(getArcButton("ddd", R.drawable.baseline_apps_white_24, this));
    }

    @Override
    public void onClick(View view) {
        switch ((String) view.getTag()) {
            case "aaa":
                Toast.makeText(this, "aaa clicked", Toast.LENGTH_SHORT).show();
                break;
            case "bbb":
                Toast.makeText(this, "bbb clicked", Toast.LENGTH_SHORT).show();
                break;
            case "ccc":
                Toast.makeText(this, "ccc clicked", Toast.LENGTH_SHORT).show();
                break;
            case "ddd":
                Toast.makeText(this, "ddd clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private ArcButton getArcButton(String tag, @DrawableRes int resourceId, View.OnClickListener onClickListener) {
        ArcButton arcButton = VividFab.makeArcButton(this);
        arcButton.setImageResource(resourceId);
        arcButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#876543")));
        arcButton.setOnClickListener(onClickListener);
        arcButton.setTag(tag);

        return arcButton;
    }


    public int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}

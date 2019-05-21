package wy.experiment.xposed.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chenxinyou on 2019/3/8.
 */

public class IconTextView extends TextView {
    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "iconfont.ttf"));
    }
    public IconTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "iconfont.ttf"));
    }
    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "iconfont.ttf"));
    }
}

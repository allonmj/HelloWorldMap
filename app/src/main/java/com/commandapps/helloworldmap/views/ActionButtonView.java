package com.commandapps.helloworldmap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.commandapps.helloworldmap.R;

/**
 * Created by Michael on 10/11/2014.
 */
public class ActionButtonView extends RelativeLayout {

    private ImageView ivActionSrc;

    public ActionButtonView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_action_button, this);

    }

    public ActionButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ActionButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);

    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ActionButtonView, 0, 0);
        Drawable drawable;
        try {
            drawable = a.getDrawable(R.styleable.ActionButtonView_action_src);
        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_action_button, this);

        ivActionSrc = (ImageView) findViewById(R.id.iv_action_src);
        if (null != drawable) {
            ivActionSrc.setImageDrawable(drawable);
        }
    }

}

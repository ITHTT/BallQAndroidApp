package com.tysci.ballq.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

/**
 * Created by Administrator on 2016-07-14 0014.
 */
public class SettingItemView extends RelativeLayout {
    private TextView tv_name;

    private CircleImageView iv_user_icon;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializing(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializing(context, attrs);
    }

    private void initializing(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_item, this, true);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_user_icon = (CircleImageView) findViewById(R.id.iv_user_icon);

        String title = "";
        String name = "";
        boolean isShowIcon = false;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);

            title = ta.getString(R.styleable.SettingItemView_setting_text_title);
            name = ta.getString(R.styleable.SettingItemView_setting_text_name);
            isShowIcon = ta.getBoolean(R.styleable.SettingItemView_setting_show_icon, false);

            ta.recycle();
        }
        tv_title.setText(title);
        setName(name);

        if (isShowIcon) {
            iv_user_icon.setVisibility(VISIBLE);
            tv_name.setVisibility(GONE);
        } else {
            iv_user_icon.setVisibility(GONE);
            tv_name.setVisibility(VISIBLE);
        }
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        findViewById(R.id.button).setOnClickListener(l);
//    }

    public final <T> void setIcon(T icon) {
        ImageUtil.loadImage(iv_user_icon, R.mipmap.icon_user_default, icon);
    }

    public final <T> void setName(T name) {
        String result;
        if (name == null) {
            result = "";
        } else if (name instanceof Integer) {
            try {
                result = getContext().getResources().getString((Integer) name);
            } catch (Resources.NotFoundException e) {
                result = name.toString();
            }
        } else {
            result = name.toString();
        }
        if (tv_name != null) {
            tv_name.setText(result);
        }
    }
}

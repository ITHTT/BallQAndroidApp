package com.tysci.ballq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.tysci.ballq.R;

/**
 * Created by LinDe
 * on 2016-08-02 0002.
 */
public class UserVStatusImageView extends ImageView
{
    public UserVStatusImageView(Context context)
    {
        this(context, null);
    }

    public UserVStatusImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UserVStatusImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setUserV_Icon(110);
    }

    public void setUserV_Icon(int v_status)
    {
        setVisibility(View.VISIBLE);
        switch (v_status)
        {
            case 0:
            default:
                setVisibility(View.GONE);
                break;
//            case 1:
//                setImageResource(R.mipmap.icon_user_v_mark);
//                break;
            case 110:// 砖家
                setImageResource(R.mipmap.icon_user_level_experts);
                break;
            case 120:// 特约
                setImageResource(R.mipmap.icon_user_level_specail);
                break;
            case 130:// 作者
                setImageResource(R.mipmap.icon_user_level_author);
                break;
        }
    }
}

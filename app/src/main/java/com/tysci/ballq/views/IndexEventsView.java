package com.tysci.ballq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

/**
 * Created by LinDe
 * on 2016-08-01 0001.
 */
public final class IndexEventsView extends LinearLayout implements View.OnClickListener
{
    private CircleImageView mCircleImageView;
    private TextView mTextView;

    private JSONObject mJSONObject;

    public IndexEventsView(Context context)
    {
        this(context, null);
    }

    public IndexEventsView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public IndexEventsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.view_index_events, this, true);

        mCircleImageView = (CircleImageView) findViewById(R.id.circle_image_view);
        mTextView = (TextView) findViewById(R.id.tv_user_nickname);
    }

    public void setJSONObject(JSONObject JSONObject)
    {
        mJSONObject = JSONObject;

        ImageUtil.loadImage(mCircleImageView, R.mipmap.icon_user_default, mJSONObject.getString("pic_url"));
        mTextView.setText(mJSONObject.getString("name"));

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (mJSONObject == null || mJSONObject.isEmpty())
        {
            setOnClickListener(null);
            return;
        }

        BallQBusinessControler.businessControler(getContext(), mJSONObject.getInteger("jump_type"), mJSONObject.getString("jump_url"));
    }
}

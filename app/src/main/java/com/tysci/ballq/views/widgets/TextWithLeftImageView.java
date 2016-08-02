package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016-08-02 0002.
 */
public class TextWithLeftImageView extends LinearLayout
{
    private ImageView mImageView;
    private TextView mTextView;

    public TextWithLeftImageView(Context context)
    {
        this(context, null);
    }

    public TextWithLeftImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextWithLeftImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context, attrs);
    }

    private void initializing(Context context, AttributeSet attrs)
    {
        LayoutInflater.from(context).inflate(R.layout.view_image_text, this, true);

        mImageView = (ImageView) this.findViewById(R.id.image_view);
        mTextView = (TextView) this.findViewById(R.id.text_view);

        int imageResID = R.mipmap.ic_ballq_logo;
        if (attrs != null)
        {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextWithLeftImageView);

            imageResID = ta.getResourceId(R.styleable.TextWithLeftImageView_text_left_image, imageResID);

            ta.recycle();
        }
        setImageViewResources(imageResID);
    }

    public void setImageViewResources(int imageResourcesID)
    {
        mImageView.setImageResource(imageResourcesID);
    }

    public <T> void setText(T text)
    {
        if (text == null)
        {
            mTextView.setText("");
        }
        else
        {
            mTextView.setText(text.toString());
        }
    }

}

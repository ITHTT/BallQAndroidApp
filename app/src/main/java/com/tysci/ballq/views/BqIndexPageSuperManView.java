package com.tysci.ballq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

/**
 * Created by LinDe on 2016-08-03 0003.
 *
 * @see com.tysci.ballq.fragments.BallQIndexPageFragment
 */
public final class BqIndexPageSuperManView extends LinearLayout
{
    public final static int TYPE_ANALYST = 0x1;
    public final static int TYPE_OVERSEAS = 0x2;

    private ViewGroup mGroupAnalyst, mGroupOverseas;

    public BqIndexPageSuperManView(Context context)
    {
        this(context, null);
    }

    public BqIndexPageSuperManView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BqIndexPageSuperManView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.view_bq_index_super_man, this, true);

        mGroupAnalyst = (ViewGroup) this.findViewById(R.id.layout_analyst);
        mGroupOverseas = (ViewGroup) this.findViewById(R.id.layout_overseas);

        updateData(TYPE_ANALYST, null);
        updateData(TYPE_OVERSEAS, null);
    }

    /**
     * @param type  one of {@link #TYPE_ANALYST} or {@link #TYPE_OVERSEAS}
     * @param array {@link JSONArray}
     */
    public void updateData(int type, JSONArray array)
    {
        final ViewGroup group;
        switch (type)
        {
            case TYPE_ANALYST:
                group = mGroupAnalyst;
                break;
            case TYPE_OVERSEAS:
                group = mGroupOverseas;
                break;
            default:
                return;
        }
        final int childCounts = group.getChildCount();
        View child;

        CircleImageView circle_image_view;
        UserVStatusImageView iv_analyst;

        TextView tv_user_nickname;
        TextView tv_content;

        for (int i = 0; i < childCounts; i++)
        {
            child = group.getChildAt(i);
            child.setVisibility(GONE);
        }

        if (array == null || array.isEmpty())
        {
            return;
        }

        JSONObject object;
        for (int i = 0; i < childCounts; i++)
        {
            child = group.getChildAt(i);
            circle_image_view = (CircleImageView) child.findViewById(R.id.circle_image_view);
            iv_analyst = (UserVStatusImageView) child.findViewById(R.id.iv_analyst);
            tv_user_nickname = (TextView) child.findViewById(R.id.tv_user_nickname);
            tv_content = (TextView) child.findViewById(R.id.tv_content);

            try
            {
                object = array.getJSONObject(i);
                if (object != null && !object.isEmpty())
                {
                    child.setVisibility(VISIBLE);

                    ImageUtil.loadImage(circle_image_view, R.mipmap.icon_user_default, object.getString("pt"));

                    tv_user_nickname.setText(object.getString("fname"));
                    tv_content.setText(object.getString("note"));

                    iv_analyst.setUserV_Icon(object.getInteger("v_status"));

                    final int uid = object.getInteger("uid");
                    child.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            final Context context = v.getContext();
                            UserInfoUtil.lookUserInfo(context, uid);
                        }
                    });
                }
                else
                {
                    child.setVisibility(INVISIBLE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

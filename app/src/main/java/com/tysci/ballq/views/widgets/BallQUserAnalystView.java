package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.modles.BallQAuthorAnalystsEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQUserAnalystView extends LinearLayout
{
    private CircleImageView ivUserHeader;
    private ImageView iV;
    private TextView tvUserName;
    private ImageView ivUserLevel;
    private TextView tvUserRankInfo;
    private ImageView ivAttention;
    private TextView tvTipCount;
    private TextView tvWins;
    private TextView tvPopularity;
    private TextView tvUserBreif;
    private BallQAuthorAnalystsEntity authorAnalystsEntity = null;

    public BallQUserAnalystView(Context context)
    {
        super(context);
        initViews(context);
    }

    public BallQUserAnalystView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context);
    }

    public BallQUserAnalystView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQUserAnalystView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_ballq_analyst, this, true);
        ivUserHeader = (CircleImageView) this.findViewById(R.id.ivUserIcon);
        iV = (ImageView) this.findViewById(R.id.isV);
        tvUserName = (TextView) this.findViewById(R.id.tv_user_name);
        ivUserLevel = (ImageView) this.findViewById(R.id.iv_user_level);
        tvUserRankInfo = (TextView) this.findViewById(R.id.tv_user_rank_info);
        ivAttention = (ImageView) this.findViewById(R.id.iv_attention);
        tvTipCount = (TextView) this.findViewById(R.id.tv_tip_count);
        tvWins = (TextView) this.findViewById(R.id.tv_wins);
        tvPopularity = (TextView) this.findViewById(R.id.tv_popularity);
        tvUserBreif = (TextView) this.findViewById(R.id.tv_user_brief);
    }

    public void setBallQAuthorAnalystsInfo(final BallQAuthorAnalystsEntity info)
    {
        this.authorAnalystsEntity = info;
        tvUserName.setText(info.getFname());
        GlideImageLoader.loadImage(getContext(), info.getPt(), R.mipmap.icon_user_default, ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(0, iV, ivUserHeader);
        tvUserBreif.setText(info.getNote());
        UserInfoUtil.setUserLevel(ivUserLevel, info.getV_status());
        CommonUtils.setTextViewFormatString(tvUserRankInfo, info.getRank_type() + "第" + info.getRank() + "名", String.valueOf(info.getRank()), Color.parseColor("#ff0000"), 1f);
        tvWins.setText("胜率: " + String.format(Locale.getDefault(), "%.0f", 100 * info.getWins()) + "%");
        tvPopularity.setText("人气: " + String.valueOf(info.getFcount()));
        tvTipCount.setText("爆料数: " + info.getTips_count());
        ivUserHeader.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfoUtil.lookUserInfo(getContext(), info.getUid());
            }
        });

        ivAttention.setSelected(info.getIsf() == 1);
        ivAttention.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (UserInfoUtil.checkLogin(getContext()))
                {
                    userAttention(info);
                }
                else
                {
                    UserInfoUtil.userLogin(getContext());
                }
            }
        });
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BallQBusinessControler.gotoUserRank(getContext(), info.getR_type(), false);
            }
        });

    }

    private void userAttention(BallQAuthorAnalystsEntity info)
    {
        final Context context = getContext();
        String url = HttpUrls.HOST_URL_V5 + "follow/change/";
        HashMap<String, String> params = new HashMap<>(4);
        params.put("user", UserInfoUtil.getUserId(context));
        params.put("token", UserInfoUtil.getUserToken(context));
        params.put("fid", String.valueOf(info.getUid()));
        if (ivAttention.isSelected())
        {
            params.put("change", "0");
        }
        else
        {
            params.put("change", "1");
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(context, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        int status = obj.getIntValue("status");
                        ToastUtil.show(context, obj.getString("message"));
                        if (status == 350)
                        {
                            ivAttention.setSelected(true);
                        }
                        else if (status == 352)
                        {
                            ivAttention.setSelected(false);
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }
}

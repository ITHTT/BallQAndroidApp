package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQAuthorAnalystsEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.UserInfoUtil;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQUserAnalystView extends LinearLayout{
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

    public BallQUserAnalystView(Context context) {
        super(context);
        initViews(context);
    }

    public BallQUserAnalystView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BallQUserAnalystView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQUserAnalystView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_ballq_analyst,this,true);
        ivUserHeader= (CircleImageView) this.findViewById(R.id.ivUserIcon);
        iV=(ImageView)this.findViewById(R.id.isV);
        tvUserName=(TextView)this.findViewById(R.id.tv_user_name);
        ivUserLevel=(ImageView)this.findViewById(R.id.iv_user_level);
        tvUserRankInfo=(TextView)this.findViewById(R.id.tv_user_rank_info);
        ivAttention=(ImageView)this.findViewById(R.id.iv_attention);
        tvTipCount=(TextView)this.findViewById(R.id.tv_tip_count);
        tvWins=(TextView)this.findViewById(R.id.tv_wins);
        tvPopularity=(TextView)this.findViewById(R.id.tv_popularity);
        tvUserBreif=(TextView)this.findViewById(R.id.tv_user_brief);
    }

    public void setBallQAuthorAnalystsInfo(BallQAuthorAnalystsEntity info){
        tvUserName.setText(info.getFname());
        GlideImageLoader.loadImage(getContext(),info.getPt(),R.mipmap.icon_user_default,ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(0,iV,ivUserHeader);

    }
}

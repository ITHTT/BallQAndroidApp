package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/19.
 */
public class BallQUserRankInfoAdapter extends RecyclerView.Adapter<BallQUserRankInfoAdapter.BallQUserRankInfoViewHolder>{
    private List<BallQUserRankInfoEntity> rankInfoEntityList=null;
    private String rankType;

    public BallQUserRankInfoAdapter(List<BallQUserRankInfoEntity> rankInfoEntityList) {
        this.rankInfoEntityList = rankInfoEntityList;
        EventBus.getDefault().register(this);
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }

    @Override
    public BallQUserRankInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_ranking_item,parent,false);
        return new BallQUserRankInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQUserRankInfoViewHolder holder, final int position) {
        final BallQUserRankInfoEntity info=rankInfoEntityList.get(position);
        holder.tvRanking.setText(String.valueOf(position+1));
        holder.tvUserNickName.setText(info.getFname());
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserHeader);
        holder.tvRecommendRounds.setText(String.valueOf(info.getTipcount()));
        setRankTypeInfo(position, holder, info);
        holder.ivAttention.setSelected(info.getIsf() == 1);

        holder.ivAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAttention(position, holder, info);
            }
        });

        holder.ivUserHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoUtil.lookUserInfo(holder.itemView.getContext(),info.getUid());
            }
        });
    }

    private void setRankTypeInfo(int position,BallQUserRankInfoViewHolder holder,BallQUserRankInfoEntity info){
        if (rankType == null || rankType.equals("")) {
            return;
        }
        holder.tvUserNickName.setVisibility(View.VISIBLE);
        switch (rankType) {
            case "tearn":
                holder.tvProfitably.setText(String.format(Locale.getDefault(), "%.2f", (float)info.getTearn() / 100F));
                break;
            case "ror":
                holder.tvProfitably.setText(String.format(Locale.getDefault(), "%.0f", info.getRor())+"%");
                break;
            case "wins":
                holder.tvProfitably.setText(String.format(Locale.getDefault(), "%.0f", info.getWins())+"%");
                break;
            case "follow":
                holder.tvRanking.setText(String.valueOf(position+1));
                holder.tvProfitably.setText(String.valueOf(info.getFrc()));
                holder.tvUserNickName.setVisibility(View.GONE);
                holder.tvRecommendRounds.setText(info.getFname());
                break;
        }
    }

    private void userAttention(final int positon,BallQUserRankInfoViewHolder holder,final BallQUserRankInfoEntity info){
        final Context context=holder.itemView.getContext();
        String url= HttpUrls.HOST_URL_V5+ "follow/change/";
        HashMap<String,String> params=new HashMap<>(3);
        params.put("user", UserInfoUtil.getUserId(context));
        params.put("token", UserInfoUtil.getUserToken(context));
        params.put("fid",String.valueOf(info.getUid()));
        params.put("change",info.getIsf()==1?"0":"1");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(context, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        ToastUtil.show(context, obj.getString("message"));
                        if (status == 350) {
                            info.setIsf(1);
                        } else if (status == 352) {
                            info.setIsf(0);
                        }
                        notifyItemChanged(positon);
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return rankInfoEntityList.size();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventObject eventObject) {
        if (eventObject != null) {
            String action = eventObject.getEventAction();
            if(action.equals("user_attention")) {
                int uid=eventObject.getData().getInt("uid",-1);
                int isAttention=eventObject.getData().getInt("attention",0);
                if(rankInfoEntityList!=null){
                    int size=rankInfoEntityList.size();
                    for(int i=0;i<size;i++){
                        if(rankInfoEntityList.get(i).getUid()==uid){
                            rankInfoEntityList.get(i).setFrc(isAttention);
                            notifyItemChanged(i);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserRankInfoViewHolder holder) {
        ButterKnife.unbind(holder);
        EventBus.getDefault().unregister(this);
        super.onViewDetachedFromWindow(holder);
    }


    public static final class BallQUserRankInfoViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserHeader;
        @Bind(R.id.isV)
        ImageView ivUserV;
        @Bind(R.id.tvUserNickName)
        TextView tvUserNickName;
        @Bind(R.id.tvRanking)
        TextView tvRanking;
        @Bind(R.id.tvRecommendRounds)
        TextView tvRecommendRounds;
        @Bind(R.id.tvProfitably)
        TextView tvProfitably;
        @Bind(R.id.ivAttention)
        ImageView ivAttention;

        public BallQUserRankInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

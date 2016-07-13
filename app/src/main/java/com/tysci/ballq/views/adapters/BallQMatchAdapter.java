package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.fragments.BallQMatchListFragment;
import com.tysci.ballq.fragments.UserAttentionMatchListFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BallQMatchAdapter extends RecyclerView.Adapter<BallQMatchAdapter.BallQMatchViewHolder>{
    private List<BallQMatchEntity> ballQMatchEntityList=null;
    private String tag;
    private int matchType=0;

    public BallQMatchAdapter(List<BallQMatchEntity> ballQMatchEntityList) {
        this.ballQMatchEntityList = ballQMatchEntityList;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setMatchType(int type){
        this.matchType=type;
    }

    @Override
    public BallQMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_item,parent,false);
        return new BallQMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQMatchViewHolder holder, final int position) {
        final BallQMatchEntity info=ballQMatchEntityList.get(position);
        holder.tvMatchName.setText(info.getTourname());
        Date date= CommonUtils.getDateAndTimeFromGMT(info.getMtime());
        if(date!=null){
            holder.tvMatchTime.setText(CommonUtils.getTimeOfDay(date));
            holder.tvMatchDate.setText(CommonUtils.getMM_ddString(date));
        }else{
            holder.tvMatchTime.setText("");
            holder.tvMatchDate.setText("");
        }
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getHtlogo(), R.drawable.icon_default_team_logo,holder.ivHtLogo);
        holder.tvHtName.setText(info.getHtname());
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getAtlogo(), R.drawable.icon_default_team_logo, holder.ivAtLogo);
        holder.tvAtName.setText(info.getAtname());
        holder.tvMatchState.setText(BallQMatchStateUtil.getMatchState(info.getStatus(), info.getEtype()));
        holder.tvTipNum.setText(String.valueOf(info.getTipcount()));

        setMatchStateInfo(holder, info);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQMatchDetailActivity.class);
                intent.putExtra(BallQMatchDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });

        holder.ivBell.setSelected(info.getIsf() == 1);
        holder.ivBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserInfoUtil.checkLogin(holder.itemView.getContext())){
                    UserInfoUtil.userLogin(holder.itemView.getContext());
                }else{
                    userAttentionMatch(info,info.getIsf()==1,position,holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ballQMatchEntityList.size();
    }


    private void setMatchStateInfo(BallQMatchViewHolder holder, BallQMatchEntity data) {
        KLog.e("group_id:" + data.getGroup_id());
        int groupId=data.getGroup_id();
        switch (groupId) {
            case 0:
            default://进行中
                holder.ivGameState.setImageResource(R.drawable.circle_match_tip_start_in);
                break;
            case 21://未开始
                holder.ivGameState.setImageResource(R.drawable.circle_match_tip_un_start);
                break;
            case 41://已结束
                holder.ivGameState.setImageResource(R.drawable.circle_match_tip_end);
                break;
        }
        if (groupId == 21 || groupId == 0) {
            holder.layoutBet.setVisibility(View.VISIBLE);// 未开始显示投注数量
            holder.tvScore.setVisibility(View.GONE);
            holder.tvBetNum.setText(String.valueOf(data.getBetcount()));
        } else {
            holder.layoutBet.setVisibility(View.GONE);
            holder.tvScore.setVisibility(View.VISIBLE);
            holder.tvScore.setText(String.valueOf(data.getHtscore() + " - " + data.getAtscore()));
        }
        if (groupId == 41) {
            if (data.getIsf() == 1) {
                holder.ivBell.setImageResource(R.drawable.icon_match_attention_selector);
                holder.ivBell.setSelected(true);
                holder.ivBell.setEnabled(true);
                holder.ivBell.setVisibility(View.VISIBLE);
            } else {
                holder.ivBell.setImageResource(R.mipmap.icon_match_bell_not_allow);
                holder.ivBell.setEnabled(false);
                holder.ivBell.setVisibility(View.GONE);
            }
        } else {
            holder.ivBell.setImageResource(R.drawable.icon_match_attention_selector);
            holder.ivBell.setEnabled(true);
            holder.ivBell.setVisibility(View.VISIBLE);
        }
    }

    public void cancelUserAttention(int eid){
        int size=ballQMatchEntityList.size();
        for(int i=0;i<size;i++){
            if(ballQMatchEntityList.get(i).getEid()==eid){
                ballQMatchEntityList.get(i).setIsf(0);
                notifyItemChanged(i);
            }
        }
    }

    private void userAttentionMatch(final BallQMatchEntity data, final boolean isAttention, final int position, final BallQMatchViewHolder holder) {
        HashMap<String, String> params = new HashMap<>();
        params.put("etype", String.valueOf(data.getEtype()));
        params.put("eid", String.valueOf(data.getEid()));
        params.put("did", JPushInterface.getRegistrationID(holder.itemView.getContext()));
        params.put("action", isAttention ? "1" : "0");
        final Context context=holder.itemView.getContext();
        if (UserInfoUtil.checkLogin(context)) {
            params.put("user",UserInfoUtil.getUserId(context));
            params.put("token", UserInfoUtil.getUserToken(context));
        }

        String url= HttpUrls.HOST_URL_V6+"match_follow/change/";
        HttpClientUtil.getHttpClientUtil().sendPostRequest(tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                holder.ivBell.setEnabled(false);
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
                        String message = obj.getString("message");
                        ToastUtil.show(context, message);
                        int status = obj.getIntValue("status");
                        if (status == 810 || status == 812) {
                            /**发送刷新用户关注比赛列表的消息*/
                            if (matchType == 0) {
                                data.setIsf(isAttention ? 0 : 1);
                                EventObject eventObject=new EventObject();
                                eventObject.addReceiver(UserAttentionMatchListFragment.class);
                                EventObject.postEventObject(eventObject,"attention_refresh");
                                notifyItemChanged(position);
                            } else if (status == 812) {
                                ballQMatchEntityList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

                                EventObject eventObject=new EventObject();
                                eventObject.addReceiver(BallQMatchListFragment.class);
                                eventObject.getData().putInt("etype", data.getEtype());
                                eventObject.getData().putInt("id",data.getEid());
                                EventObject.postEventObject(eventObject,"attention_refresh");

                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                holder.ivBell.setEnabled(true);
            }
        });
    }


    @Override
    public void onViewDetachedFromWindow(BallQMatchViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQMatchViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvMatchTime)
        TextView tvMatchTime;
        @Bind(R.id.tvMatchDate)
        TextView tvMatchDate;
        @Bind(R.id.ivHtLogo)
        ImageView ivHtLogo;
        @Bind(R.id.tvHtName)
        TextView tvHtName;
        @Bind(R.id.tvMatchName)
        TextView tvMatchName;
        @Bind(R.id.tvScore)
        TextView tvScore;
        @Bind(R.id.tvMatchState)
        TextView tvMatchState;
        @Bind(R.id.ivGameState)
        ImageView ivGameState;
        @Bind(R.id.tvTipNum)
        TextView tvTipNum;
        @Bind(R.id.vgBet)
        LinearLayout layoutBet;
        @Bind(R.id.tvBetNum)
        TextView tvBetNum;
        @Bind(R.id.ivAtLogo)
        ImageView ivAtLogo;
        @Bind(R.id.tvAtName)
        TextView tvAtName;
        @Bind(R.id.ivBell)
        ImageView ivBell;

        public BallQMatchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

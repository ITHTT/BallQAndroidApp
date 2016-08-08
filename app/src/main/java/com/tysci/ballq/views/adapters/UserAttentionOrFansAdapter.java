package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAttentionActivity;
import com.tysci.ballq.modles.BallQUserAttentionOrFansEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/1.
 */
public class UserAttentionOrFansAdapter extends RecyclerView.Adapter<UserAttentionOrFansAdapter.UserAttentionOrFansViweHolder>
{
    private List<BallQUserAttentionOrFansEntity> userAttentionOrFansEntityList;
    private boolean isSelf = false;
    private String tag;

    public UserAttentionOrFansAdapter(List<BallQUserAttentionOrFansEntity> userAttentionOrFansEntityList)
    {
        this.userAttentionOrFansEntityList = userAttentionOrFansEntityList;
    }

    public void setIsSelf(boolean isSelf)
    {
        this.isSelf = isSelf;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    @Override
    public UserAttentionOrFansViweHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_user_attention_item, parent, false);
        return new UserAttentionOrFansViweHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAttentionOrFansViweHolder holder, int position)
    {
        final BallQUserAttentionOrFansEntity info = userAttentionOrFansEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserIcon);
        holder.tvTipCount.setText(String.valueOf(info.getTipcount()));
        holder.tvUserNickName.setText(info.getFname());
        holder.tvRor.setText(String.format(Locale.getDefault(), "%.2f", info.getRor()) + "%");
        holder.tvWins.setText(String.format(Locale.getDefault(), "%.2f", info.getWins()) + "%");
        holder.ivPush.setSelected(info.getIsa() == 1);
        holder.ivAttention.setSelected(true);
        if (isSelf)
        {
            holder.layoutPush.setVisibility(View.VISIBLE);
            holder.layoutAttention.setVisibility(View.VISIBLE);
            holder.divier.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.layoutPush.setVisibility(View.GONE);
            holder.layoutAttention.setVisibility(View.GONE);
            holder.divier.setVisibility(View.GONE);
        }

        holder.layoutPush.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setPushListener(holder, info);
            }
        });

        holder.layoutAttention.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cancleAttention(holder, info);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
    }

    public void setPushListener(final UserAttentionOrFansViweHolder holder, final BallQUserAttentionOrFansEntity info)
    {
        final Context context = holder.itemView.getContext();
        Map<String, String> params = new HashMap<>(5);
        params.put("user", UserInfoUtil.getUserId(context));
        params.put("token", UserInfoUtil.getUserToken(context));
        params.put("fid", String.valueOf(info.getUid()));
        params.put("did", JPushInterface.getRegistrationID(context));
        KLog.e("attention:" + info.getIsa());
        params.put("attention", info.getIsa() == 1 ? "0" : "1");
        String url = HttpUrls.HOST_URL_V6 + "attention_user_tip/";
        HttpClientUtil.getHttpClientUtil().sendPostRequest(tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                holder.ivPush.setEnabled(false);
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
                        String message = obj.getString("message");
                        ToastUtil.show(context, message);
                        int status = obj.getIntValue("status");
                        if (status == 821)
                        {
                            holder.ivPush.setSelected(true);
                            info.setIsa(1);
                        }
                        else if (status == 820)
                        {
                            holder.ivPush.setSelected(false);
                            info.setIsa(0);
                        }
                    }
                }
                return;
            }

            @Override
            public void onFinish(Call call)
            {
                holder.ivPush.setEnabled(true);
            }
        });

    }

    private void cancleAttention(final UserAttentionOrFansViweHolder holder, BallQUserAttentionOrFansEntity info)
    {
        String url = HttpUrls.HOST_URL_V5 + "follow/change/";
        final Context context = holder.itemView.getContext();
        Map<String, String> params = new HashMap<>();
        params.put("user", UserInfoUtil.getUserId(context));
        params.put("token", UserInfoUtil.getUserToken(context));
        params.put("fid", String.valueOf(info.getUid()));
        params.put("change", "0");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                holder.ivAttention.setEnabled(false);
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
                        String message = obj.getString("message");
                        ToastUtil.show(context, message);
                        int status = obj.getIntValue("status");
                        if (status == 352)
                        {
                            userAttentionOrFansEntityList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());

                            EventObject eventObject = new EventObject();
                            eventObject.addReceiver(UserAttentionActivity.class);
                            eventObject.getData().putInt("size", userAttentionOrFansEntityList.size());
                            EventObject.postEventObject(eventObject, "cancel_attention");
                        }
                    }
                }
                return;
            }

            @Override
            public void onFinish(Call call)
            {
                holder.ivAttention.setEnabled(true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return userAttentionOrFansEntityList.size();
    }

    public static final class UserAttentionOrFansViweHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.tvUserNickName)
        TextView tvUserNickName;
        @Bind(R.id.tvTipCount)
        TextView tvTipCount;
        @Bind(R.id.tvWins)
        TextView tvWins;
        @Bind(R.id.tvRor)
        TextView tvRor;
        @Bind(R.id.ivPush)
        ImageView ivPush;
        @Bind(R.id.ivAttention)
        ImageView ivAttention;
        @Bind(R.id.divider)
        View divier;
        @Bind(R.id.layout_push)
        FrameLayout layoutPush;
        @Bind(R.id.layout_attention)
        FrameLayout layoutAttention;

        public UserAttentionOrFansViweHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

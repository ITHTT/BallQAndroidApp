package com.tysci.ballq.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.BallQUserCollectionEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT
 * on 2016/6/20.
 */
public class BallQUserCollectionRecordAdapter extends RecyclerView.Adapter<BallQUserCollectionRecordAdapter.BallQUserCollectionRecordViewHolder>
{
    private List<BallQUserCollectionEntity> userCollectionEntityList = null;

    public BallQUserCollectionRecordAdapter(List<BallQUserCollectionEntity> userCollectionEntityList)
    {
        this.userCollectionEntityList = userCollectionEntityList;
    }

    @Override
    public BallQUserCollectionRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_user_collection_item, parent, false);
        return new BallQUserCollectionRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQUserCollectionRecordViewHolder holder, int position)
    {
        final BallQUserCollectionEntity info = userCollectionEntityList.get(position);
        holder.tvUserName.setText(info.getFname());

        holder.tvContent.setText(info.getTitle());
        if (info.getEtype() == 0)
        {
            holder.tvType.setText("爆料");
        }
        else if (info.getEtype() == 1)
        {
            holder.tvType.setText("球经");
        }
        else if (info.getEtype() == 2)
        {
            holder.tvType.setText("帖子");
        }

        Date date = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (date != null)
        {
            holder.tvDate.setText(CommonUtils.getMMddString(date));
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteCollect(info, holder);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity activity = (Activity) holder.itemView.getContext();
                int type = info.getEtype();
                Intent intent;
                if (type == 0)
                {
                    intent = new Intent(activity, BallQTipOffDetailActivity.class);
                    BallQTipOffEntity tipOffEntity = new BallQTipOffEntity();
                    tipOffEntity.setId(info.getEid());
                    tipOffEntity.setEid(1);
                    intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipOffEntity);
                    activity.startActivity(intent);
                }
                else if (type == 1)
                {
                    Class<BallQBallWarpDetailActivity> cls = BallQBallWarpDetailActivity.class;
                    intent = new Intent(activity, cls);
                    BallQBallWarpInfoEntity entity = new BallQBallWarpInfoEntity();
                    entity.setId(info.getEid());
                    intent.putExtra(cls.getSimpleName(), entity);
                    activity.startActivity(intent);
                }
                else if (type == 2)
                {
                    intent = new Intent(activity, BallQCircleNoteDetailActivity.class);
                    intent.putExtra("id", String.valueOf(info.getEid()));
                    activity.startActivity(intent);
                }
            }
        });
    }

    private void deleteCollect(BallQUserCollectionEntity info, final BallQUserCollectionRecordViewHolder holder)
    {
        Context context = holder.itemView.getContext();
        String url = HttpUrls.HOST_URL_V5 + "user/favorites/del/";
        HashMap<String, String> params = new HashMap<>(3);
        params.put("fid", String.valueOf(info.getFid()));
        if (UserInfoUtil.checkLogin(context))
        {
            params.put("user", UserInfoUtil.getUserId(context));
            params.put("token", UserInfoUtil.getUserToken(context));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(getClass().getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(holder.itemView.getContext(), "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object != null)
                    {
                        int status = object.getIntValue("status");
                        String msg = object.getString("message");
                        if (status == 0 && msg.equalsIgnoreCase("ok"))
                        {
                            ToastUtil.show(holder.itemView.getContext(), msg);
                            userCollectionEntityList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            return;
                        }
                    }
                    ToastUtil.show(holder.itemView.getContext(), "取消收藏失败");
                }
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return userCollectionEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserCollectionRecordViewHolder holder)
    {
        ButterKnife.unbind(holder);
        super.onViewDetachedFromWindow(holder);
    }

    public static final class BallQUserCollectionRecordViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.tvUserNickName)
        TextView tvUserName;
        @Bind(R.id.tvCollectionType)
        TextView tvType;
        @Bind(R.id.tvDate)
        TextView tvDate;
        @Bind(R.id.ivDelete)
        ImageView ivDelete;
        @Bind(R.id.tvContent)
        TextView tvContent;

        public BallQUserCollectionRecordViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

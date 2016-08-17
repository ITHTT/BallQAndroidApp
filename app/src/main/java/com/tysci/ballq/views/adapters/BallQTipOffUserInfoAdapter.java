package com.tysci.ballq.views.adapters;

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
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.activitys.UserTipOffListRecordActivity;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQTipOffUserInfoAdapter extends RecyclerView.Adapter<BallQTipOffUserInfoAdapter.BallQTipOffUserInfoViewHolder>
{
    private List<BallQUserRankInfoEntity> rankInfoEntityList;

    public BallQTipOffUserInfoAdapter(List<BallQUserRankInfoEntity> rankInfoEntityList)
    {
        this.rankInfoEntityList = rankInfoEntityList;
    }

    @Override
    public BallQTipOffUserInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tip_off_user_item, parent, false);
        return new BallQTipOffUserInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQTipOffUserInfoViewHolder holder, final int position)
    {
        final BallQUserRankInfoEntity info = rankInfoEntityList.get(position);
        holder.tvUuserName.setText(info.getFname());
        holder.tvUserTipCount.setText(String.valueOf(info.getTipcount()));

        holder.tvUserAllProfit.setText(info.getTearn() > 0 ? "+" : "");
        holder.tvUserAllProfit.append(String.format(Locale.getDefault(), "%.2f", info.getTearn() * 1F / 100F));

//        holder.tvUserRecommendCount.setText(String.valueOf(info.getFrc()));
        holder.ivAttention.setSelected(info.getIsf() == 1);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                final Class cls = UserTipOffListRecordActivity.class;
                Intent intent = new Intent(context, cls);
                intent.putExtra("uid", String.valueOf(info.getUid()));
                context.startActivity(intent);
            }
        });
        holder.layout_attention.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                if (UserInfoUtil.checkLogin(context))
                {
                    String url = HttpUrls.HOST_URL_V5 + "follow/change/";
                    HashMap<String, String> params = new HashMap<>(4);
                    params.put("user", UserInfoUtil.getUserId(context));
                    params.put("token", UserInfoUtil.getUserToken(context));
                    params.put("fid", String.valueOf(info.getUid()));
                    if (holder.ivAttention.isSelected())
                    {
                        params.put("change", "0");
                    }
                    else
                    {
                        params.put("change", "1");
                    }
                    final LoadingProgressDialog dialog = new LoadingProgressDialog(context);
                    HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
                    {
                        @Override
                        public void onBefore(Request request)
                        {
                            dialog.show();
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
                                        info.setIsf(1);
                                        notifyItemChanged(position);
                                    }
                                    else if (status == 352)
                                    {
                                        info.setIsf(0);
                                        notifyItemChanged(position);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFinish(Call call)
                        {
                            dialog.dismiss();
                        }
                    });

                }
                else
                {
                    UserInfoUtil.userLogin(context);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return rankInfoEntityList.size();
    }

    public static final class BallQTipOffUserInfoViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.tv_user_name)
        TextView tvUuserName;
        @Bind(R.id.tv_user_tip_count)
        TextView tvUserTipCount;
//        @Bind(R.id.tv_user_recommend_count)
//        TextView tvUserRecommendCount;
        @Bind(R.id.tv_user_all_profit)
        TextView tvUserAllProfit;

        @Bind(R.id.layout_attention)
        ViewGroup layout_attention;
        @Bind(R.id.iv_attention)
        ImageView ivAttention;

        public BallQTipOffUserInfoViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.tysci.ballq.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.dialog.SpinKitProgressDialog;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.TextWithLeftImageView;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-08-02 0002.
 *
 * @author used in 2016-08-01
 */
public class BqTipOffAdapter extends WrapRecyclerAdapter<BallQTipOffEntity, BqTipOffAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_tip_off_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final BallQTipOffEntity tipInfo, final int position)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipInfo);
                context.startActivity(intent);
            }
        });
        holder.ivUserPortrait.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = holder.itemView.getContext();
                UserInfoUtil.lookUserInfo(context, tipInfo.getUid());
            }
        });

        float ftmp;

        // 比赛时间
        CalendarUtil matchCalendar = CalendarUtil.parseStringTZ(tipInfo.getMtime());
        if (matchCalendar == null)
        {
            holder.tvMatchTime.setVisibility(View.GONE);
            holder.tvMatchDate.setVisibility(View.GONE);
        }
        else
        {
            holder.tvMatchTime.setVisibility(View.VISIBLE);
            holder.tvMatchDate.setVisibility(View.VISIBLE);
            holder.tvMatchTime.setText(matchCalendar.getStringFormat("HH:mm"));
            holder.tvMatchDate.setText(matchCalendar.getStringFormat("MM-dd"));
            // 比赛状态
            String matchState = BallQMatchStateUtil.getMatchState(tipInfo.getMstatus(), tipInfo.getEtype());
            holder.tvGameState.setText(matchState);
            // 比赛未开始
            if (matchCalendar.timeMillis > System.currentTimeMillis())
            {
                holder.tvHtScore.setVisibility(View.GONE);
                holder.tvAtScore.setVisibility(View.GONE);
            }
            // 比赛已开始
            else
            {
                holder.tvHtScore.setVisibility(View.VISIBLE);
                holder.tvAtScore.setVisibility(View.VISIBLE);
            }
        }

        // 赛名
        holder.tvMatchEvent.setText(tipInfo.getTourname());

        // 主队数据
        ImageUtil.loadImage(holder.ivHtLogo, tipInfo.getHtlogo());
        holder.tvHtName.setText(tipInfo.getHtname());
        holder.tvHtScore.setText(String.valueOf(tipInfo.getHtscore()));
        // 客队数据
        ImageUtil.loadImage(holder.ivAtLogo, tipInfo.getAtlogo());
        holder.tvAtName.setText(tipInfo.getAtname());
        holder.tvAtScore.setText(String.valueOf(tipInfo.getAtscore()));

        // 玩法
        String choice = MatchBettingInfoUtil.getBettingResultInfo(tipInfo.getChoice(), tipInfo.getOtype(), tipInfo.getOdata());
        holder.tvChoice.setText(choice);
        // 本爆料创建时间
        CalendarUtil tipCreateCalendarUtil = CalendarUtil.parseStringTZ(tipInfo.getCtime());
        if (tipCreateCalendarUtil == null)
        {
            holder.tvTipCreateTime.setVisibility(View.GONE);
        }
        else
        {
            holder.tvTipCreateTime.setVisibility(View.VISIBLE);
            holder.tvTipCreateTime.setText(tipCreateCalendarUtil.getStringFormat("MM-dd HH:mm"));
        }
        // 爆料内容
        holder.tvContent.setText(tipInfo.getCont());

        // 相关数据

        holder.textSam.setText(String.format(Locale.getDefault(), "%.0f", tipInfo.getSam() * 1F / 100F));
        holder.textRead.setText(tipInfo.getReading_count());
        holder.textComment.setText(tipInfo.getComcount());
        holder.textLike.setText(tipInfo.getLike_count());

        holder.tvUserNickName.setText(tipInfo.getFname());

        holder.textTipCount.setText(tipInfo.getTipcount());
        ftmp = tipInfo.getWins();
        holder.textWins.setText((ftmp >= 0 ? "+" : "") + String.format(Locale.getDefault(), "%.0f", ftmp * 100F) + "%");
        ftmp = tipInfo.getRor();
        holder.textRor.setText((ftmp >= 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", ftmp) + "%");

        // 用户头像
        ImageUtil.loadImage(holder.ivUserPortrait, R.mipmap.icon_user_default, tipInfo.getPt());
        UserInfoUtil.setUserHeaderVMark(tipInfo.getIsv(), holder.ivUserV, holder.ivUserPortrait);

        // 收藏
        holder.ivCheckCollection.setSelected(tipInfo.getIsc() == 1);
        holder.ivCheckCollection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkCollection(holder, tipInfo, position);
            }
        });
        // 输赢
        holder.ivBetResult.setVisibility(View.VISIBLE);
        switch (tipInfo.getStatus())
        {
            case -1:
            case 0:
            case 4:// 重新派彩
            default:
                holder.ivBetResult.setVisibility(View.INVISIBLE);
                break;
            case 1:// 赢
                holder.ivBetResult.setImageResource(R.mipmap.watermark_win);
                break;
            case 2:// 输
                holder.ivBetResult.setImageResource(R.mipmap.watermark_lose);
                break;
            case 3:// 走
                holder.ivBetResult.setImageResource(R.mipmap.watermark_gone);
                break;
        }
    }

    private void checkCollection(ViewHolder holder, final BallQTipOffEntity tipInfo, final int position)
    {
        final Context context = holder.itemView.getContext();
        if (!UserInfoUtil.checkLogin(context))
        {
            UserInfoUtil.userLogin(context);
            return;
        }
        String url;
        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(context));
        map.put("token", UserInfoUtil.getUserToken(context));
        if (tipInfo.getIsc() != 1)
        {
            url = HttpUrls.HOST_URL_V5 + "user/favorites/add/";
            map.put("etype", String.valueOf(0));
            map.put("eid", String.valueOf(tipInfo.getId()));
            final SpinKitProgressDialog dialog = new SpinKitProgressDialog((Activity) context);
            HttpClientUtil.getHttpClientUtil().sendPostRequest(TAG, url, map, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {
                    dialog.show();
                }

                @Override
                public void onError(Call call, Exception error)
                {
                    ToastUtil.show(context, R.string.request_error);
                }

                @Override
                public void onSuccess(Call call, String response)
                {
                    KLog.json(response);
                    JSONObject object = JSONObject.parseObject(response);
                    if (JsonParams.isJsonRight(object))
                    {
                        ToastUtil.show(context, "收藏成功");
                        tipInfo.setIsc(1);
                        tipInfo.setFid(object.getInteger("data"));
                        notifyItemChanged(position);
                    }
                    else
                    {
                        ToastUtil.show(context, object.getString(JsonParams.MESSAGE));
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
            url = HttpUrls.HOST_URL_V5 + "user/favorites/del/";
            map.put("fid", String.valueOf(tipInfo.getFid()));
            final SpinKitProgressDialog dialog = new SpinKitProgressDialog((Activity) context);
            HttpClientUtil.getHttpClientUtil().sendPostRequest(TAG, url, map, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {
                    dialog.show();
                }

                @Override
                public void onError(Call call, Exception error)
                {
                    ToastUtil.show(context, R.string.request_error);
                }

                @Override
                public void onSuccess(Call call, String response)
                {
                    KLog.json(response);
                    JSONObject object = JSONObject.parseObject(response);
                    if (JsonParams.isJsonRight(object))
                    {
                        ToastUtil.show(context, "取消收藏成功");
                        tipInfo.setIsc(0);
                        notifyItemChanged(position);
                    }
                    else
                    {
                        ToastUtil.show(context, object.getString(JsonParams.MESSAGE));
                    }
                }

                @Override
                public void onFinish(Call call)
                {
                    dialog.dismiss();
                }
            });
        }
    }

    static class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.tvMatchTime)
        TextView tvMatchTime;// 比赛时间
        @Bind(R.id.tvMatchEvent)
        TextView tvMatchEvent;// 赛名
        @Bind(R.id.tvMatchDate)
        TextView tvMatchDate;// 比赛日期

        @Bind(R.id.ivHtLogo)
        ImageView ivHtLogo;// 主队图标
        @Bind(R.id.tvHtName)
        TextView tvHtName;// 主队队名
        @Bind(R.id.tvHtScore)
        TextView tvHtScore;// 主队得分

        @Bind(R.id.ivAtLogo)
        ImageView ivAtLogo;// 客队图标
        @Bind(R.id.tvAtName)
        TextView tvAtName;// 客队队名
        @Bind(R.id.tvAtScore)
        TextView tvAtScore;// 客队得分

        @Bind(R.id.tvGameState)
        TextView tvGameState;// 比赛状态
        @Bind(R.id.ivCheckCollection)
        ImageView ivCheckCollection;// 收藏

        @Bind(R.id.tvChoice)
        TextView tvChoice;// 作者投注选择
        @Bind(R.id.tvTipCreateTime)
        TextView tvTipCreateTime;// 爆料创建时间
        @Bind(R.id.tvContent)
        TextView tvContent;// 爆料内容

        @Bind(R.id.text_sam)
        TextWithLeftImageView textSam;// 投注额
        @Bind(R.id.text_read)
        TextWithLeftImageView textRead;// 阅读量
        @Bind(R.id.text_comment)
        TextWithLeftImageView textComment;// 评论数
        @Bind(R.id.text_like)
        TextWithLeftImageView textLike;// 点赞数

        @Bind(R.id.tvUserNickName)
        TextView tvUserNickName;// 用户昵称

        @Bind(R.id.text_tip_count)
        TextWithLeftImageView textTipCount;// 爆料数
        @Bind(R.id.text_wins)
        TextWithLeftImageView textWins;// 胜率
        @Bind(R.id.text_ror)
        TextWithLeftImageView textRor;// 盈利率

        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserPortrait;// 用户头像
        @Bind(R.id.isV)
        ImageView ivUserV;// 用户大V标致

        @Bind(R.id.ivBetResult)
        ImageView ivBetResult;// 输赢走待定

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}

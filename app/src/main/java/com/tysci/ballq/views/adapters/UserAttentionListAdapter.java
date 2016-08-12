package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.modles.UserAttentionListEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016-07-20 0020.
 *
 * @see com.tysci.ballq.fragments.UserAttentionMatchListFragment
 */
public class UserAttentionListAdapter extends WrapRecyclerAdapter<UserAttentionListEntity, ButterKnifeRecyclerViewHolder>
{

    @Override
    public ButterKnifeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        switch (viewType)
        {
            case 1:// 比赛
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_attention_list_match_item, parent, false);
                return new ViewHolder(itemView);
            case 2:// 爆料
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_tip_off_item_2, parent, false);
                return new BqTipOffAdapter.ViewHolder(itemView);
            case 3:// 球茎
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_ball_warp_item_2, parent, false);
                return new BqBallWrapAdapter.ViewHolder(itemView);
        }
        return null;
    }

    @Override
    protected void onBindViewHolder(ButterKnifeRecyclerViewHolder holder, UserAttentionListEntity info, int position)
    {
        switch (info.getMtype())
        {
            case 1:// 比赛
                try
                {
                    onBindMatchDataViewHolder((ViewHolder) holder, info);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case 2:// 爆料
                try
                {
                    onBindTipOffViewHolder((BqTipOffAdapter.ViewHolder) holder, info, position);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case 3:// 球茎
                try
                {
                    onBindArticleViewHolder((BqBallWrapAdapter.ViewHolder) holder, info);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 比赛
     */
    @SuppressWarnings("ConstantConditions")
    private void onBindMatchDataViewHolder(ViewHolder holder, UserAttentionListEntity userAttentionListEntity)
    {
        final UserAttentionListEntity.Data info = userAttentionListEntity.getData();

        ImageUtil.loadImage(holder.ivUserIcon, R.mipmap.icon_user_default, info.getPt());
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserIcon);
        holder.tv_user_nickname.setText(info.getFname());

        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(info.getCtime());
        if (createCalendar == null)
        {
            holder.tv_create_time.setVisibility(View.GONE);
        }
        else
        {
            holder.tv_create_time.setVisibility(View.VISIBLE);
            holder.tv_create_time.setText(createCalendar.getStringFormat("MM-dd HH:mm"));
        }

        CalendarUtil matchCal = CalendarUtil.parseStringTZ(info.getMtime());
        if (matchCal != null)
        {
            holder.tv_match_date.setText(matchCal.getStringFormat("MM-dd"));
            holder.tv_match_time.setText(matchCal.getStringFormat("HH:mm"));
        }
        else
        {
            holder.tv_match_date.setText("");
            holder.tv_match_time.setText("");
        }
        if (BallQMatchStateUtil.getMatchState(info.getMstatus(), info.getEtype()).equals("完场"))
        {
            // 完场后VS改为显示比分
            holder.tv_vs.setText(info.getHtscore());
            holder.tv_vs.append(" - ");
            holder.tv_vs.append(info.getAtscore());
        }
        else
        {
            holder.tv_vs.setText(String.valueOf("VS"));
        }

        holder.tvMatchName.setText(info.getTourname());

        holder.tv_home_team_name.setText(info.getHtname());
        ImageUtil.loadImage(holder.iv_home_team_icon, info.getHtlogo());

        holder.tv_away_team_name.setText(info.getAtname());
        ImageUtil.loadImage(holder.iv_away_team_icon, info.getAtlogo());

        holder.tvChoice.setText(MatchBettingInfoUtil.getBettingResultInfo(info.getChoice(), info.getOtype(), info.getOdata()));
        final int sam = info.getSam();
        holder.iv_money_icon.setVisibility(sam == 0 ? View.GONE : View.VISIBLE);
        holder.tvSam.setVisibility(sam == 0 ? View.GONE : View.VISIBLE);
        holder.tvSam.setText(String.format(Locale.getDefault(), "%.2f", sam * 1F / 100F));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BallQMatchEntity matchInfo = new BallQMatchEntity();
                matchInfo.setEid(info.getEid());
                matchInfo.setEtype(info.getEtype());

                final Context context = v.getContext();
                Intent intent = new Intent(context, BallQMatchDetailActivity.class);
                intent.putExtra(BallQMatchDetailActivity.class.getSimpleName(), matchInfo);
                context.startActivity(intent);
            }
        });
        holder.ivUserIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
    }

    /**
     * 爆料
     */
    @SuppressWarnings("ConstantConditions")
    private void onBindTipOffViewHolder(final BqTipOffAdapter.ViewHolder holder, final UserAttentionListEntity info, final int position)
    {
        final UserAttentionListEntity.Data tipInfo = info.getData();

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BallQTipOffEntity info = new BallQTipOffEntity();
                info.setId(tipInfo.getId());
                info.setEid(tipInfo.getEid());
                final Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), info);
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

        holder.textSam.setText(String.format(Locale.getDefault(), "%.2f", tipInfo.getSam() * 1F / 100F));
        holder.textRead.setText(tipInfo.getReading_count());
        holder.textComment.setText(tipInfo.getComcount());
        holder.textLike.setText(tipInfo.getLike_count());

        holder.tvUserNickName.setText(tipInfo.getFname());

        holder.textTipCount.setText(tipInfo.getTipcount());
        ftmp = tipInfo.getWins();
        holder.textWins.setText((ftmp >= 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", ftmp * 100F) + "%");
        ftmp = tipInfo.getRor();
        holder.textRor.setText((ftmp >= 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", ftmp));

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

    private void checkCollection(BqTipOffAdapter.ViewHolder holder, final UserAttentionListEntity.Data tipInfo, final int position)
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
            final LoadingProgressDialog dialog = new LoadingProgressDialog(context);
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
            final LoadingProgressDialog dialog = new LoadingProgressDialog(context);
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

    @SuppressWarnings("ConstantConditions")
    private void onBindArticleViewHolder(final BqBallWrapAdapter.ViewHolder holder, final UserAttentionListEntity userAttentionListEntity)
    {
        final UserAttentionListEntity.Data articleInfo = userAttentionListEntity.getData();

        String tmp;

        holder.tvUserName.setText(articleInfo.getFname());
        holder.tvLikeCounts.setText(String.valueOf(articleInfo.getLike_count()));
        holder.tvCommentCounts.setText(String.valueOf(articleInfo.getComcount()));
        holder.tvReadCounts.setText(String.valueOf(articleInfo.getReading_count()));
        holder.tvRewardCounts.setText(String.valueOf(articleInfo.getBoncount()));
        holder.tvTitle.setText(articleInfo.getTitle());

        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(articleInfo.getCtime());
        if (createCalendar == null)
        {
            holder.tvCreateDate.setVisibility(View.GONE);
        }
        else
        {
            holder.tvCreateDate.setVisibility(View.VISIBLE);
            holder.tvCreateDate.setText(createCalendar.getStringFormat("MM-dd HH:mm"));
        }

        ImageUtil.loadImage(holder.ivInfoConver, R.mipmap.icon_ball_wrap_default_img, articleInfo.getCover());

        ImageUtil.loadImage(holder.ivUserHeader, R.mipmap.icon_user_default, articleInfo.getPt());
        UserInfoUtil.setUserHeaderVMark(articleInfo.getIsv(), holder.isV, holder.ivUserHeader);

        tmp = articleInfo.getTitle1();
        if (TextUtils.isEmpty(tmp))
        {
            holder.ivUserAchievement01.setVisibility(View.GONE);
        }
        else
        {
            holder.ivUserAchievement01.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(holder.ivUserAchievement01, R.mipmap.icon_user_achievement_circle_mark, tmp);
        }
        tmp = articleInfo.getTitle2();
        if (TextUtils.isEmpty(tmp))
        {
            holder.ivUserAchievement02.setVisibility(View.GONE);
        }
        else
        {
            holder.ivUserAchievement02.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(holder.ivUserAchievement02, R.mipmap.icon_user_achievement_circle_mark, tmp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BallQBallWarpInfoEntity info = new BallQBallWarpInfoEntity();
                info.setId(articleInfo.getId());
                final Context context = v.getContext();
                Intent intent = new Intent(context, BallQBallWarpDetailActivity.class);
                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });
        holder.ivUserHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, articleInfo.getUid());
            }
        });

        holder.user_v_status.setUserV_Icon(articleInfo.getIsv());
    }

    @Override
    public int getItemViewType(int position)
    {
//        mtype 1 比赛 2爆料 3球茎
        if (position < 0 || position >= getItemCount())
        {
            return super.getItemViewType(position);
        }
        return getItem(position).getMtype();
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
    {

        @Bind(R.id.tv_match_time)
        TextView tv_match_time;
        @Bind(R.id.tv_match_date)
        TextView tv_match_date;
        @Bind(R.id.tv_vs)
        TextView tv_vs;

        @Bind(R.id.tvMatchName)
        TextView tvMatchName;

        @Bind(R.id.iv_home_team_icon)
        ImageView iv_home_team_icon;
        @Bind(R.id.tv_home_team_name)
        TextView tv_home_team_name;

        @Bind(R.id.iv_away_team_icon)
        ImageView iv_away_team_icon;
        @Bind(R.id.tv_away_team_name)
        TextView tv_away_team_name;

        @Bind(R.id.tvChoice)
        TextView tvChoice;
        @Bind(R.id.iv_money_icon)
        ImageView iv_money_icon;
        @Bind(R.id.tvSam)
        TextView tvSam;

        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView ivUserV;

        @Bind(R.id.tv_user_nickname)
        TextView tv_user_nickname;
        @Bind(R.id.tv_create_time)
        TextView tv_create_time;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}

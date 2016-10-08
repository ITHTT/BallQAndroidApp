package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserVStatusImageView;
import com.tysci.ballq.views.widgets.CircleImageView;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/10/8.
 *
 * @author LinDe
 */

public class BqTipOffVideoAdapter2 extends WrapRecyclerAdapter<BallQTipOffEntity, BqTipOffVideoAdapter2.BqTipOffVideoVH>
{
    @Override
    public BqTipOffVideoVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_video_item_3, parent, false);
        return new BqTipOffVideoVH(itemView);
    }

    @Override
    protected void onBindViewHolder2(BqTipOffVideoVH holder, final BallQTipOffEntity tipOff, int position)
    {
        final Context context = holder.itemView.getContext();
        // portrait
        Glide.with(context).load(HttpUrls.getImageUrl(tipOff.getPt()))
                .asBitmap()
                .override(80, 80)
                .placeholder(R.mipmap.icon_user_default)
                .into(holder.ivUserPortrait);
        // nickname
        holder.tvUserNickname.setText(tipOff.getFname());
        // title
        holder.tvTitle.setText(tipOff.getCont());
        // reading counts
        holder.tvReadingCounts.setText(String.valueOf(tipOff.getReading_count()));
        // comment counts
        holder.tvCommentCounts.setText(String.valueOf(tipOff.getComcount()));
        // like counts
        holder.tvLikeCounts.setText(String.valueOf(tipOff.getLike_count()));
        // user v status
        holder.userVStatus.setUserV_Icon(tipOff.getIsv());
        // create date
        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(tipOff.getCtime());
        holder.tvCreateDate.setText(createCalendar == null ? "" : createCalendar.getStringFormat("MM-dd HH:mm"));
        // item click
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipOff);
                context.startActivity(intent);
            }
        });
        // portrait click
        holder.ivUserPortrait.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfoUtil.lookUserInfo(context, tipOff.getUid());
            }
        });
        // video
        if (tipOff.getRichtext_type() == 2)
        {
            holder.videoPlayer.setVisibility(View.VISIBLE);
            getVideoInfo(tipOff.getVid(), holder.videoPlayer, position);
            // the first image of video
            Glide.with(context).load(HttpUrls.getImageUrl(tipOff.getFirst_image()))
                    .asBitmap()
                    .override(80, 80)
                    .placeholder(R.mipmap.icon_ball_wrap_default_img)
                    .into(holder.videoPlayer.thumbImageView);
        } else
        {
            holder.videoPlayer.setVisibility(View.GONE);
        }
    }

    private void getVideoInfo(String vid, final JCVideoPlayerStandard videoPlayer, int position)
    {
        String url = String.format("https://player.polyv.net/videojson/%s.js", vid);
        HttpClientUtil.getHttpClientUtil().sendGetRequest("BqTipOffVideoAdapter" + position, url, 60, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {

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
                        JSONArray mp4Arrays = obj.getJSONArray("mp4");
                        if (mp4Arrays != null && !mp4Arrays.isEmpty())
                        {
                            videoPlayer.setUp(mp4Arrays.getString(0), "");
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

    class BqTipOffVideoVH extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.video_player)
        JCVideoPlayerStandard videoPlayer;
        @Bind(R.id.iv_user_portrait)
        CircleImageView ivUserPortrait;
        @Bind(R.id.tv_user_nickname)
        TextView tvUserNickname;
        @Bind(R.id.user_v_status)
        UserVStatusImageView userVStatus;

//        @Bind(R.id.iv_ballq_info_author_achievement01)
//        ImageView userAchievement01;
//        @Bind(R.id.iv_ballq_info_author_achievement02)
//        ImageView userAchievement02;

        @Bind(R.id.tv_ballq_info_create_date)
        TextView tvCreateDate;

        @Bind(R.id.tv_ballq_info_title)
        TextView tvTitle;

        @Bind(R.id.tvReadingCount)
        TextView tvReadingCounts;
        @Bind(R.id.tv_ballq_info_comments_count)
        TextView tvCommentCounts;
        @Bind(R.id.tvLikeCount)
        TextView tvLikeCounts;

        BqTipOffVideoVH(View itemView)
        {
            super(itemView);
        }
    }
}

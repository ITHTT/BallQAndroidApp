package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Locale;

import butterknife.Bind;

/**
 * Created by LinDe
 * on 2016-08-04 0004.
 */
public class BqMatchTipOffHistoryAdapter extends WrapRecyclerAdapter<BallQTipOffEntity, BqMatchTipOffHistoryAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_bq_match_tip_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final BallQTipOffEntity tipInfo, int position)
    {

        ImageUtil.loadImage(holder.ivUserIcon, R.mipmap.icon_user_default, tipInfo.getPt());
        UserInfoUtil.setUserHeaderVMark(tipInfo.getIsv(), holder.ivUserV, holder.ivUserIcon);

        holder.tv_user_nickname.setText(tipInfo.getFname());
        holder.tv_comment_counts.setText(String.valueOf(tipInfo.getComcount()));

        holder.tv_match_info.setText(tipInfo.getHtname());
        holder.tv_match_info.append(" ");
        holder.tv_match_info.append(tipInfo.getHtscore());
        holder.tv_match_info.append("-");
        holder.tv_match_info.append(tipInfo.getAtscore());
        holder.tv_match_info.append(" ");
        holder.tv_match_info.append(tipInfo.getAtname());

        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(tipInfo.getCtime());
        if (createCalendar == null)
        {
            holder.tv_create_time.setText("");
        }
        else
        {
            holder.tv_create_time.setText(createCalendar.getStringFormat("MM-dd HH:mm"));
        }

        // 玩法
        String choice = MatchBettingInfoUtil.getBettingResultInfo(tipInfo.getChoice(), tipInfo.getOtype(), tipInfo.getOdata());
        holder.tvChoice.setText(choice);

        int sam = tipInfo.getSam();
        if (sam == 0)
        {
            holder.iv_money_icon.setVisibility(View.GONE);
            holder.tvSam.setText("");
        }
        else
        {
            holder.iv_money_icon.setVisibility(View.VISIBLE);
            holder.tvSam.setText(String.format(Locale.getDefault(), "%.0f", sam * 1F / 100));
        }

        holder.tv_content.setText(tipInfo.getCont());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipInfo);
                context.startActivity(intent);
            }
        });
        holder.ivUserIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, tipInfo.getUid());
            }
        });
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView ivUserV;

        @Bind(R.id.tv_match_info)
        TextView tv_match_info;
        @Bind(R.id.tv_create_time)
        TextView tv_create_time;

        @Bind(R.id.tv_user_nickname)
        TextView tv_user_nickname;
        @Bind(R.id.tv_comment_counts)
        TextView tv_comment_counts;

        @Bind(R.id.tvChoice)
        TextView tvChoice;
        @Bind(R.id.iv_money_icon)
        ImageView iv_money_icon;
        @Bind(R.id.tvSam)
        TextView tvSam;

        @Bind(R.id.tv_content)
        TextView tv_content;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}

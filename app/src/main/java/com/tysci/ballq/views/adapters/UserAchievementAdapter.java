package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAchievementActivity;
import com.tysci.ballq.modles.UserAchievementEntity;
import com.tysci.ballq.utils.ImageUtil;

import java.util.List;

/**
 * Created by LinDe on 2016-07-15 0015.
 * 我的成就适配器
 */
public class UserAchievementAdapter extends RecyclerView.Adapter<UserAchievementAdapter.ViewHolder> {
    private final UserAchievementActivity activity;
    private List<UserAchievementEntity> dataList;

    private boolean showAttained;

    public UserAchievementAdapter(UserAchievementActivity activity, List<UserAchievementEntity> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    public void setShowAttained(boolean showAttained) {
        this.showAttained = showAttained;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_achievement_adapter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserAchievementEntity info = dataList.get(position);

        ImageUtil.loadImage(holder.iv_achievement_logo, R.mipmap.icon_user_achievement_circle_mark, info.getLogo());
        holder.tv_achievement_content.setText(info.getContent());

        if (showAttained) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.addShowing(info.getId());
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_achievement_logo;
        TextView tv_achievement_content;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setId(R.id.layout_parent);
            iv_achievement_logo = (ImageView) itemView.findViewById(R.id.achievement_logo);
            tv_achievement_content = (TextView) itemView.findViewById(R.id.achievement_content);
        }
    }
}

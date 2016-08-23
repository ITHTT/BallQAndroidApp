package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserAchievementActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.dialog.ImageUrlBrowserDialog;
import com.tysci.ballq.modles.UserAchievementEntity;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-07-15 0015.
 * 我的成就适配器
 */
public class UserAchievementAdapter extends RecyclerView.Adapter<UserAchievementAdapter.ViewHolder>
{
    private final UserAchievementActivity activity;
    private final boolean isSelf;

    private List<UserAchievementEntity> dataList;

    private boolean showAttained;

    public UserAchievementAdapter(UserAchievementActivity activity, boolean isSelf, List<UserAchievementEntity> dataList)
    {
        this.activity = activity;
        this.isSelf = isSelf;
        this.dataList = dataList;
    }

    public void setShowAttained(boolean showAttained)
    {
        this.showAttained = showAttained;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_achievement_adapter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final UserAchievementEntity info = dataList.get(position);

        ImageUtil.loadImage(holder.iv_achievement_logo, R.mipmap.icon_user_achievement_circle_mark, info.getLogo());
        holder.tv_achievement_content.setText(info.getContent());

        if (showAttained && isSelf)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    activity.addShowing(info.getId());
                }
            });
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ToastUtil.show(holder.itemView.getContext(), info.getContent());
                }
            });
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                ImageUrlBrowserDialog dialog = new ImageUrlBrowserDialog(v.getContext());
                List<String> tmp = new ArrayList<>();
                for (UserAchievementEntity info : dataList)
                {
                    tmp.add(info.getLogo());
                }
                dialog.addUrl(tmp);
                dialog.setCurrentImageIndex(position);
                dialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.achievement_logo)
        ImageView iv_achievement_logo;
        @Bind(R.id.achievement_content)
        TextView tv_achievement_content;

        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setId(R.id.layout_parent);
//            iv_achievement_logo = (ImageView) itemView.findViewById(R.id.achievement_logo);
//            tv_achievement_content = (TextView) itemView.findViewById(R.id.achievement_content);
        }
    }
}

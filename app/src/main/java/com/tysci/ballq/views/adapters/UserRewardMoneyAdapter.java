package com.tysci.ballq.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tysci.ballq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserRewardMoneyAdapter extends BaseAdapter{
    private List<String> rewardMoneys;

    public UserRewardMoneyAdapter(List<String>rewardMoneys){
        this.rewardMoneys=rewardMoneys;
    }

    @Override
    public int getCount() {
        return rewardMoneys.size();
    }

    @Override
    public Object getItem(int position) {
        return rewardMoneys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserRewardMoneyViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_reward_item,parent,false);
            holder=new UserRewardMoneyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
           holder= (UserRewardMoneyViewHolder) convertView.getTag();
        }
        holder.tvRewardMoney.setText(rewardMoneys.get(position));
        return convertView;
    }

    public static final class UserRewardMoneyViewHolder{
        TextView tvRewardMoney;
        public UserRewardMoneyViewHolder(View view){
            tvRewardMoney= (TextView) view.findViewById(R.id.tv_reward_moneys);
        }
    }
}

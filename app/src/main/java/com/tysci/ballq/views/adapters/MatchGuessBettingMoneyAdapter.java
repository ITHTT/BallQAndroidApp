package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.tysci.ballq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class MatchGuessBettingMoneyAdapter extends BaseAdapter{
    private List<String> moneys;
    private Context context;
    private OnCheckItemListener onCheckItemListener=null;

    private boolean isCheckable=true;
    private String currentCheckedItem=null;

    public MatchGuessBettingMoneyAdapter(Context context,List<String>list){
        this.context=context;
        this.moneys=list;
    }

    public void setCheckable(boolean isCheckable){
        this.isCheckable=isCheckable;
        currentCheckedItem=null;
        notifyDataSetChanged();
    }

    public void reset(){
        currentCheckedItem=null;
        isCheckable=true;
        notifyDataSetChanged();
    }

    public void setOnCheckItemListener(OnCheckItemListener listener){
        this.onCheckItemListener=listener;
    }

    @Override
    public int getCount() {
        return moneys.size();
    }

    @Override
    public Object getItem(int position) {
        return moneys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GameBettingMoneyViewHodler holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_match_guess_betting_item,parent,false);
            holder=new GameBettingMoneyViewHodler(convertView);
            convertView.setTag(holder);
        }else{
            holder= (GameBettingMoneyViewHodler) convertView.getTag();
        }
        final String money=moneys.get(position);
        //KLog.e("money:"+money);
        holder.checkBox.setText(moneys.get(position));
        if(money.equals("0")){
            holder.checkBox.setVisibility(View.GONE);
        }else{
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        if(money.equals(currentCheckedItem)){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setEnabled(isCheckable);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCheckable) {
                    currentCheckedItem = money;
                    notifyDataSetChanged();
                    if (onCheckItemListener != null) {
                        onCheckItemListener.onCheckedItem(position, currentCheckedItem);
                    }
                }
            }
        });

        return convertView;
    }

    public static final class GameBettingMoneyViewHodler{
        CheckBox checkBox;
        public GameBettingMoneyViewHodler(View item){
            checkBox= (CheckBox) item.findViewById(R.id.cb_game_betting_money);
        }
    }

    public interface OnCheckItemListener{
        void onCheckedItem(int postion, String value);
    }
}

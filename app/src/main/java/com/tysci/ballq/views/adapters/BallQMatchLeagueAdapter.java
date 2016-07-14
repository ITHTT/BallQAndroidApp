package com.tysci.ballq.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchLeagueEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/1.
 */
public class BallQMatchLeagueAdapter extends BaseAdapter{
    private List<BallQMatchLeagueEntity> ballQMatchLeagueEntityList;
    private List<String>selectedItems;

    public BallQMatchLeagueAdapter(List<BallQMatchLeagueEntity> leagueEntityList){
        this.ballQMatchLeagueEntityList=leagueEntityList;
        addSelectedItmes();
    }

    public void addSelectedItmes(){
        if(selectedItems==null) {
            this.selectedItems = new ArrayList<>(ballQMatchLeagueEntityList.size());
        }else if(!selectedItems.isEmpty()){
            selectedItems.clear();
        }
        int size=ballQMatchLeagueEntityList.size();
        for(int i=0;i<size;i++){
            selectedItems.add(String.valueOf(ballQMatchLeagueEntityList.get(i).getId()));
        }
        notifyDataSetChanged();
    }

    public void removeSelectedItems(){
        if(selectedItems!=null&&!selectedItems.isEmpty()){
            selectedItems.clear();
        }
        notifyDataSetChanged();
    }

    public List<String> getSelectedItems(){
        return selectedItems;
    }

    @Override
    public int getCount() {
        return ballQMatchLeagueEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return ballQMatchLeagueEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BallQLeagueViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_league_select_item,parent,false);
            holder=new BallQLeagueViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (BallQLeagueViewHolder) convertView.getTag();
        }
        final BallQMatchLeagueEntity info=ballQMatchLeagueEntityList.get(position);
        holder.rbLeagueItem.setText(info.getName());
        holder.rbLeagueItem.setChecked(isChecked(String.valueOf(info.getId())));
        final BallQLeagueViewHolder finalHolder = holder;
        holder.rbLeagueItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked(String.valueOf(info.getId()))){
                    selectedItems.remove(String.valueOf(info.getId()));
                    finalHolder.rbLeagueItem.setChecked(false);
                }else{
                    selectedItems.add(String.valueOf(info.getId()));
                    finalHolder.rbLeagueItem.setChecked(true);
                }
            }
        });
        return convertView;
    }

    public boolean isChecked(String id){
        if(selectedItems!=null&&selectedItems.size()>0){
            if(selectedItems.contains(id)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public static final class BallQLeagueViewHolder{
        RadioButton rbLeagueItem;
        public BallQLeagueViewHolder(View itemView){
            rbLeagueItem= (RadioButton) itemView.findViewById(R.id.rb_league_item);
        }
    }
}

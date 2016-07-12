package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BallQMatchFilterDateAdapter extends RecyclerView.Adapter<BallQMatchFilterDateAdapter.BallQMatchFilterDateViewHolder>{
    private List<String> filterDates;
    private String currentSelectedDate="";
    private OnSelectDateListener onSelectDateListener=null;

    public BallQMatchFilterDateAdapter(List<String> filterDates) {
        this.filterDates = filterDates;
    }

    public void setOnSelectDateListener(OnSelectDateListener listener){
        this.onSelectDateListener=listener;
    }

    public void setCurrentSelectedDate(String currentSelectedDate){
        this.currentSelectedDate=currentSelectedDate;
    }

    @Override
    public BallQMatchFilterDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_filter_date_item,parent,false);
        DisplayMetrics displayMetrics= CommonUtils.getScreenDisplayMetrics(parent.getContext());
        int screenWidth_3 = displayMetrics.widthPixels/ 3;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(screenWidth_3, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        return new BallQMatchFilterDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchFilterDateViewHolder holder, final int position) {
        holder.cbDate.setText(filterDates.get(position));
        if(currentSelectedDate.equals(filterDates.get(position))){
            holder.cbDate.setChecked(true);
            holder.cbDate.setTextSize(16);
        }else{
            holder.cbDate.setChecked(false);
            holder.cbDate.setTextSize(13);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedDate=filterDates.get(position);
                notifyDataSetChanged();
                if(onSelectDateListener!=null){
                    onSelectDateListener.onSelectDateItem(position, filterDates.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.filterDates.size();
    }

    public static final class BallQMatchFilterDateViewHolder extends RecyclerView.ViewHolder{
        CheckBox cbDate;
        public BallQMatchFilterDateViewHolder(View itemView) {
            super(itemView);
            cbDate= (CheckBox) itemView.findViewById(R.id.tv_cb_date);
        }
    }

    public interface OnSelectDateListener{
        void onSelectDateItem(int position, String date);
    }
}

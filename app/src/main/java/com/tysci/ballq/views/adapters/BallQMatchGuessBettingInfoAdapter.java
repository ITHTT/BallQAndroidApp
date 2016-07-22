package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchGuessBettingEntity;
import com.tysci.ballq.utils.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 * 比赛竞猜、投注记录的数据适配器
 */
public class BallQMatchGuessBettingInfoAdapter extends RecyclerView.Adapter<BallQMatchGuessBettingInfoAdapter.MatchGuessBettingInfoViewHolder>{
    private List<BallQMatchGuessBettingEntity> matchGuessBettingEntityList;
    private OnBettingItemListener onBettingItemListener;
    private OnDeleteBettingRecordListener onDeleteBettingRecordListener;
    private int dividerPosition=-1;

    public BallQMatchGuessBettingInfoAdapter(List<BallQMatchGuessBettingEntity> list){
        this.matchGuessBettingEntityList=list;
    }

    public void setOnBettingItemListener(OnBettingItemListener onBettingItemListener){
        this.onBettingItemListener=onBettingItemListener;
    }

    public void setOnDeleteBettingRecordListener(OnDeleteBettingRecordListener listener){
        this.onDeleteBettingRecordListener=listener;
    }

    public void setDividerPosition(int position){
        this.dividerPosition=position;
    }

    public int getDividerPosition(){
        return dividerPosition;
    }

    @Override
    public MatchGuessBettingInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==0){
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_guess_item,parent,false);
        }else if(viewType==1){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_guess_betting_record_item,parent,false);
        }
        return new MatchGuessBettingInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MatchGuessBettingInfoViewHolder holder, final int position) {
        final BallQMatchGuessBettingEntity info=matchGuessBettingEntityList.get(position);
        if(info.getDataType()==0){
            holder.tvMatchGuessLeft.setText("");
            holder.tvMatchGuessCenter.setText("");
            holder.tvMatchGuessRight.setText("");
            setMatchGuessInfo(info, holder);
            View.OnClickListener listener= new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v instanceof TextView){
                        String bettingInfo=((TextView) v).getText().toString();
                        if(onBettingItemListener!=null){
                            String type=getBettingChoiceType(info,v.getId());
                            KLog.e("Type:" + type);
                            onBettingItemListener.onBettingItem(position,bettingInfo,info,type);
                        }
                    }
                }
            };
            holder.tvMatchGuessLeft.setOnClickListener(listener);
            holder.tvMatchGuessCenter.setOnClickListener(listener);
            holder.tvMatchGuessRight.setOnClickListener(listener);
        }else{
            holder.tvMatchBettingResult.setText(info.getBettingInfo());
            holder.tvMatchBettingMoneys.setText(String.valueOf(info.getBettingMoney()));

            if(dividerPosition==position){
                holder.itemView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            }else{
                holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);
            }

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int moneys=info.getBettingMoney();
                    String type=info.getBettingType();
                    int id=info.getId();
                    matchGuessBettingEntityList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                    if(onDeleteBettingRecordListener!=null){
                        onDeleteBettingRecordListener.onDeleteBettingRecord(position,id,type,-moneys);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return matchGuessBettingEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return matchGuessBettingEntityList.get(position).getDataType();
    }

    /**
     * 设置竞猜数据
     * @param info
     * @param holder
     */
    private void setMatchGuessInfo(BallQMatchGuessBettingEntity info, MatchGuessBettingInfoViewHolder holder){
        String type=info.getOtype();
        if(type.equals("SP")){
            parseSP(info,holder);
        }else if(type.equals("AHC")){
            parseAsianPlate(info, holder);
        }else if(type.equals("2W")||type.equals("3W")){
            parseWinDrawLose(info, holder, info.getOtype().equalsIgnoreCase("3W"));
        } else if (type.equals("TO")){
            parseBigOrSmallBall(info, holder);
        }else if(type.equals("HC")){
            parseLetBall(info, holder);
        }
    }

    private void parseSP(BallQMatchGuessBettingEntity data,MatchGuessBettingInfoViewHolder holder) {
        try {
            final JSONObject j = new JSONObject(data.getOdata());
            holder.tvMatchGuessName.setText("亚盘");
            holder.tvMatchGuessLeft.setText(String.valueOf("主队 " + j.getString("HS") + "@" + j.getString("HO")));
            holder.tvMatchGuessLeft.setEnabled(data.getHO_cnt()==0);
            holder.tvMatchGuessRight.setText(String.valueOf("客队 " + j.getString("AS") + "@" + j.getString("AO")));
            holder.tvMatchGuessRight.setEnabled(data.getAO_cnt()==0);
            holder.tvMatchGuessCenter.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            holder.itemView.setVisibility(View.GONE);
        }
    }

    private void parseLetBall(BallQMatchGuessBettingEntity data, MatchGuessBettingInfoViewHolder holder) {
        try {
            JSONObject object=new JSONObject(data.getOdata());
            if(object!=null) {
                final String title = "竞彩让球(" + object.getString("HC") + ")";
                holder.tvMatchGuessName.setText(title);
                final String left = "主胜 " + object.getString("HO");
                final String center = "平 " + object.getString("DO");
                final String right = "客胜 " + object.getString("AO");
                holder.tvMatchGuessLeft.setText(left);
                holder.tvMatchGuessLeft.setEnabled(data.getHO_cnt()==0);
                holder.tvMatchGuessCenter.setText(center);
                holder.tvMatchGuessCenter.setEnabled(data.getDO_cnt()==0);
                holder.tvMatchGuessRight.setText(right);
                holder.tvMatchGuessRight.setEnabled(data.getAO_cnt()==0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseBigOrSmallBall(BallQMatchGuessBettingEntity data, MatchGuessBettingInfoViewHolder holder) {
        try {
            JSONObject object=new JSONObject(data.getOdata());
            final String left = "高于 " + object.getString("T") + "@" + object.getString("OO");
            final String right = "低于 " + object.getString("T") + "@" + object.getString("UO");
            holder.tvMatchGuessName.setText("大小球");
            holder.tvMatchGuessLeft.setText(left);
            holder.tvMatchGuessLeft.setEnabled(data.getOO_cnt()==0);
            holder.tvMatchGuessRight.setText(right);
            holder.tvMatchGuessRight.setEnabled(data.getUO_cnt()==0);
            holder.tvMatchGuessCenter.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseWinDrawLose(BallQMatchGuessBettingEntity data, MatchGuessBettingInfoViewHolder holder, boolean showCenter) {
        try {
            JSONObject object=new JSONObject(data.getOdata());
            if (showCenter) {
                holder.tvMatchGuessName.setText("胜平负");
                holder.tvMatchGuessCenter.setVisibility(View.VISIBLE);
                final String center = "平 " + object.getString("DO");
                holder.tvMatchGuessCenter.setText(center);
                holder.tvMatchGuessCenter.setEnabled(data.getDO_cnt()==0);
            } else {
                holder.tvMatchGuessName.setText("胜负");
                holder.tvMatchGuessCenter.setVisibility(View.GONE);
            }
            final String left = "主胜 " + object.getString("HO");
            final String right = "客胜 " + object.getString("AO");
            holder.tvMatchGuessLeft.setText(left);
            holder.tvMatchGuessLeft.setEnabled(data.getHO_cnt() == 0);
            holder.tvMatchGuessRight.setText(right);
            holder.tvMatchGuessRight.setEnabled(data.getAO_cnt() == 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAsianPlate(BallQMatchGuessBettingEntity data, MatchGuessBettingInfoViewHolder holder) {
        try {
            JSONObject object=new JSONObject(data.getOdata());
            holder.tvMatchGuessName.setText("亚盘");
            String HCH=object.getString("HCH");
            if(object.getDouble("HCH")>0){
                HCH="+"+HCH;
            }
            final String left = "主队 " + HCH + "@" + object.getString("MLH");
            String HCA=object.getString("HCA");
            if(object.getDouble("HCA")>0){
                HCA="+"+HCA;
            }
            final String right = "客队 " + HCA + "@" + object.getString("MLA");
            holder.tvMatchGuessLeft.setText(left);
//            KLog.e("MLH_cnt:"+data.getMLH_cnt());
//            KLog.e("MLA_cnt:"+data.getMLA_cnt());
//            KLog.e("Odata:"+data.getOdata());
//            KLog.e("dataType:"+data.getDataType());
            holder.tvMatchGuessLeft.setEnabled(data.getMLH_cnt() == 0);
            holder.tvMatchGuessCenter.setVisibility(View.GONE);
            holder.tvMatchGuessRight.setText(right);
            holder.tvMatchGuessRight.setEnabled(data.getMLA_cnt()==0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getBettingChoiceType(BallQMatchGuessBettingEntity data, int id) {
        String type=data.getOtype();
        KLog.e("type:" + type);
        KLog.e("id:" + id);
        if(type.equals("SP")){
            switch (id) {
                case R.id.tvLeft:
                    return "HO";
                case R.id.tvRight:
                    return "AO";
            }
        }else if(type.equals("AHC")){
            switch(id){
                case R.id.tvLeft:
                    return "MLH";
                case R.id.tvRight:
                    return "MLA";
            }

        }else if(type.equals("2W")||type.equals("3W")){
            switch (id){
                case R.id.tvLeft:
                    return "HO";
                case R.id.tvCenter:
                    return "DO";
                case R.id.tvRight:
                    return "AO";
            }
        }else if(type.equals("TO")){
            switch (id) {
                case R.id.tvLeft:
                    return "OO";
                case R.id.tvRight:
                    return "UO";
            }
        }else if(type.equals("HC")){
            switch (id) {
                case R.id.tvLeft:
                    return "HO";
                case R.id.tvCenter:
                    return "DO";
                case R.id.tvRight:
                    return "AO";
            }
        }
        return "";
    }

    public void setGuessBettingState(int id,String type,int value){
        int size=matchGuessBettingEntityList.size();
        for(int i=0;i<size;i++){
            if(id==matchGuessBettingEntityList.get(i).getId()){
                setBettingState(i,type,matchGuessBettingEntityList.get(i),value);
                break;
            }
        }
    }

    private void setBettingState(int position,String type,BallQMatchGuessBettingEntity info,int value){
        if(!TextUtils.isEmpty(type)){
            switch(type){
                case "HO":
                    info.setHO_cnt(value);
                    break;
                case "AO":
                    info.setAO_cnt(value);
                    break;
                case "MLH":
                    info.setMLH_cnt(value);
                    break;
                case "MLA":
                    info.setMLA_cnt(value);
                    break;
                case "DO":
                    info.setDO_cnt(value);
                    break;
                case "OO":
                    info.setOO_cnt(value);
                    break;
                case "UO":
                    info.setUO_cnt(value);
                    break;
            }
            notifyItemChanged(position);
        }

    }

    public interface OnBettingItemListener{
        void onBettingItem(int position, String bettingInfo, BallQMatchGuessBettingEntity info, String bettingType);
    }

    public interface OnDeleteBettingRecordListener{
        void onDeleteBettingRecord(int position,int id,String type, int moneys);
    }

    public static final class MatchGuessBettingInfoViewHolder extends RecyclerView.ViewHolder{
        TextView tvMatchGuessName;
        TextView tvMatchGuessLeft;
        TextView tvMatchGuessCenter;
        TextView tvMatchGuessRight;

        TextView tvMatchBettingResult;
        TextView tvMatchBettingInfo;
        TextView tvMatchBettingMoneys;
        ImageView ivDelete;

        public MatchGuessBettingInfoViewHolder(View itemView) {
            super(itemView);
            tvMatchGuessName= (TextView) itemView.findViewById(R.id.tv_match_guess_type);
            tvMatchGuessLeft=(TextView)itemView.findViewById(R.id.tvLeft);
            tvMatchGuessRight=(TextView)itemView.findViewById(R.id.tvRight);
            tvMatchGuessCenter=(TextView)itemView.findViewById(R.id.tvCenter);

            tvMatchBettingResult=(TextView)itemView.findViewById(R.id.tv_match_betting_result);
            tvMatchBettingInfo=(TextView)itemView.findViewById(R.id.tv_match_betting_info);
            tvMatchBettingMoneys=(TextView)itemView.findViewById(R.id.tv_match_betting_moneys);
            ivDelete=(ImageView)itemView.findViewById(R.id.iv_delete);
        }
    }
}

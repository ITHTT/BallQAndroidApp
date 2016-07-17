package com.tysci.ballq.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQGoBettingRecordEntity;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BallQGoBettingRecordAdapter extends RecyclerView.Adapter<BallQGoBettingRecordAdapter.BallQGoBettingRecordViewHolder>
        implements StickyHeaderAdapter<BallQGoBettingRecordAdapter.BallQGoBettingRecordViewHolder> {
    private List<BallQGoBettingRecordEntity> ballQGoBettingRecordEntityList=null;

    public BallQGoBettingRecordAdapter(List<BallQGoBettingRecordEntity>list){
        this.ballQGoBettingRecordEntityList=list;
    }

    @Override
    public BallQGoBettingRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_history_record_item,parent,false);
        return new BallQGoBettingRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQGoBettingRecordViewHolder holder, int position) {
        int index=position+1;
        if(index%2!=0){
            holder.itemView.setBackgroundColor(Color.parseColor("#dfdfdf"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        BallQGoBettingRecordEntity info=ballQGoBettingRecordEntityList.get(position);

        holder.tvMatchLeagueName.setText(info.getTourname_short());
       // holder.tvMatchLeagueTime.setText();
        holder.tvMatchLeagueTime.setText(CommonUtils.getDateAndTimeFormatString(CommonUtils.getDateAndTimeFromGMT(info.getMatch_time()).getTime()));
        holder.tvMatchHomeTeamName.setText(info.getHt_name());
        holder.tvMatchAwayTeamName.setText(info.getAt_name());
        holder.tvGoBettingTime.setText(CommonUtils.getTimeOfDay(CommonUtils.getDateAndTimeFromGMT(info.getGo_ctime()).getTime()));
        holder.tvUserBettingTime.setText(CommonUtils.getTimeOfDay(CommonUtils.getDateAndTimeFromGMT(info.getCtime()).getTime()));
        setBallQGoBettingInfo(info, holder);
        setUserBettingInfo(info,holder);



        setBallQGoBettingResultInfo(info,holder);
    }

    private void setBallQGoBettingInfo(BallQGoBettingRecordEntity matchEntity,BallQGoBettingRecordViewHolder holder){
        String data=matchEntity.getGo_odds_info();
        String choice=matchEntity.getGo_choice();
        try {
            JSONObject object=new JSONObject(data);
            if(choice.equals("MLH")||choice.equals("MLA")){
                holder.tvGoBettingType.setText("亚盘");
                String HCH=object.getString("HCH");
                if(object.getDouble("HCH")>0){
                    HCH="+"+HCH;
                }
                final String left = "主队" + HCH + "@" + object.getString("MLH");
                String HCA=object.getString("HCA");
                if(object.getDouble("HCA")>0){
                    HCA="+"+HCA;
                }
                final String right = "客队" + HCA + "@" + object.getString("MLA");
                holder.tvGoBettingInfo01.setText(left);
                holder.tvGoBettingInfo02.setText(right);
                if(choice.equals("MLH")){
                    holder.tvGoBettingInfo01.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.tvGoBettingInfo01.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.tvGoBettingInfo01.setBackgroundResource(0);
                    holder.tvGoBettingInfo01.setTextColor(Color.parseColor("#100e0f"));
                }

                if(choice.equals("MLA")){
                    holder.tvGoBettingInfo02.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.tvGoBettingInfo02.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.tvGoBettingInfo02.setBackgroundResource(0);
                    holder.tvGoBettingInfo02.setTextColor(Color.parseColor("#100e0f"));
                }
            }else if(choice.equals("OO")||choice.equals("UO")){
                holder.tvGoBettingType.setText("大小球");
                final String left = "高于" + object.getString("T") + "@" + object.getString("OO");
                final String right = "低于" + object.getString("T") + "@" + object.getString("UO");
                holder.tvGoBettingInfo01.setText(left);
                holder.tvGoBettingInfo02.setText(right);
                if(choice.equals("OO")){
                    holder.tvGoBettingInfo01.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.tvGoBettingInfo01.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.tvGoBettingInfo01.setBackgroundResource(0);
                    holder.tvGoBettingInfo01.setTextColor(Color.parseColor("#100e0f"));
                }
                if(choice.equals("UO")){
                    holder.tvGoBettingInfo02.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.tvGoBettingInfo02.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.tvGoBettingInfo02.setBackgroundResource(0);
                    holder.tvGoBettingInfo02.setTextColor(Color.parseColor("#100e0f"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserBettingInfo(BallQGoBettingRecordEntity info,BallQGoBettingRecordViewHolder holder){
        String data=info.getOdds_info();
        String choice=info.getChoice();
        try {
            JSONObject object=new JSONObject(data);
            if(choice.equals("MLH")||choice.equals("MLA")){
                holder.tvUserBettingType.setText("亚盘");
                String HCH=object.getString("HCH");
                if(object.getDouble("HCH")>0){
                    HCH="+"+HCH;
                }
                final String left = "主队" + HCH + "@" + object.getString("MLH");
                String HCA=object.getString("HCA");
                if(object.getDouble("HCA")>0){
                    HCA="+"+HCA;
                }
                final String right = "客队" + HCA + "@" + object.getString("MLA");
                holder.tvUserBettingInfo01.setText(left);
                holder.tvUserBettingInfo02.setText(right);
                if(choice.equals("MLH")){
                    holder.tvUserBettingInfo01.setBackgroundResource(R.drawable.ballq_go_user_betting_info_selected);
                    holder.tvUserBettingInfo01.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    holder.tvUserBettingInfo01.setBackgroundResource(0);
                    holder.tvUserBettingInfo01.setTextColor(Color.parseColor("#100e0f"));
                }

                if(choice.equals("MLA")){
                    holder.tvUserBettingInfo02.setBackgroundResource(R.drawable.ballq_go_user_betting_info_selected);
                    holder.tvUserBettingInfo02.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    holder.tvUserBettingInfo02.setBackgroundResource(0);
                    holder.tvUserBettingInfo02.setTextColor(Color.parseColor("#100e0f"));
                }
            }else if(choice.equals("OO")||choice.equals("UO")){
                holder.tvUserBettingType.setText("大小球");
                final String left = "高于" + object.getString("T") + "@" + object.getString("OO");
                final String right = "低于" + object.getString("T") + "@" + object.getString("UO");
                holder.tvUserBettingInfo01.setText(left);
                holder.tvUserBettingInfo02.setText(right);
                if(choice.equals("OO")){
                    holder.tvUserBettingInfo01.setBackgroundResource(R.drawable.ballq_go_user_betting_info_selected);
                    holder.tvUserBettingInfo01.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    holder.tvUserBettingInfo01.setBackgroundResource(0);
                    holder.tvUserBettingInfo01.setTextColor(Color.parseColor("#100e0f"));
                }
                if(choice.equals("UO")){
                    holder.tvUserBettingInfo02.setBackgroundResource(R.drawable.ballq_go_user_betting_info_selected);
                    holder.tvUserBettingInfo02.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    holder.tvUserBettingInfo02.setBackgroundResource(0);
                    holder.tvUserBettingInfo02.setTextColor(Color.parseColor("#100e0f"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBallQGoBettingResultInfo(BallQGoBettingRecordEntity info,BallQGoBettingRecordViewHolder holder){
        float go_amount=(float)(info.getGo_return_amount()-info.getGo_stake_amount())/100;
        if(go_amount>=0) {
            holder.tvGoBettingMoneys.setText("+"+String.format("%.2f", go_amount) + "金币");
        }else{
            holder.tvGoBettingMoneys.setText(String.format("%.2f", go_amount) + "金币");
        }

        int go_status=info.getGo_status();
        if(go_status==2){
           // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.tvGoBettingMoneys.setTextColor(Color.parseColor("#469c4a"));
        }else if(go_status==1){
           // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.tvGoBettingMoneys.setTextColor(Color.parseColor("#df575a"));
        }else{
           // holder.layoutGoBettingMoneys.setVisibility(View.INVISIBLE);
            holder.tvGoBettingMoneys.setTextColor(Color.parseColor("#3a3a3a"));
        }

        float user_amount=(float)(info.getReturn_amount()-info.getStake_amount())/100;
        if(user_amount>=0){
            holder.tvUserBettingMoneys.setText("+"+String.format("%.2f",user_amount)+"金币");
        }else{
            holder.tvUserBettingMoneys.setText(String.format("%.2f",user_amount)+"金币");
        }

        int user_status=info.getStatus();

        if(user_status==2){
            holder.tvUserBettingMoneys.setTextColor(Color.parseColor("#469c4a"));
        }else if(user_status==1){
            holder.tvUserBettingMoneys.setTextColor(Color.parseColor("#df575a"));
        }else{
            holder.tvUserBettingMoneys.setTextColor(Color.parseColor("#3a3a3a"));
        }

        float yield_gap=(float) info.getYield_gap()/100;
        if(yield_gap>=0){
            holder.tvBettingResult.setText("+"+String.format("%.2f",yield_gap)+"金币");
        }else{
            holder.tvBettingResult.setText(String.format("%.2f",yield_gap)+"金币");
        }

        int bet_status=info.getBet_status();
        if(bet_status==0){
          //平
            holder.ivBettingResult.setImageResource(R.mipmap.icon_ballq_go_state3);
            holder.tvBettingResult.setTextColor(Color.parseColor("#707070"));
        }else if(bet_status==1){
            //赢
            holder.ivBettingResult.setImageResource(R.mipmap.icon_ballq_go_state4);
            holder.tvBettingResult.setTextColor(Color.parseColor("#df575a"));
        }else if(bet_status==-1){
            //**输**/
            holder.ivBettingResult.setImageResource(R.mipmap.icon_ballq_go_state1);
            holder.tvBettingResult.setTextColor(Color.parseColor("#469c4a"));
        }else if(bet_status==2){
            //待定
            holder.ivBettingResult.setImageResource(R.mipmap.icon_ballq_go_state2);
            holder.tvBettingResult.setTextColor(Color.parseColor("#3a3a3a"));
        }

        if(bet_status==2){
            holder.layoutUserBettingMoneys.setVisibility(View.INVISIBLE);
            holder.layoutGoBettingMoneys.setVisibility(View.INVISIBLE);
        }else{
            holder.layoutUserBettingMoneys.setVisibility(View.VISIBLE);
            holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ballQGoBettingRecordEntityList.size();
    }

    @Override
    public String getHeaderId(int position) {
        if(position>=0&&position<getItemCount()){
            BallQGoBettingRecordEntity info=ballQGoBettingRecordEntityList.get(position);
//            Date date=CommonUtils.getDateAndTimeFromGMT(info.getGo_ctime());
//            return CommonUtils.getMM_ddString(date);
            return info.getBetting_time();
        }
        return null;
    }

    @Override
    public BallQGoBettingRecordViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_betting_record_header_item,parent,false);
        return new BallQGoBettingRecordViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BallQGoBettingRecordViewHolder viewholder, int position) {
        if(position>=0&&position<getItemCount()){
            TextView tvTitle= (TextView) viewholder.itemView.findViewById(R.id.tv_title);
            tvTitle.setText(getHeaderId(position));
        }

    }

    public static final class BallQGoBettingRecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvMatchLeagueName;
        TextView tvMatchLeagueTime;
        TextView tvMatchHomeTeamName;
        TextView tvMatchAwayTeamName;
        TextView tvGoBettingType;
        TextView tvGoBettingTime;
        TextView tvGoBettingInfo01;
        TextView tvGoBettingInfo02;
        TextView tvGoBettingMoneys;
        TextView tvUserBettingType;
        TextView tvUserBettingTime;
        TextView tvUserBettingInfo01;
        TextView tvUserBettingInfo02;
        TextView tvUserBettingMoneys;
        ImageView ivBettingResult;
        TextView tvBettingResult;
        ViewGroup layoutGoBettingMoneys;
        ViewGroup layoutUserBettingMoneys;

        public BallQGoBettingRecordViewHolder(View itemView) {
            super(itemView);
            tvMatchLeagueName=(TextView)itemView.findViewById(R.id.tv_match_league_name);
            tvMatchLeagueTime=(TextView)itemView.findViewById(R.id.tv_match_league_time);
            tvMatchHomeTeamName=(TextView)itemView.findViewById(R.id.tv_match_home_team_name);
            tvMatchAwayTeamName=(TextView)itemView.findViewById(R.id.tv_match_away_team_name);

            tvGoBettingType=(TextView)itemView.findViewById(R.id.tv_match_ballq_go_betting_type);
            tvGoBettingTime=(TextView)itemView.findViewById(R.id.tv_ballq_go_betting_time);
            tvGoBettingInfo01=(TextView)itemView.findViewById(R.id.tv_ballq_go_betting_info1);
            tvGoBettingInfo02=(TextView)itemView.findViewById(R.id.tv_ballq_go_betting_info2);
            tvGoBettingMoneys=(TextView)itemView.findViewById(R.id.tv_ballq_go_betting_moneys);

            tvUserBettingType=(TextView)itemView.findViewById(R.id.tv_match_ballq_user_betting_type);
            tvUserBettingTime=(TextView)itemView.findViewById(R.id.tv_ballq_user_betting_time);
            tvUserBettingInfo01=(TextView)itemView.findViewById(R.id.tv_ballq_user_betting_info1);
            tvUserBettingInfo02=(TextView)itemView.findViewById(R.id.tv_ballq_user_betting_info2);
            tvUserBettingMoneys=(TextView)itemView.findViewById(R.id.tv_ballq_user_betting_moneys);

            ivBettingResult=(ImageView)itemView.findViewById(R.id.iv_betting_result);
            tvBettingResult=(TextView)itemView.findViewById(R.id.tv_betting_result);

            layoutGoBettingMoneys= (ViewGroup) itemView.findViewById(R.id.layout_go_betting_moneys);
            layoutUserBettingMoneys= (ViewGroup) itemView.findViewById(R.id.layout_user_betting_moneys);
        }
    }
}

package com.tysci.ballq.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQGoMatchEntity;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQPKGreatWarGoAdapter extends RecyclerView.Adapter<BallQPKGreatWarGoAdapter.BallQPKGreatWarGoViewHolder>{
    private List<BallQGoMatchEntity> ballQGoMatchEntityList=null;

    public BallQPKGreatWarGoAdapter(List<BallQGoMatchEntity>ballQGoMatchEntityList){
        this.ballQGoMatchEntityList=ballQGoMatchEntityList;
    }
    @Override
    public BallQPKGreatWarGoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_pk_item,parent,false);
        return new BallQPKGreatWarGoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQPKGreatWarGoViewHolder holder, final int position) {
        int index=position+1;
        if(index%2!=0){
            holder.layoutContent.setBackgroundColor(Color.parseColor("#dfdfdf"));
        }else{
            holder.layoutContent.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        BallQGoMatchEntity info=ballQGoMatchEntityList.get(position);

        holder.tvMatchLeagueName.setText(info.getTourname_short());
        holder.tvHomeTeamName.setText(info.getHt_name());
        holder.tvAwayTeamName.setText(info.getAt_name());
        holder.tvMatchTime.setText(CommonUtils.getDateAndTimeFormatString(CommonUtils.getDateAndTimeFromGMT(info.getMatch_time()).getTime()));

        parseAsianPlate(info.getUser_choice(),info.getAhc_odds_info(), holder);
        parseBigOrSmallBall(info.getUser_choice(),info.getTo_odds_info(), holder);

        setBallQGoChoice(info,holder);

        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckedItemState(v.getId(),holder);
                String choice= (String) v.getTag();
                KLog.e("user_choice:" + choice);
                ballQGoMatchEntityList.get(position).setUser_choice(choice);
            }
        };

        holder.cbType1BettingInfo01.setOnClickListener(clickListener);
        holder.cbType1BettingInfo02.setOnClickListener(clickListener);
        holder.cbType2BettingInfo01.setOnClickListener(clickListener);
        holder.cbType2BettingInfo02.setOnClickListener(clickListener);
    }

    private void setCheckedItemState(int id,BallQPKGreatWarGoViewHolder holder){
        holder.cbType1BettingInfo01.setChecked(false);
        holder.cbType1BettingInfo02.setChecked(false);
        holder.cbType2BettingInfo01.setChecked(false);
        holder.cbType2BettingInfo02.setChecked(false);
        if(id==R.id.cb_type1_betting_info1){
            holder.cbType1BettingInfo01.setChecked(true);
        }else if(id==R.id.cb_type1_betting_info2){
            holder.cbType1BettingInfo02.setChecked(true);
        }else if(id==R.id.cb_type2_betting_info1){
            holder.cbType2BettingInfo01.setChecked(true);
        }else if(id==R.id.cb_type2_betting_info2){
            holder.cbType2BettingInfo02.setChecked(true);
        }
    }

    private void parseAsianPlate(String userChoice,String data,BallQPKGreatWarGoViewHolder holder) {
        try {
            final JSONObject object = new JSONObject(data);
            holder.tvBettingType01.setText("亚盘");
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
            holder.cbType1BettingInfo01.setText(left);
            holder.cbType1BettingInfo01.setTag("MLH");
            holder.cbType1BettingInfo02.setText(right);
            holder.cbType1BettingInfo02.setTag("MLA");
            if("MLH".equals(userChoice)){
                holder.cbType1BettingInfo01.setChecked(true);
            }else{
                holder.cbType1BettingInfo01.setChecked(false);
            }

            if("MLA".equals(userChoice)){
                holder.cbType1BettingInfo02.setChecked(true);
            }else{
                holder.cbType1BettingInfo02.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseBigOrSmallBall(String userChoice,String data, BallQPKGreatWarGoViewHolder holder) {
        try {
            JSONObject object=new JSONObject(data);
            final String left = "高于" + object.getString("T") + "@" + object.getString("OO");
            final String right = "低于" + object.getString("T") + "@" + object.getString("UO");
            holder.tvBettingType02.setText("大小球");
            holder.cbType2BettingInfo01.setText(left);
            holder.cbType2BettingInfo01.setTag("OO");
            holder.cbType2BettingInfo02.setText(right);
            holder.cbType2BettingInfo02.setTag("UO");

            if("OO".equals(userChoice)){
                holder.cbType2BettingInfo01.setChecked(true);
            }else{
                holder.cbType2BettingInfo01.setChecked(false);
            }

            if("UO".equals(userChoice)){
                holder.cbType2BettingInfo02.setChecked(true);
            }else{
                holder.cbType2BettingInfo02.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBallQGoChoice(BallQGoMatchEntity matchEntity,BallQPKGreatWarGoViewHolder holder){
        int type=matchEntity.getGo_odds_type();
        String data=matchEntity.getGo_odds_info();
        String choice=matchEntity.getGo_choice();
        try {
            JSONObject object=new JSONObject(data);
            if(type==matchEntity.getAhc_odds_type()){
                holder.tvBallqGoBettingType.setText("亚盘");
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
                holder.cbBallqGoBettingInfo01.setText(left);
                holder.cbBallqGoBettingInfo02.setText(right);
                if(choice.equals("MLH")){
                    holder.cbBallqGoBettingInfo01.setChecked(true);
                    holder.ivBallqGoIcon01.setVisibility(View.VISIBLE);
                }else{
                    holder.cbBallqGoBettingInfo01.setChecked(false);
                    holder.ivBallqGoIcon01.setVisibility(View.GONE);
                }

                if(choice.equals("MLA")){
                    holder.cbBallqGoBettingInfo02.setChecked(true);
                    holder.ivBallqGoIcon02.setVisibility(View.VISIBLE);
                }else{
                    holder.cbBallqGoBettingInfo02.setChecked(false);
                    holder.ivBallqGoIcon02.setVisibility(View.GONE);
                }
            }else if(type==matchEntity.getTo_odds_type()){
                holder.tvBallqGoBettingType.setText("大小球");
                final String left = "高于" + object.getString("T") + "@" + object.getString("OO");
                final String right = "低于" + object.getString("T") + "@" + object.getString("UO");
                holder.cbBallqGoBettingInfo01.setText(left);
                holder.cbBallqGoBettingInfo02.setText(right);
                if(choice.equals("OO")){
                    holder.cbBallqGoBettingInfo01.setChecked(true);
                    holder.ivBallqGoIcon01.setVisibility(View.VISIBLE);
                }else{
                    holder.cbBallqGoBettingInfo01.setChecked(false);
                    holder.ivBallqGoIcon01.setVisibility(View.GONE);
                }

                if(choice.equals("UO")){
                    holder.cbBallqGoBettingInfo02.setChecked(true);
                    holder.ivBallqGoIcon02.setVisibility(View.VISIBLE);
                }else{
                    holder.cbBallqGoBettingInfo02.setChecked(false);
                    holder.ivBallqGoIcon02.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ballQGoMatchEntityList.size();
    }

    public static final class BallQPKGreatWarGoViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_match_league_name)
        TextView tvMatchLeagueName;
        @Bind(R.id.tv_home_team_name)
        TextView tvHomeTeamName;
        @Bind(R.id.tv_away_team_name)
        TextView tvAwayTeamName;
        @Bind(R.id.tv_match_time)
        TextView tvMatchTime;
        @Bind(R.id.tv_betting_type_01)
        TextView tvBettingType01;
        @Bind(R.id.cb_type1_betting_info1)
        CheckBox cbType1BettingInfo01;
        @Bind(R.id.cb_type1_betting_info2)
        CheckBox cbType1BettingInfo02;
        @Bind(R.id.tv_betting_type_02)
        TextView tvBettingType02;
        @Bind(R.id.cb_type2_betting_info1)
        CheckBox cbType2BettingInfo01;
        @Bind(R.id.cb_type2_betting_info2)
        CheckBox cbType2BettingInfo02;
        @Bind(R.id.tv_ballq_go_betting_type)
        TextView tvBallqGoBettingType;
        @Bind(R.id.iv_ballq_go_icon_01)
        ImageView ivBallqGoIcon01;
        @Bind(R.id.cb_ballq_go_betting_info_01)
        CheckBox cbBallqGoBettingInfo01;
        @Bind(R.id.iv_ballq_go_icon_02)
        ImageView ivBallqGoIcon02;
        @Bind(R.id.cb_ballq_go_betting_info_02)
        CheckBox cbBallqGoBettingInfo02;
        @Bind(R.id.layout_content)
        ViewGroup layoutContent;

        public BallQPKGreatWarGoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

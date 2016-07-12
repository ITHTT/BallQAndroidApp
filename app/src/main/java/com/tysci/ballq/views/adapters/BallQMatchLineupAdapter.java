package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchLineupEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
public class BallQMatchLineupAdapter extends RecyclerView.Adapter<BallQMatchLineupAdapter.BallQMatchLineupViewHolder>{
    private List<BallQMatchLineupEntity> matchLineupEntityList=null;

    public BallQMatchLineupAdapter(List<BallQMatchLineupEntity>datas){
        this.matchLineupEntityList=datas;
    }

    @Override
    public BallQMatchLineupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==0){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_lineup_picture,parent,false);
        }else if(viewType==1){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_lineup_header,parent,false);
        }else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_lineup_content_item,parent,false);
        }
        return new BallQMatchLineupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchLineupViewHolder holder, int position) {
        int type=getItemViewType(position);
        BallQMatchLineupEntity info=matchLineupEntityList.get(position);
        if(type==0){
            String formations=info.getLineupFormation();
            if(!TextUtils.isEmpty(formations)) {
                holder.tvMatchLineupName.setText(formations);
                String[] infos=formations.split(":");
                if(infos!=null&&infos.length>0){
                    View view=getMatchLineupView(holder.itemView.getContext(),infos[infos.length-1],info);
                    if(holder.layoutMatchLineupPicture.getChildCount()>0) {
                        holder.layoutMatchLineupPicture.removeAllViews();
                    }
                    holder.layoutMatchLineupPicture.addView(view);
                }
            }
        }else if(type==1){
            holder.tvMatchLineupTitle.setText(info.getLineupTitle());
        }else{
            holder.tvPlayerNumber.setText(String.valueOf(info.getShirt_number()));
            holder.tvPlayerName.setText(info.getName());
            holder.tvPlayerPosition.setText(getPosGMDF(info.getPos_GMDF()));
            BallQMatchLineupEntity.MatchStatusEntity statusEntity=info.getStatus();
            if(statusEntity!=null) {
                holder.tvPlayerAppear.setText(getNoEmptyString(statusEntity.getMatches_played_in_tournament()));
                holder.tvPlayerFirst.setText(getNoEmptyString(statusEntity.getStart_xi()));
                holder.tvPlayerScore.setText(getNoEmptyString(statusEntity.getGoals_in_tournamet()));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        BallQMatchLineupEntity info=matchLineupEntityList.get(position);
        if(!TextUtils.isEmpty(info.getLineupFormation())&&info.getMap()!=null){
            return 0;
        }else if(!TextUtils.isEmpty(info.getLineupTitle())){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return matchLineupEntityList.size();
    }

    private String getPosGMDF(String pos_GMDF) {
        if ("G".equals(pos_GMDF)) {
            return "门将";
        } else if ("D".equals(pos_GMDF)) {
            return "后卫";
        } else if ("M".equals(pos_GMDF)) {
            return "中场";
        } else if ("F".equals(pos_GMDF)) {
            return "前锋";
        } else {
            return "";
        }
    }

    private String getNoEmptyString(String value){
        if(TextUtils.isEmpty(value)){
            return "0";
        }else{
            return value;
        }
    }

    private int getMatchLineupMapLayout(String formation) {
        if ("2-3-1-4".equals(formation)) {
            return R.layout.layout_match_lineup_formation_2314;
        } else if ("3-3-1-3".equals(formation)) {
            return R.layout.layout_match_lineup_formation_3313;
        } else if ("3-3-3-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_3331;
        } else if ("3-4-1-2".equals(formation)) {
            return R.layout.layout_match_lineup_formation_3412;
        } else if ("4-1-4-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4141;
        } else if ("4-2-1-3".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4213;
        } else if ("4-2-3-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4231;
        } else if ("4-3-1-2".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4312;
        } else if ("4-3-2-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4321;
        } else if ("4-4-1-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_4411;
        } else if ("3-3-4".equals(formation)) {
            return R.layout.layout_match_lineup_formation_334;
        } else if ("3-4-3".equals(formation)) {
            return R.layout.layout_match_lineup_formation_343;
        } else if ("3-5-2".equals(formation)) {
            return R.layout.layout_match_lineup_formation_352;
        } else if ("4-2-4".equals(formation)) {
            return R.layout.layout_match_lineup_formation_424;
        } else if ("4-3-3".equals(formation)) {
            return R.layout.layout_match_lineup_formation_433;
        } else if ("4-4-2".equals(formation)) {
            return R.layout.layout_match_lineup_formation_442;
        } else if ("4-5-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_451;
        } else if ("5-3-2".equals(formation)) {
            return R.layout.layout_match_lineup_formation_532;
        } else if ("5-4-1".equals(formation)) {
            return R.layout.layout_match_lineup_formation_541;
        }
        return R.layout.layout_match_lineup_formation_2314;
    }

    private View getMatchLineupView(Context context, String information, BallQMatchLineupEntity info) {
        int map=getMatchLineupMapLayout(information);
        final View v = LayoutInflater.from(context).inflate(map,null);
        final TextView[] tv_arr = new TextView[]{
                (TextView) v.findViewById(R.id.tv_1),
                (TextView) v.findViewById(R.id.tv_2),
                (TextView) v.findViewById(R.id.tv_3),
                (TextView) v.findViewById(R.id.tv_4),
                (TextView) v.findViewById(R.id.tv_5),
                (TextView) v.findViewById(R.id.tv_6),
                (TextView) v.findViewById(R.id.tv_7),
                (TextView) v.findViewById(R.id.tv_8),
                (TextView) v.findViewById(R.id.tv_9),
                (TextView) v.findViewById(R.id.tv_10),
                (TextView) v.findViewById(R.id.tv_11)};

        final ImageView[] iv_arr = new ImageView[]{
                (ImageView) v.findViewById(R.id.iv_1),
                (ImageView) v.findViewById(R.id.iv_2),
                (ImageView) v.findViewById(R.id.iv_3),
                (ImageView) v.findViewById(R.id.iv_4),
                (ImageView) v.findViewById(R.id.iv_5),
                (ImageView) v.findViewById(R.id.iv_6),
                (ImageView) v.findViewById(R.id.iv_7),
                (ImageView) v.findViewById(R.id.iv_8),
                (ImageView) v.findViewById(R.id.iv_9),
                (ImageView) v.findViewById(R.id.iv_10),
                (ImageView) v.findViewById(R.id.iv_11)};
        List<Integer> maps=info.getMap();
        if(maps!=null) {
            final int size = maps.size();
            for (int i = 0; i < size; i++) {
                iv_arr[i].setImageResource(i == 0 ? R.mipmap.tag_jersey_1 : R.mipmap.tag_jersey_2);
                tv_arr[i].setTextColor(i == 0 ? Color.parseColor("#ffffff") :Color.parseColor("#3a3a3a"));
                tv_arr[i].setText(maps.get(i).toString());
            }
        }
        return v;
    }

    public static final class BallQMatchLineupViewHolder extends RecyclerView.ViewHolder{
        TextView tvPlayerNumber;
        TextView tvPlayerName;
        TextView tvPlayerPosition;
        TextView tvPlayerAppear;
        TextView tvPlayerFirst;
        TextView tvPlayerScore;
        TextView tvMatchLineupTitle;

        TextView tvMatchLineupName;
        LinearLayout layoutMatchLineupPicture;


        public BallQMatchLineupViewHolder(View layout_matchView) {

            super(layout_matchView);
            tvMatchLineupTitle=(TextView)layout_matchView.findViewById(R.id.tv_ballq_match_lineup_header_title);

            tvMatchLineupName=(TextView)layout_matchView.findViewById(R.id.match_lineup_picture_title);
            layoutMatchLineupPicture=(LinearLayout)layout_matchView.findViewById(R.id.layout_match_lineup_picture);

            tvPlayerAppear=(TextView)layout_matchView.findViewById(R.id.tv_player_appear);
            tvPlayerNumber=(TextView)layout_matchView.findViewById(R.id.tv_player_number);
            tvPlayerName=(TextView)layout_matchView.findViewById(R.id.tv_player_name);
            tvPlayerPosition=(TextView)layout_matchView.findViewById(R.id.tv_player_position);
            tvPlayerFirst=(TextView)layout_matchView.findViewById(R.id.tv_player_first);
            tvPlayerScore=(TextView)layout_matchView.findViewById(R.id.tv_player_score);
        }
    }
}

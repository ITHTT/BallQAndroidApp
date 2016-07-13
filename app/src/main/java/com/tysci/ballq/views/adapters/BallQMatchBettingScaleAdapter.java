package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchBettingScaleEntity;
import com.tysci.ballq.views.widgets.chartview.MatchBettingScaleHorizontalLineView;
import com.tysci.ballq.views.widgets.chartview.MatchBettingScaleLineView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BallQMatchBettingScaleAdapter extends RecyclerView.Adapter<BallQMatchBettingScaleAdapter.BallQMatchBettingScaleViewHolder>{
    private List<BallQMatchBettingScaleEntity> matchBettingScaleEntityList=null;

    public BallQMatchBettingScaleAdapter(List<BallQMatchBettingScaleEntity> datas){
        this.matchBettingScaleEntityList=datas;
    }
    @Override
    public BallQMatchBettingScaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_betting_scale_data,parent,false);
        return new BallQMatchBettingScaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchBettingScaleViewHolder holder, int position) {
        BallQMatchBettingScaleEntity info=matchBettingScaleEntityList.get(position);
        holder.tvStakeAmount.setText(String.valueOf(info.getS()/100));
        holder.tvR.setText(String.valueOf(info.getR()/100));
        setBettingTypeInfo(info, holder);

    }

    private void setBettingTypeInfo(BallQMatchBettingScaleEntity info,BallQMatchBettingScaleViewHolder holder){
        String type=info.getBettingType();
        String typeTitle=null;
        if(type.equals("AHC")){
            typeTitle="亚盘(" + info.getT() + ")球";
            holder.horizontalLineView.setIsBigSmall(false);
            holder.horizontalLineView.setBettingScaleData((float) info.getHs() / 100f, (float) info.getDs() / 100f, (float) info.getAs() / 100f);

            holder.scaleLineView.setIsBigSmall(false);
            holder.scaleLineView.setBettingDatas((float)info.getHr()/100f,(float)info.getDr()/100f,(float)info.getAr()/100f);
        }else if(type.equals("TO")){
            typeTitle="大小球(" + info.getT() + ")";
            holder.horizontalLineView.setIsBigSmall(true);
            holder.horizontalLineView.setBettingScaleData((float)info.getOs()/100f,0f,(float)info.getUs()/100f);
            holder.scaleLineView.setIsBigSmall(true);
            holder.scaleLineView.setBettingDatas((float)info.getOr()/100f,0f,(float)info.getUr()/100f);
        }else if(type.equals("3W")){
            typeTitle="胜平负";
            holder.horizontalLineView.setIsBigSmall(false);
            holder.horizontalLineView.setBettingScaleData((float)info.getHs()/100f,(float)info.getDs()/100f,(float)info.getAs()/100f);

            holder.scaleLineView.setIsBigSmall(false);
            holder.scaleLineView.setBettingDatas((float) info.getHr() / 100f, (float) info.getDr() / 100f, (float) info.getAr() / 100f);
        }else if(type.equals("HC")){
            typeTitle="竞彩让球(" + info.getT() + ")球";
            holder.horizontalLineView.setIsBigSmall(false);
            holder.horizontalLineView.setBettingScaleData((float)info.getHs()/100f,(float)info.getDs()/ 100f, (float)info.getAs()/100f);

            holder.scaleLineView.setIsBigSmall(false);
            holder.scaleLineView.setBettingDatas((float)info.getHr()/100f,(float)info.getDr()/100f,(float)info.getAr()/100f);
        }
        if(!TextUtils.isEmpty(typeTitle)){
            holder.tvOddsType.setText(typeTitle);
        }
    }

    @Override
    public int getItemCount() {
        return matchBettingScaleEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQMatchBettingScaleViewHolder holder) {
        ButterKnife.unbind(holder);
        super.onViewDetachedFromWindow(holder);
    }

    public static final class BallQMatchBettingScaleViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_odds_type)
        TextView tvOddsType;
        @Bind(R.id.tvStakeAmount)
        TextView tvStakeAmount;
        @Bind(R.id.horizontal_lineView)
        MatchBettingScaleHorizontalLineView horizontalLineView;
        @Bind(R.id.tvR)
        TextView tvR;
        @Bind(R.id.scale_line_view)
        MatchBettingScaleLineView scaleLineView;

        public BallQMatchBettingScaleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

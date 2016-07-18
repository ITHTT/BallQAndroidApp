package com.tysci.ballq.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.modles.BallQGoReplayEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallQGoReplayAdapter extends RecyclerView.Adapter<BallQGoReplayAdapter.BallQGoReplayViewHolder> implements StickyHeaderAdapter<BallQGoReplayAdapter.BallQGoReplayViewHolder>{
    private List<BallQGoReplayEntity> replayEntityList=null;

    public BallQGoReplayAdapter(List<BallQGoReplayEntity> replayEntityList) {
        this.replayEntityList = replayEntityList;
    }

    @Override
    public BallQGoReplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_replay_item,parent,false);
        return new BallQGoReplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQGoReplayViewHolder holder, int position) {
        int index=position+1;
        if(index%2!=0){
            holder.itemView.setBackgroundColor(Color.parseColor("#dfdfdf"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        final BallQGoReplayEntity info=replayEntityList.get(position);
        holder.tvTourName.setText(info.getTourname_short()+" "+info.getMatchTime());
        holder.tvHomeName.setText(info.getHt_name());
        holder.tvAwayName.setText(info.getAt_name());
        float stake_amount=info.getStake_amount();
        holder.stake_amount.setText((stake_amount > 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", stake_amount / 100F) + "金币");
        if(stake_amount<0){
            // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.stake_amount.setTextColor(Color.parseColor("#469c4a"));
        }else{
            // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.stake_amount.setTextColor(Color.parseColor("#df575a"));
        }
        setBallQGoBettingInfo(info, holder);
        int win_amount =info.getWin_amount();
        holder.win_amount.setText((win_amount > 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", win_amount * 1f / 100) + "金币");
        if(win_amount<0){
            // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.win_amount.setTextColor(Color.parseColor("#469c4a"));
        }else{
            // holder.layoutGoBettingMoneys.setVisibility(View.VISIBLE);
            holder.win_amount.setTextColor(Color.parseColor("#df575a"));
        }

        holder.tips_count.setText(info.getTips_count() + " 爆料");
        setWinState(holder, info);

        holder.content.setEnabled(!TextUtils.isEmpty(info.getContent()));
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil2.show(holder.itemView.getContext(), content, true);
                toShowPopupWindow(holder.itemView.getContext(), info.getContent());
            }
        });
        holder.tips_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallQMatchEntity entity=getMatchInfo(info);
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQMatchDetailActivity.class);
                intent.putExtra(BallQMatchDetailActivity.class.getSimpleName(), entity);
                context.startActivity(intent);
            }
        });
    }

    private void setBallQGoBettingInfo(BallQGoReplayEntity matchEntity,BallQGoReplayViewHolder holder){
        String data=matchEntity.getOdds_info();
        String choice=matchEntity.getChoice();
        try {
            JSONObject object=new JSONObject(data);
            if(choice.equals("MLH")||choice.equals("MLA")){
                holder.odds_type.setText("亚盘");
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
                holder.odds_info1.setText(left);
                holder.odds_info2.setText(right);
                if(choice.equals("MLH")){
                    holder.odds_info1.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.odds_info1.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.odds_info1.setBackgroundResource(0);
                    holder.odds_info1.setTextColor(Color.parseColor("#100e0f"));
                }

                if(choice.equals("MLA")){
                    holder.odds_info2.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.odds_info2.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.odds_info2.setBackgroundResource(0);
                    holder.odds_info2.setTextColor(Color.parseColor("#100e0f"));
                }
            }else if(choice.equals("OO")||choice.equals("UO")){
                holder.odds_type.setText("大小球");
                final String left = "高于" + object.getString("T") + "@" + object.getString("OO");
                final String right = "低于" + object.getString("T") + "@" + object.getString("UO");
                holder.odds_info1.setText(left);
                holder.odds_info2.setText(right);
                if(choice.equals("OO")){
                    holder.odds_info1.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.odds_info1.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.odds_info1.setBackgroundResource(0);
                    holder.odds_info1.setTextColor(Color.parseColor("#100e0f"));
                }
                if(choice.equals("UO")){
                    holder.odds_info2.setBackgroundResource(R.drawable.ballq_go_betting_info_bg);
                    holder.odds_info2.setTextColor(Color.parseColor("#d2a653"));
                }else{
                    holder.odds_info2.setBackgroundResource(0);
                    holder.odds_info2.setTextColor(Color.parseColor("#100e0f"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWinState(BallQGoReplayViewHolder holder,BallQGoReplayEntity info){
        switch (info.getStatus()) {
            case 1:
                holder.ivStatus.setImageResource(R.mipmap.go_win_flag);
                break;
            case 2:
                holder.ivStatus.setImageResource(R.mipmap.go_lose_flag);
                break;
            case 3:
                holder.ivStatus.setImageResource(R.mipmap.go_gone_flag);
                break;
            default:
                holder.ivStatus.setImageResource(R.mipmap.go_determined_flag);
                break;
        }
    }

    private void toShowPopupWindow(Context context, String content) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        final Activity activity;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            return;
        }
        final WindowManager.LayoutParams windowLayoutParams = activity.getWindow().getAttributes();

        windowLayoutParams.alpha = 0.3f;
        activity.getWindow().setAttributes(windowLayoutParams);

        final View pwView = LayoutInflater.from(context).inflate(R.layout.pw_only_text, null);
        ((TextView) pwView.findViewById(R.id.tvTitle)).setText("复盘");
        ((TextView) pwView.findViewById(R.id.tvContent)).setText(content);

        final PopupWindow pw = new PopupWindow(pwView, (int) (width * 1f * 3 / 4), ViewGroup.LayoutParams.WRAP_CONTENT);
        //pw.setAnimationStyle(R.style.UserTaskPw);// 设置进入退出动画
        //noinspection deprecation
        pw.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片,点击泡泡之外的地方关闭泡泡必须要有这个
        pw.setOutsideTouchable(true);// 设置可以点击泡泡之外关闭泡泡
        pw.setFocusable(true);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                windowLayoutParams.alpha = 1F;
                activity.getWindow().setAttributes(windowLayoutParams);
            }
        });

        pw.showAtLocation(activity.getWindow().peekDecorView(), Gravity.CENTER, 0, 0);

        pwView.findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }

    private BallQMatchEntity getMatchInfo(BallQGoReplayEntity info){
        BallQMatchEntity entity=new BallQMatchEntity();
        entity.setEid(info.getEid());
        entity.setEtype(info.getEtype());
        return entity;
    }

    @Override
    public int getItemCount() {
        return replayEntityList.size();
    }

    @Override
    public String getHeaderId(int position) {
        if(position>=0&&position<getItemCount()){
            String matchDate=replayEntityList.get(position).getMatchTime();
            return matchDate.split(" ")[0];
        }
        return null;
    }

    @Override
    public BallQGoReplayViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_replay_title,parent,false);
        return new BallQGoReplayViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BallQGoReplayViewHolder viewholder, int position) {
        if(position>=0&&position<getItemCount()){
            TextView tvDate= (TextView) viewholder.itemView.findViewById(R.id.tvDate);
            String matchDate=replayEntityList.get(position).getMatchTime();
            tvDate.setText(matchDate.split(" ")[0]);
        }
    }

    public static final class BallQGoReplayViewHolder extends RecyclerView.ViewHolder{
        TextView tvTourName;// 赛事
        TextView tvHomeName;// 主队
        TextView tvAwayName;// 客队
        TextView stake_amount;
        TextView odds_type;
        TextView odds_info1;
        TextView odds_info2;
        ImageView ivStatus;
        TextView win_amount;
        TextView content;
        TextView tips_count;

        public BallQGoReplayViewHolder(View itemView) {
            super(itemView);

            tvTourName = (TextView) itemView.findViewById(R.id.tvTourName);

            tvHomeName = (TextView) itemView.findViewById(R.id.tvHomeName);
            tvAwayName = (TextView) itemView.findViewById(R.id.tvAwayName);

            stake_amount = (TextView) itemView.findViewById(R.id.stake_amount);
            odds_type = (TextView) itemView.findViewById(R.id.odds_type);
            odds_info1 = (TextView) itemView.findViewById(R.id.odds_info1);
            odds_info2 = (TextView) itemView.findViewById(R.id.odds_info2);

            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
            win_amount = (TextView) itemView.findViewById(R.id.win_amount);

            content = (TextView) itemView.findViewById(R.id.content);
            tips_count = (TextView) itemView.findViewById(R.id.tips_count);
        }
    }
}

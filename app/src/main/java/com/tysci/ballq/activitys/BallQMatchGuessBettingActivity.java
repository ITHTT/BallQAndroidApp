package com.tysci.ballq.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.dialog.SpinKitProgressDialog;
import com.tysci.ballq.fragments.BallQMatchListFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchGuessBettingEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchGuessBettingInfoAdapter;
import com.tysci.ballq.views.dialogs.BallQMatchBettingGuessDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/16.
 */
public class BallQMatchGuessBettingActivity extends BaseActivity implements BallQMatchGuessBettingInfoAdapter.OnBettingItemListener,
        BallQMatchGuessBettingInfoAdapter.OnDeleteBettingRecordListener, BallQMatchBettingGuessDialog.OnBettingClickListener
{
    @Bind(R.id.iv_home_team_icon)
    protected ImageView ivHomeTeamIcon;
    @Bind(R.id.tv_home_team_name)
    protected TextView tvHomeTeamName;
    @Bind(R.id.tv_game_time)
    protected TextView tvGameTime;
    @Bind(R.id.tv_game_date)
    protected TextView tvGameDate;
    @Bind(R.id.iv_away_team_icon)
    protected ImageView ivAwayTeamIcon;
    @Bind(R.id.tv_away_team_name)
    protected TextView tvAwayTeamName;
    @Bind(R.id.tv_game_league_name)
    protected TextView tvGameLeagueName;
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @Bind(R.id.bt_betting)
    protected Button btBetting;

    private BallQMatchEntity matchEntity = null;
    private List<BallQMatchGuessBettingEntity> matchGuessBettingEntityList = null;
    private BallQMatchGuessBettingInfoAdapter adapter = null;

    private BallQMatchBettingGuessDialog bettingDialog = null;
//    private LoadingProgressDialog loadingProgressDialog;

    private int bettingCounts = 0;
    private int successBettingCount = 0;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_match_guess_betting;
    }

    @Override
    protected void initViews()
    {
        setTitle("竞猜");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected View getLoadingTargetView()
    {
        return this.findViewById(R.id.layout_match_guess_content);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        matchEntity = intent.getParcelableExtra(Tag);
        if (matchEntity != null)
        {
            initMatchInfo(matchEntity);
            showLoading();
            getMatchGuessInfo(matchEntity.getEid(), matchEntity.getEtype());
        }
    }

//    private void showProgressDialog(String msg){
//        if(loadingProgressDialog==null){
//            loadingProgressDialog=new LoadingProgressDialog(this);
//            loadingProgressDialog.setCanceledOnTouchOutside(false);
//        }
//        loadingProgressDialog.setMessage(msg);
//        loadingProgressDialog.show();
//    }
//
//    private void dimssProgressDialog(){
//        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
//            loadingProgressDialog.dismiss();
//        }
//    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals("betting_success"))
            {
                bettingDialog.dismiss();
                finish();
            }
        }
    }

    private void initMatchInfo(BallQMatchEntity data)
    {
        GlideImageLoader.loadImage(this, data.getHtlogo(), R.drawable.icon_default_team_logo, ivHomeTeamIcon);
        tvHomeTeamName.setText(data.getHtname());
        GlideImageLoader.loadImage(this, data.getAtlogo(), R.drawable.icon_default_team_logo, ivAwayTeamIcon);
        tvAwayTeamName.setText(data.getAtname());
        tvGameLeagueName.setText(data.getTourname());
        Date date = CommonUtils.getDateAndTimeFromGMT(data.getMtime());
        if (date != null)
        {
            if (date.getTime() <= System.currentTimeMillis())
            {
                // 已开始
                String gameState = BallQMatchStateUtil.getMatchState(data.getStatus(), data.getEtype());
                if (!TextUtils.isEmpty(gameState) && gameState.equals("未开始"))
                {
                    tvGameTime.setText(CommonUtils.getTimeOfDay(date));
                    tvGameDate.setText(CommonUtils.getMM_ddString(date));
                }
                else
                {
                    final String tmpScore = data.getHtscore() + " - " + data.getAtscore();
                    tvGameTime.setText(tmpScore);
                    tvGameDate.setText(gameState);
                }
            }
            else
            {
                tvGameTime.setText(CommonUtils.getTimeOfDay(date));
                tvGameDate.setText(CommonUtils.getMM_ddString(date));
            }
        }
    }

    private void getMatchGuessInfo(int eid, int etype)
    {
        String url = HttpUrls.HOST_URL_V5 + "match/" + eid + "/odds_info/?etype=" + etype;
        HashMap<String, String> params = new HashMap<>(2);
        if (UserInfoUtil.checkLogin(this))
        {
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showLoading();
                        getMatchGuessInfo(matchEntity.getEid(), matchEntity.getEtype());
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        JSONArray objArrays = obj.getJSONArray("data");
                        if (objArrays != null && !objArrays.isEmpty())
                        {
                            hideLoad();
                            if (matchGuessBettingEntityList == null)
                            {
                                matchGuessBettingEntityList = new ArrayList<BallQMatchGuessBettingEntity>(3);
                            }
                            getGuessBettingInfos(objArrays, matchGuessBettingEntityList);
                            KLog.e("MLA_cnt:" + matchGuessBettingEntityList.get(0).getMLA_cnt());
                            KLog.e("MLH_cnt:" + matchGuessBettingEntityList.get(0).getMLH_cnt());

                            if (adapter == null)
                            {
                                adapter = new BallQMatchGuessBettingInfoAdapter(matchGuessBettingEntityList);
                                adapter.setOnBettingItemListener(BallQMatchGuessBettingActivity.this);
                                adapter.setDividerPosition(objArrays.size());
                                adapter.setOnDeleteBettingRecordListener(BallQMatchGuessBettingActivity.this);
                                recyclerView.setAdapter(adapter);
                            }
                            else
                            {
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    private void getGuessBettingInfos(JSONArray datas, List<BallQMatchGuessBettingEntity> list)
    {
        int size = datas.size();
        for (int i = 0; i < size; i++)
        {
            BallQMatchGuessBettingEntity entity = new BallQMatchGuessBettingEntity();
            JSONObject obj = datas.getJSONObject(i);
            entity.setMLA_cnt(obj.getIntValue("MLA_cnt"));
            entity.setMLH_cnt(obj.getIntValue("MLH_cnt"));
            entity.setAO_cnt(obj.getIntValue("AO_cnt"));
            entity.setHO_cnt(obj.getIntValue("HO_cnt"));
            entity.setOO_cnt(obj.getIntValue("OO_cnt"));
            entity.setUO_cnt(obj.getIntValue("UO_cnt"));
            entity.setDO_cnt(obj.getIntValue("DO_cnt"));
            entity.setOdata(obj.getString("odata"));
            entity.setOtype(obj.getString("otype"));
            entity.setEid(obj.getIntValue("eid"));
            entity.setEtype(obj.getIntValue("etype"));
            entity.setId(obj.getIntValue("id"));

            KLog.e("MLA_cnt:" + obj.getIntValue("MLA_cnt"));
            KLog.e("MLH_cnt:" + obj.getIntValue("MLH_cnt"));
            KLog.e("MLA_cnt:" + entity.getMLA_cnt());
            KLog.e("MLH_cnt:" + entity.getMLH_cnt());
            list.add(entity);
        }
    }

    private boolean checkBettingInfo(String bettingInfo)
    {
        if (adapter != null)
        {
            int position = adapter.getDividerPosition();
            if (position == -1)
            {
                return true;
            }
            int size = matchGuessBettingEntityList.size();
            for (int i = position; i < size; i++)
            {
                if (bettingInfo.equals(matchGuessBettingEntityList.get(i).getBettingInfo()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBettingItem(int position, String bettingInfo, BallQMatchGuessBettingEntity info, String bettingType)
    {
        boolean isBetting = checkBettingInfo(bettingInfo);
        if (isBetting)
        {
            if (bettingDialog == null)
            {
                bettingDialog = new BallQMatchBettingGuessDialog(this);
                bettingDialog.setBallQData(matchEntity);
                bettingDialog.setOnBettingClickListener(this);
            }
            bettingDialog.reset();
            KLog.e("BettingType:" + bettingType);
            bettingDialog.setBettingInfo(bettingInfo);
            //bettingDialog.setBettingType(type);
            bettingDialog.setBettingInfoType(info, bettingType);
            bettingDialog.show();
        }
        else
        {
            ToastUtil.show(this, "该类型投注已被选择,请选择其他类型");
        }
    }

    @Override
    public void onDeleteBettingRecord(int position, int id, String type, int moneys)
    {
        bettingDialog.addAllBettingMoneys(moneys);
        adapter.setGuessBettingState(id, type, 0);
        int dividerPosition = adapter.getDividerPosition();
        if (dividerPosition == matchGuessBettingEntityList.size())
        {
            if (btBetting.getVisibility() != View.GONE)
            {
                btBetting.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBettingClick(BallQMatchGuessBettingEntity info)
    {
        if (btBetting.getVisibility() != View.VISIBLE)
        {
            btBetting.setVisibility(View.VISIBLE);
        }
        matchGuessBettingEntityList.add(info);
        adapter.setGuessBettingState(info.getId(), info.getBettingType(), 1);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_betting)
    protected void onBetting(View view)
    {
        if (adapter != null)
        {
            int position = adapter.getDividerPosition();
            if (position >= 0)
            {
                int size = matchGuessBettingEntityList.size();
                bettingCounts = size - position;
                KLog.e("bettingCounts:" + bettingCounts);
                successBettingCount = 0;
                btBetting.setText("投注请求中...");
                btBetting.setEnabled(false);
//                showProgressDialog("投注请求中...");
                for (int i = position; i < size; i++)
                {
                    requestBetting(view.getContext(), matchGuessBettingEntityList.get(i));
                }
            }
        }
    }

    private void requestBetting(final Context context, BallQMatchGuessBettingEntity data)
    {
        String url = HttpUrls.HOST_URL_V5 + "match/" + matchEntity.getEid() + "/add_tip/";
        final HashMap<String, String> params = new HashMap<>();
        params.put("stake", String.valueOf(data.getBettingMoney() * 100));
        params.put("choice", data.getBettingType());
        params.put("otype", data.getOtype());
        params.put("eid", String.valueOf(matchEntity.getEid()));
        params.put("etype", String.valueOf(matchEntity.getEtype()));
        params.put("oid", String.valueOf(data.getId()));
        if (UserInfoUtil.checkLogin(this))
        {
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        final SpinKitProgressDialog dialog = new SpinKitProgressDialog(context);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                dialog.show();
            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(context, "投注失败");
                KLog.e("投注失败");
                successBettingCount++;
                if (successBettingCount == bettingCounts)
                {
                    btBetting.setText("投注");
                    btBetting.setEnabled(true);
                }
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.e("投注成功");
                KLog.json(response);
                successBettingCount++;
                if (successBettingCount == bettingCounts)
                {
                    if (!TextUtils.isEmpty(response))
                    {
                        JSONObject object = JSONObject.parseObject(response);
                        if (object != null && !object.isEmpty())
                        {
                            ToastUtil.show(BallQMatchGuessBettingActivity.this, object.getString("message"));
                            if (this != null)
                            {
                                refreshMatchList();
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                dialog.dismiss();
            }
        });
    }

    private void refreshMatchList()
    {
        EventObject eventObject = new EventObject();
        eventObject.addReceiver(BallQMatchListFragment.class);
        eventObject.getData().putInt("eid", matchEntity.getEid());
        eventObject.getData().putInt("etype", matchEntity.getEtype());
        EventObject.postEventObject(eventObject, "betting_success");
    }
}

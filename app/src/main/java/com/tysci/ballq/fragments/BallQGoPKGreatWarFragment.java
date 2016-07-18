package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQGoMatchEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQPKGreatWarGoAdapter;
import com.tysci.ballq.views.dialogs.BallQGoBettingRulesTipDialog;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 * pk大战
 */
public class BallQGoPKGreatWarFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.tv_betting_tip)
    protected TextView tvBettingTip;
    @Bind(R.id.bt_betting)
    protected Button btBetting;

    private BallQPKGreatWarGoAdapter adapter=null;
    private List<BallQGoMatchEntity> goMatchEntityList=null;
    private BallQGoBettingRulesTipDialog rulesTipDialog=null;
    private LoadingProgressDialog loadingProgressDialog;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_pk_great_war_go;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        btBetting.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        swipeRefresh.setOnRefreshListener(this);
        btBetting.setEnabled(false);
        addHeaderView();
        showLoading();
        requestDatas();
    }

    private void addHeaderView(){
        ImageView imageView=new ImageView(this.getActivity());
        imageView.setImageResource(R.mipmap.icon_ballq_go_pk_header);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.addHeaderView(imageView);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(baseActivity);
            loadingProgressDialog.setCanceledOnTouchOutside(false);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog(){
        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
            loadingProgressDialog.dismiss();
        }
    }

    private void onRefreshCompelete(){
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    private void requestDatas(){
        String url= HttpUrls.HOST_URL_V5+"go/matches/";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (adapter == null) {
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestDatas();
                        }
                    });
                } else {
                    ToastUtil.show(baseActivity, "请求失败");
                }

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        if (status == 0) {
                            JSONObject data = obj.getJSONObject("data");
                            if (data != null && !data.isEmpty()) {
                                publishBallQGOTimeRangeInfo(data);
                                JSONArray array = data.getJSONArray("matches");
                                if (array != null && !array.isEmpty()) {
                                    btBetting.setEnabled(true);
                                    hideLoad();
                                    if (goMatchEntityList == null) {
                                        goMatchEntityList = new ArrayList<BallQGoMatchEntity>(10);
                                    }
                                    if (!goMatchEntityList.isEmpty()) {
                                        goMatchEntityList.clear();
                                    }
                                    CommonUtils.getJSONListObject(array, goMatchEntityList, BallQGoMatchEntity.class);
                                    if (adapter == null) {
                                        adapter = new BallQPKGreatWarGoAdapter(goMatchEntityList);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    private void publishBallQGOTimeRangeInfo(JSONObject obj){
        String start=obj.getString("go_start");
        String end=obj.getString("go_end");
        int stake=obj.getIntValue("go_stake_amount");
        EventObject eventObject=new EventObject();
        eventObject.addReceiver(BallQGoGreatWarReViewFragment.class);
        eventObject.getData().putString("go_start", start);
        eventObject.getData().putString("go_end", end);
        eventObject.getData().putInt("go_stake", stake);
        EventObject.postEventObject(eventObject, "ballq_go_info");
    }

    @OnClick(R.id.bt_betting_rule)
    protected void onClickRules(View view){
        if(rulesTipDialog==null){
            rulesTipDialog=new BallQGoBettingRulesTipDialog(this.getActivity());
        }
        rulesTipDialog.show();
    }

    @OnClick(R.id.bt_betting)
    protected void onUserBetting(View view){
        if(UserInfoUtil.checkLogin(baseActivity)){
            postBettingInfo();
        }else{
            UserInfoUtil.userLogin(baseActivity);
        }
    }

    private void postBettingInfo(){
        HashMap<String,String> params=new HashMap<>(3);
        params.put("user", UserInfoUtil.getUserId(baseActivity));
        params.put("token", UserInfoUtil.getUserToken(baseActivity));
        final String bettingInfo=getBettingInfos(goMatchEntityList);
        if(TextUtils.isEmpty(bettingInfo)){
            ToastUtil.show(getActivity(), "必须投注所有比赛");
            return;
        }else{
            KLog.e("投注信息:"+bettingInfo);
            params.put("bets", bettingInfo);
        }
        showProgressDialog("投注请求中...");
        String url=HttpUrls.HOST_URL_V5+"go/add_bet/";
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                btBetting.setEnabled(false);
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(baseActivity, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        ToastUtil.show(baseActivity, obj.getString("message"));
                        int status = obj.getIntValue("status");
                        if (status == 1200) {
                            EventObject eventObject = new EventObject();
                            eventObject.addReceiver(BallQGoGreatWarHistoryFragment.class);
                            EventObject.postEventObject(eventObject, "refresh_great_war_history");
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                btBetting.setEnabled(true);
                dimssProgressDialog();
            }
        });
    }

    private String getBettingInfos(List<BallQGoMatchEntity> datas){
        if(datas!=null&&!datas.isEmpty()){
            int size=datas.size();
            org.json.JSONArray jsonArray=new org.json.JSONArray();
            for(int i=0;i<size;i++){
                BallQGoMatchEntity info=datas.get(i);
                if(!TextUtils.isEmpty(info.getUser_choice())){
                    org.json.JSONObject object=new org.json.JSONObject();
                    try {
                        object.put("goinfo_id",info.getGoinfo_id());
                        object.put("eid",info.getEid());
                        object.put("etype",info.getEtype());
                        String choice=info.getUser_choice();
                        object.put("choice",choice);
                        if(choice.equals("MLH")||choice.equals("MLA")){
                            object.put("oid",info.getAhc_odds_id());
                        }else{
                            object.put("oid",info.getTo_odds_id());
                        }
                        jsonArray.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    return null;
                }
            }
            return jsonArray.toString();
        }
        return null;
    }


    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onRefresh() {
        requestDatas();

    }
}

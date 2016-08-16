package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.fragments.BallQMatchListFragment;
import com.tysci.ballq.modles.BallQMatchLeagueEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQMatchLeagueAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/30.
 */
public class BallQMatchLeagueSelectActivity extends BaseActivity
{
    @Bind(R.id.rb_select_all)
    protected RadioButton btSelectAll;
    @Bind(R.id.rb_select_nothing)
    protected RadioButton btSelectNothing;
    @Bind(R.id.gv_leagues)
    protected GridView gvLeagues;

    private List<BallQMatchLeagueEntity> leagueEntities;
    private BallQMatchLeagueAdapter adapter = null;
    private int type = 0;
    private String date;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_match_league_select;
    }

    @Override
    protected void initViews()
    {
        setTitle("赛事筛选");
        setTitleRightAttributes();
        btSelectAll.setEnabled(false);
        btSelectNothing.setEnabled(false);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return this.findViewById(R.id.gv_leagues);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        date = intent.getStringExtra("date");
        type = intent.getIntExtra("etype", 0);
        if (!TextUtils.isEmpty(date))
        {
            showLoading();
            requestDatas(type, date);
        }
    }

    public void setTitleRightAttributes()
    {
        TextView btnRight = titleBar.getRightMenuTextView();
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("确定");
        btnRight.setBackgroundResource(R.drawable.btn_tra_gold);
        btnRight.setGravity(Gravity.CENTER);
        int _5px = CommonUtils.dip2px(this, 5);
        int _10px = CommonUtils.dip2px(this, 10);
        btnRight.setPadding(_10px, _5px, _10px, _5px);
//        btnRight.setWidth(CommonUtils.dip2px(this, 60));
//        btnRight.setHeight(CommonUtils.dip2px(this, 25));
        btnRight.setTextColor(this.getResources().getColor(R.color.gold));
        btnRight.setTextSize(13);
        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (adapter != null)
                {
                    if (adapter.getSelectedItems() == null || adapter.getSelectedItems().isEmpty())
                    {
                        ToastUtil.show(BallQMatchLeagueSelectActivity.this, "至少选择一个赛事");
                    }
                    else
                    {
                        EventObject eventObject = new EventObject();
                        eventObject.addReceiver(BallQMatchListFragment.class);
                        eventObject.getData().putInt("etype", type);
                        eventObject.getData().putStringArrayList("league_ids", (ArrayList<String>) adapter.getSelectedItems());
                        EventObject.postEventObject(eventObject, "match_league_filter");
                        finish();
                    }
                }
                else
                {
                    ToastUtil.show(BallQMatchLeagueSelectActivity.this, "至少选择一个赛事");
                }
            }
        });
    }

    private void requestDatas(final int type, final String date)
    {
        String url = HttpUrls.HOST_URL + "/api/ares/tours/?etype=" + type + "&dt=" + date;
//        String url= HttpUrls.HOST_URL_V5+ "matches/?etype="+type+"&dt="+date;
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack()
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
                        requestDatas(type, date);
                    }
                });

            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    List<BallQMatchLeagueEntity> datas = BallQMatchLeagueEntity.getBallQMatchLeagueInfos(response);
                    if (datas != null && !datas.isEmpty())
                    {
                        hideLoad();
                        btSelectAll.setEnabled(true);
                        btSelectNothing.setEnabled(true);
                        if (leagueEntities == null)
                        {
                            leagueEntities = new ArrayList<>();
                        }
                        if (leagueEntities.size() > 0)
                        {
                            leagueEntities.clear();
                        }
                        leagueEntities.addAll(datas);
                        if (adapter == null)
                        {
                            adapter = new BallQMatchLeagueAdapter(leagueEntities);
                            gvLeagues.setAdapter(adapter);
                        }
                        else
                        {
                            adapter.notifyDataSetChanged();
                        }
                        return;
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
    protected void handleInstanceState(Bundle outState)
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

    }

    @OnClick({R.id.rb_select_nothing, R.id.rb_select_all})
    protected void onSelecteMenu(View view)
    {
        int id = view.getId();
        if (adapter != null)
        {
            if (id == R.id.rb_select_all)
            {
                btSelectAll.setChecked(true);
                btSelectNothing.setChecked(false);
                adapter.addSelectedItmes();
            }
            else if (id == R.id.rb_select_nothing)
            {
                btSelectNothing.setChecked(true);
                btSelectAll.setChecked(false);
                adapter.removeSelectedItems();
            }
        }
    }
}

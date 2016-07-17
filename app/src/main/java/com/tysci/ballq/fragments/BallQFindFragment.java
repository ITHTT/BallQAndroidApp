package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQWebViewActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQFindMenuEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQFindMenuAdapter;
import com.tysci.ballq.views.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQFindFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
    @Bind(R.id.menus_list)
    protected ListView findMenuList;

    private List<BallQFindMenuEntity> findMenuEntityList;
    private BallQFindMenuAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_find;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
       titleBar.setTitleBarTitle("发现");
        titleBar.setTitleBarLeftIcon(0, null);
        findMenuList.setOnItemClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        setRefreshing();
        requestFindMenus();
    }

    @Override
    protected View getLoadingTargetView() {
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

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 1000);
    }

//    @OnClick(R.id.layout_circle)
//    protected void onClickCircle(View view){
//        Intent intent=new Intent(baseActivity, BallQFindCircleNoteActivity.class);
//        startActivity(intent);
//    }

    private void requestFindMenus(){
        String url= HttpUrls.HOST_URL+"/api/ares/discover/";
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 5*60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(baseActivity,"请求失败");

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                        if(status==0) {
                            JSONArray datas = obj.getJSONArray("data");
                            if(datas!=null&&!datas.isEmpty()){
                                if(findMenuEntityList==null){
                                    findMenuEntityList=new ArrayList<BallQFindMenuEntity>(10);
                                }
                                if(!findMenuEntityList.isEmpty()){
                                    findMenuEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(datas,findMenuEntityList,BallQFindMenuEntity.class);
                                if(adapter==null){
                                    adapter=new BallQFindMenuAdapter(findMenuEntityList);
                                    findMenuList.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    @Override
    public void onRefresh() {
        requestFindMenus();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BallQFindMenuEntity menuEntity=findMenuEntityList.get(position);
        BallQBusinessControler.businessControler(baseActivity, menuEntity.getJump_type(), menuEntity.getJump_url());
    }

    @OnClick({R.id.layout_WhoScorec,R.id.layout_CleverGames,R.id.layout_TiTan})
    protected void onClickExtraInfo(View view){
        String url=null;
        String title=null;
        int id=view.getId();
        switch(id){
            case R.id.layout_WhoScorec:
                url="http://euro.ballq.cn/";
                title="WhoScored";
                break;
            case R.id.layout_CleverGames:
                url="http://euro.ballq.cn/Games/";
                title="Clevergames";
                break;
            case R.id.layout_TiTan:
                url="http://www.ttplus.cn/m/index.html#/home";
                title="体坛+";
                break;
        }
        if(!TextUtils.isEmpty(url)){
            Intent intent=new Intent(baseActivity, BallQWebViewActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("title",title);
            startActivity(intent);
        }
    }
}

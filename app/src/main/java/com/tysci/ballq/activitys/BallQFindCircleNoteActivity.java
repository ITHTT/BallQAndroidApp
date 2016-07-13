package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.BallQFindCircleNoteListFragment;
import com.tysci.ballq.modles.BallQCircleSectionEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/13.
 */
public class BallQFindCircleNoteActivity extends BaseActivity{
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;

    private List<BallQCircleSectionEntity> sectionEntityList=null;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_circle_note;
    }

    @Override
    protected void initViews() {
        setTitle("圈子");
        showLoading();
        requestCircleSectionList();
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.layout_content);
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void requestCircleSectionList(){
        String url= HttpUrls.CIRCLE_HOST_URL+"bbs/section/list";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                showErrorInfo(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        requestCircleSectionList();
                    }
                });

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONObject dataMapObj=obj.getJSONObject("dataMap");
                        if(dataMapObj!=null&&!dataMapObj.isEmpty()){
                            JSONArray sections=dataMapObj.getJSONArray("sections");
                            if(sections!=null&&!sections.isEmpty()){
                                hideLoad();
                                if(sectionEntityList==null){
                                    sectionEntityList=new ArrayList<BallQCircleSectionEntity>(sections.size());
                                }
                                if(!sectionEntityList.isEmpty()){
                                    sectionEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(sections,sectionEntityList,BallQCircleSectionEntity.class);
                                setCircleSectionInfo(sectionEntityList);
                                return;
                            }
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void setCircleSectionInfo(List<BallQCircleSectionEntity>datas){
        if(datas!=null&&!datas.isEmpty()){
            int size=datas.size();
            String[] titles=new String[size];
            List<BaseFragment> fragments=new ArrayList<>(size);
            for(int i=0;i<size;i++){
                BallQCircleSectionEntity info=datas.get(i);
                titles[i]=info.getName();
                BallQFindCircleNoteListFragment fragment= new BallQFindCircleNoteListFragment();
                fragment.setCircleSectionId(info.getId());
                fragments.add(fragment);
            }

            BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getSupportFragmentManager(),titles,fragments);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(fragments.size());
            tabLayout.setViewPager(viewPager);
        }

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}

package com.tysci.ballq.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.dialogs.BallQAddBarrageDialog;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SimpleTextCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 * 大战综述
 */
public class BallQGoGreatWarReViewFragment extends BaseFragment implements BallQAddBarrageDialog.OnPostBarrageListener{
    @Bind(R.id.tab_layout)
    protected PagerSlidingTabStrip tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    @Bind(R.id.sv_danmaku)
    protected IDanmakuView danmakuView;
    @Bind(R.id.time_start_end)
    protected TextView tvTimeRange;
    @Bind(R.id.profit)
    protected TextView tvProfit;

    private DanmakuContext danmakuContext;
    private int currentPages=1;
    private BallQAddBarrageDialog addBarrageDialog=null;

    private Handler danmakuHandler=new Handler(Looper.getMainLooper());
    private List<String> dankmakus=null;
    private int currentIndex=0;
    private LoadingProgressDialog loadingProgressDialog;
    private Runnable danmakuRunnable=new Runnable() {
        @Override
        public void run() {
            if (dankmakus != null) {
                int size = dankmakus.size();
                boolean isFinished = false;
                for (int i = currentIndex; i < currentIndex + 3; i++) {
                    if (i < size) {
                        addDanmaku(dankmakus.get(i), true, false);
                    } else {
                        isFinished = true;
                        break;
                    }
                }
                currentIndex += 3;
                if (isFinished) {
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //currentPages = 1;
                            getDanmakuInfos(currentPages);
                        }
                    }, 2000);
                } else {
                    danmakuHandler.postDelayed(danmakuRunnable, 1200);
                }
            }
        }
    };

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_go_great_war_review;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        addFragments();
        initDanmakuView();
        getDanmakuInfos(currentPages);
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

    private void initDanmakuView(){
        danmakuContext=DanmakuContext.create();
        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
// 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);


        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                //.setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer  设置缓存绘制填充器，默认使用{@link SimpleTextCacheStuffer}只支持纯文字显示, 如果需要图文混排请设置{@link SpannedCacheStuffer}如果需要定制其他样式请扩展{@link SimpleTextCacheStuffer}|{@link SpannedCacheStuffer}
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠
        danmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {
                //KLog.e("更新弹幕。。。");

            }

            @Override
            public void drawingFinished() {
                KLog.e("绘制完成");

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
                KLog.e("弹幕显示");
            }

            @Override
            public void prepared() {
                danmakuView.start();
            }
        });
        danmakuView.prepare(new BaseDanmakuParser() {
            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, danmakuContext);
        danmakuView.showFPS(false); //是否显示FPS
        danmakuView.enableDanmakuDrawingCache(true);
    }

    private void addFragments(){
        String[] titles={"球商GO复盘","排行榜"};
        List<BaseFragment> fragments=new ArrayList<>(titles.length);
        for(int i=0;i<titles.length;i++){
            View view= LayoutInflater.from(baseActivity).inflate(R.layout.layout_attention_tab_title, null);
            TextView title= (TextView) view.findViewById(R.id.tv_tab_title);
            title.setText(titles[i]);
            tabLayout.addTab(view);
            BaseFragment fragment;
            if(i==0){
                fragment=new BallQGoReplayFragment();
            }else{
                fragment=new BallQGoRankListFragment();
            }
            fragments.add(fragment);
        }
        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
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
        if(!TextUtils.isEmpty(action)){
            if(action.equals("ballq_go_info")){
                String start=data.getString("go_start");
                String end=data.getString("go_end");
                int stake=data.getInt("go_stake");
                tvProfit.setText("球商GO盈利:"+stake);
                Date startDate=CommonUtils.getDateAndTimeFromGMT(start);
                Date endDate=CommonUtils.getDateAndTimeFromGMT(end);
                if(startDate!=null&&endDate!=null){
                    tvTimeRange.setText(CommonUtils.getChinaDateAndTimeString(startDate)+"至"+CommonUtils.getChinaDateAndTimeString(endDate));
                }
            }
        }
    }

    private void addDanmaku(String text, boolean isLive, boolean isSelf) {
//        if(!mDanmakuView.isShown())
//        {
//             return;
//        }
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || danmakuView == null || TextUtils.isEmpty(text)) {
            return;
        }
//        danmaku.text = "这是一条弹幕" + System.nanoTime();
        danmaku.text = text;
        danmaku.padding = CommonUtils.dip2px(baseActivity,5);
        danmaku.priority = 1;  // 0可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = isLive;
        danmaku.time = danmakuView.getCurrentTime() + 1200;
        danmaku.textSize = CommonUtils.sp2px(baseActivity,13);
        if (isSelf) {
            danmaku.textColor = Color.BLACK;
        } else {
            danmaku.textColor = Color.parseColor("#3a3a3a");
        }
//        danmaku.textShadowColor = Color.WHITE;
        if (isSelf) {
            danmaku.underlineColor = Color.BLACK;
        }
//        danmaku.borderColor = Color.GREEN;
        danmakuView.addDanmaku(danmaku);
    }

    private void getDanmakuInfos(final int pages){
        String url= HttpUrls.HOST_URL_V5 + "go/comments/?p="+pages;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e("加载失败");
                if(contentView!=null) {
                    contentView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //currentPages = 1;
                            getDanmakuInfos(pages);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                // addDanmaku("获取弹幕信息成功",true,true);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        if (status == 0) {
                            JSONArray dataArrays = obj.getJSONArray("data");
                            if (dataArrays != null && !dataArrays.isEmpty()) {
                                currentPages++;
                                if (dankmakus == null) {
                                    dankmakus = new ArrayList<String>(10);
                                }
                                if (dataArrays.size() < 10) {
                                    currentPages = 1;
                                }
                                showDanmaku(dataArrays, dankmakus);
                                return;
                            }
                        }
                    }
                }
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPages = 1;
                        getDanmakuInfos(currentPages);
                    }
                },2000);
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void showDanmaku(JSONArray datas,List<String>strs){
        int size = datas.size();
        if(!strs.isEmpty()){
            strs.clear();
        }
        for (int i = 0; i < size; i++) {
            strs.add(datas.getString(i));
        }
        currentIndex=0;
        if(isResumed()) {
            danmakuHandler.postDelayed(danmakuRunnable, 1200);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
        danmakuHandler.removeCallbacks(danmakuRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (danmakuView != null && danmakuView.isPrepared()) {
                danmakuView.pause();
            }
//            mTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
        danmakuHandler.post(danmakuRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(danmakuView!=null) {
            danmakuView.release();
        }
        if(danmakuContext!=null){
            danmakuContext=null;
        }
        if(danmakuHandler!=null){
            danmakuHandler.removeCallbacks(danmakuRunnable);
        }
    }

    @OnClick(R.id.iv_write)
    protected void addBarrageDialog(View view){
        if(addBarrageDialog==null){
            addBarrageDialog=new BallQAddBarrageDialog(baseActivity);
            addBarrageDialog.setOnPostBarrageListener(this);
        }
        addBarrageDialog.show();
    }

    @Override
    public void onPostBarrage(final String content) {
        if(!UserInfoUtil.checkLogin(baseActivity)){
            UserInfoUtil.userLogin(baseActivity);
        }else{
            Map<String,String> params=new HashMap<String,String>(3);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
            params.put("content",content);
            String url=HttpUrls.HOST_URL_V5+"go/comment/add/";
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {
                    showProgressDialog("提交中...");

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
                            String message=obj.getString("message");
                            ToastUtil.show(baseActivity, message);
                            if(status==1206){
                                addBarrageDialog.dismiss();
                                addDanmaku(content, true, true);
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call) {
                    dimssProgressDialog();
                }
            });
        }
    }

    private static class BackgroundCacheStuffer extends SimpleTextCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding=10;
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setColor(0x8125309b);  //弹幕背景颜色
            canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }
}

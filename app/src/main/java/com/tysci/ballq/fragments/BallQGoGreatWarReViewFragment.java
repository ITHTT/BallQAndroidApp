package com.tysci.ballq.fragments;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SimpleTextCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by Administrator on 2016/7/15.
 * 大战综述
 */
public class BallQGoGreatWarReViewFragment extends BaseFragment{
    @Bind(R.id.tab_layout)
    protected PagerSlidingTabStrip tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    @Bind(R.id.sv_danmaku)
    protected IDanmakuView danmakuView;

    private DanmakuContext danmakuContext;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_go_great_war_review;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        addFragments();

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

            }

            @Override
            public void drawingFinished() {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

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

package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQCircleNoteEntity;
import com.tysci.ballq.modles.BallQCircleUserCommentEntity;
import com.tysci.ballq.modles.BallQNoteContentEntity;
import com.tysci.ballq.modles.BallQUserAchievementEntity;
import com.tysci.ballq.modles.BallQUserEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQCircleNoteCommentAdapter;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/25.
 */
public class BallQCircleNoteDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private LinearLayout.LayoutParams layoutContentParams;
    private int imageWidth;

    private List<BallQCircleUserCommentEntity> commentEntityList;
    private BallQCircleNoteCommentAdapter adapter=null;

    private View headerView;
    private int noteId;
    private int sortType = 1;
    private int onlyAuthor = 0;
    private String loadFinishedTip="没有更多数据了";
    private int currentPages=1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_circle_note_detail;
    }

    @Override
    protected void initViews() {
        setTitle("帖子详情");
        setRightMenuAttrs();
        initContentLayoutParams();
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headerView= LayoutInflater.from(this).inflate(R.layout.layout_ballq_circle_note_header,null);
        recyclerView.addHeaderView(headerView);
    }

    private void setRightMenuAttrs(){
        ImageView ivRightMenu=titleBar.getRightMenuImageView();
        ivRightMenu.getLayoutParams().height= CommonUtils.dip2px(this,15);
        ivRightMenu.getLayoutParams().width=LinearLayout.LayoutParams.WRAP_CONTENT;
        ivRightMenu.setPadding(0, 0, CommonUtils.dip2px(this, 10), 0);
        ivRightMenu.setImageResource(R.mipmap.icon_titlebar_right_menu);
        ivRightMenu.setScaleType(ImageView.ScaleType.CENTER);
        ivRightMenu.setOnClickListener(this);
    }

    private void initContentLayoutParams(){
        layoutContentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutContentParams.setMargins(0, 10, 0, 0);
        imageWidth= CommonUtils.getScreenDisplayMetrics(this).widthPixels;
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    @Override
    protected void getIntentData(Intent intent) {
        noteId=intent.getIntExtra(Tag,-1);
        if(noteId>=0){
            requestCircleDetailInfos(noteId);
        }
    }

    private void requestCircleDetailInfos(int id){
        String url= HttpUrls.CIRCLE_HOST_URL+"bbs/topic/view/"+id;
        Map<String,String> params=null;
        if(UserInfoUtil.checkLogin(this)){
            params=new HashMap<>(1);
            params.put("userId", UserInfoUtil.getUserId(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag,url,params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&!obj.isEmpty()){
                        JSONObject dataMap=obj.getJSONObject("dataMap");
                        if(dataMap!=null&&!dataMap.isEmpty()){
                            BallQCircleNoteEntity noteEntity=dataMap.getObject("topic",BallQCircleNoteEntity.class);
                            if(noteEntity!=null){
                                initBallQCircleNoteDetailInfos(headerView,noteEntity);
                                requestCircleNoteCommentInfos(noteId,sortType,onlyAuthor,currentPages,false);
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void initBallQCircleNoteDetailInfos(View headerView,BallQCircleNoteEntity info){
        if(commentEntityList==null){
            commentEntityList=new ArrayList<>(10);
            adapter=new BallQCircleNoteCommentAdapter(commentEntityList);
            recyclerView.setAdapter(adapter);
        }

        CircleImageView ivUserHeader= (CircleImageView) headerView.findViewById(R.id.ivUserIcon);
        ImageView isV=(ImageView) headerView.findViewById(R.id.isV);
        TextView tvUserName=(TextView)headerView.findViewById(R.id.tv_author_name);
        ImageView ivAchievement01=(ImageView)headerView.findViewById(R.id.iv_user_achievement01);
        ImageView ivAchievement02=(ImageView)headerView.findViewById(R.id.iv_user_achievement02);
        TextView tvTop=(TextView)headerView.findViewById(R.id.tv_top);
        TextView tvCreated=(TextView)headerView.findViewById(R.id.tv_create_time);
        TextView tvTitle=(TextView)headerView.findViewById(R.id.tv_title);
        TextView tvReadNum=(TextView)headerView.findViewById(R.id.tv_read_num);
        LinearLayout layoutContent=(LinearLayout)headerView.findViewById(R.id.layout_content);
        final BallQUserEntity auther=info.getCreater();
        if(auther!=null){
            GlideImageLoader.loadImage(this,auther.getPortrait(), R.mipmap.icon_user_default,ivUserHeader);
            ivUserHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoUtil.lookUserInfo(BallQCircleNoteDetailActivity.this,auther.getUserId());
                }
            });
            UserInfoUtil.setUserHeaderVMark(auther.getIsV(),isV,ivUserHeader);
            List<BallQUserAchievementEntity>achievementEntityList=auther.getAchievements();
            if(achievementEntityList!=null){
                int size=achievementEntityList.size();
                for(int i=0;i<size;i++){
                    if(i==0){
                        ivAchievement01.setVisibility(View.VISIBLE);
                        GlideImageLoader.loadImage(this,achievementEntityList.get(i).getLogo(), R.mipmap.icon_user_achievement_circle_mark,ivAchievement01);
                    }else if(i==1){
                        ivAchievement02.setVisibility(View.VISIBLE);
                        GlideImageLoader.loadImage(this,achievementEntityList.get(i).getLogo(), R.mipmap.icon_user_achievement_circle_mark,ivAchievement02);
                    }
                }
            }
            tvUserName.setText(auther.getFirstName());
            if(UserInfoUtil.checkLogin(this)){
                if(String.valueOf(auther.getUserId()).equals(UserInfoUtil.getUserId(this))){
                    onlyAuthor=1;
                }
            }
        }
        if(info.getTop()==1){
            tvTop.setVisibility(View.VISIBLE);
            tvCreated.setVisibility(View.INVISIBLE);
        }else{
            tvTop.setVisibility(View.GONE);
            tvCreated.setVisibility(View.VISIBLE);
            tvCreated.setText(CommonUtils.getDateAndTimeFormatString(info.getCreateTime()));
        }
        tvTitle.setText(info.getTitle());
        tvReadNum.setText(String.valueOf(info.getViewCount()));
        initContentType(info.getContents(),layoutContent);
    }

    private void initContentType(List<BallQNoteContentEntity> contents, LinearLayout layoutContent) {
        BallQNoteContentEntity content;
        ArrayList<BallQNoteContentEntity> imgUrls = null;
        View.OnClickListener imgClickListener = null;
        int imgCounts = 0;
        //KLog.e("size:" + contents.size());
        int size = contents.size();
        for (int i = 0; i < size; i++) {
            content = contents.get(i);
            switch (content.getType()) {
                case 0:
                case 1:
                    if (imgUrls == null) {
                        imgUrls = new ArrayList<>(9);
                    }
                    if (imgClickListener == null) {
                        //imgClickListener = getImageClickListener(imgUrls);
                    }
                    imgUrls.add(content);
                    initContentImg(layoutContent, content, imgCounts, imgClickListener);
                    imgCounts++;
                    break;
                case 2:
                    // TODO: 16/3/23
                    break;
                case 3:
                    // TODO: 16/3/23
                    break;
                case 4:
                    initContentText(layoutContent, content);
                    break;
            }
        }
    }

    private void initContentText(LinearLayout layout_content, BallQNoteContentEntity content) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(layoutContentParams);
        tv.setText(content.getContent());
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextSize(14f);
        tv.setTextColor(getResources().getColor(R.color.c_3a3a3a));
        layout_content.addView(tv);
    }

    private void initContentImg(LinearLayout layoutContent, final BallQNoteContentEntity content, int index, View.OnClickListener listener) {
        final ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams layoutImageParams = new LinearLayout.LayoutParams(imageWidth, imageWidth * content.getHeight() / content.getWidth());
        layoutImageParams.setMargins(0, 20, 0, 0);
        iv.setLayoutParams(layoutImageParams);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setTag(R.id.tag, index);
        iv.setOnClickListener(listener);
        GlideImageLoader.loadImage(this,content.getContent(), R.color.default_circle_img,iv);
        layoutContent.addView(iv);
    }

    private void requestCircleNoteCommentInfos(int id, int sortType, int onlyAuthor, int pages, final boolean isLoadMore){
        String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/" + id
                + "/comments?sortType=" + sortType
                + "&onlyAuthor=" + onlyAuthor
                + "&pageNo=" + pages
                + "&pageSize=10";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag,url,60,new HttpClientUtil.StringResponseCallBack(){

            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONObject dataMap=obj.getJSONObject("dataMap");
                        if(dataMap!=null&&!dataMap.isEmpty()){
                            JSONArray arrays=dataMap.getJSONArray("comments");
                            if(arrays!=null&&!arrays.isEmpty()){
                                if(!isLoadMore&&!commentEntityList.isEmpty()){
                                    commentEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(arrays,commentEntityList,BallQCircleUserCommentEntity.class);
                                adapter.notifyDataSetChanged();
                                if(arrays.size()<10){
                                    recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                                }else{
                                    recyclerView.setStartLoadMore();
                                    if(isLoadMore){
                                        currentPages++;
                                    }else{
                                        currentPages=2;
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
                if(isLoadMore){
                    recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
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

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}

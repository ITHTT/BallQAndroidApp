package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.fragments.BallQFindCircleNoteListFragment;
import com.tysci.ballq.modles.BallQCircleNoteEntity;
import com.tysci.ballq.modles.BallQCircleUserCommentEntity;
import com.tysci.ballq.modles.BallQNoteContentEntity;
import com.tysci.ballq.modles.BallQUserAchievementEntity;
import com.tysci.ballq.modles.BallQUserEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SoftInputUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQCircleNoteCommentAdapter;
import com.tysci.ballq.views.dialogs.BallQCircleNoteMenu;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.dialogs.ShareDialog;
import com.tysci.ballq.views.interfaces.OnLongClickUserHeaderListener;
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
public class BallQCircleNoteDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener
,OnLongClickUserHeaderListener {
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.et_comment)
    protected EditText etComment;
    @Bind(R.id.ivLike)
    protected ImageView ivLike;
    @Bind(R.id.iv_share)
    protected ImageView ivShare;
    @Bind(R.id.btnPublish)
    protected Button btPublish;
    private ImageView menuDelete;
    private ImageView menuCollect;

    private LinearLayout.LayoutParams layoutContentParams;
    private int imageWidth;

    private BallQCircleNoteMenu menuView;

    private List<BallQCircleUserCommentEntity> commentEntityList;
    private BallQCircleNoteCommentAdapter adapter=null;
    private BallQCircleNoteEntity circleNoteEntity=null;

    private View headerView;
    private int noteId;
    private int sortType = 1;
    private int onlyAuthor = 0;
    private int isCollected = 0;
    private String loadFinishedTip="没有更多数据了";
    private int currentPages=1;
    private String replyerName;
    private String replyerId;
    private String cacheCommentInfo="";
    private String fid = null;
    private ShareDialog shareDialog=null;
    private LoadingProgressDialog loadingProgressDialog=null;

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
        ivLike.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        btPublish.setOnClickListener(this);

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivLike.setVisibility(View.GONE);
                    ivShare.setVisibility(View.GONE);
                    btPublish.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(cacheCommentInfo)) {
                        etComment.setText(cacheCommentInfo);
                        etComment.setSelection(cacheCommentInfo.length());
                    }
                } else {
                    etComment.setText("");
                    ivLike.setVisibility(View.VISIBLE);
                    ivShare.setVisibility(View.VISIBLE);
                    btPublish.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    SoftInputUtil.hideSoftInput(BallQCircleNoteDetailActivity.this);
                    cacheCommentInfo = etComment.getText().toString();
                    replyerId = null;
                    replyerName = null;
                    etComment.clearFocus();
                    etComment.setHint("发表评论");
                    etComment.setText("");
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void setRightMenuAttrs(){
        ImageView ivRightMenu=titleBar.getRightMenuImageView();
        ivRightMenu.getLayoutParams().height= CommonUtils.dip2px(this,10);
        ivRightMenu.getLayoutParams().width=LinearLayout.LayoutParams.WRAP_CONTENT;
        ivRightMenu.setPadding(0, 0, CommonUtils.dip2px(this, 8), 0);
        ivRightMenu.setImageResource(R.mipmap.icon_titlebar_right_menu);
        ivRightMenu.setScaleType(ImageView.ScaleType.CENTER);
        titleBar.setRightOnClickListener(this);
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
            showLoading();
            requestCircleDetailInfos(noteId);
            getCircleUserInfo(noteId);
        }
    }

    private void setRefreshing(){
        if(swipeRefresh!=null){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
    }

    private void onRefreshCompelete() {
        if (swipeRefresh != null) {
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

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(this);
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

    private void requestCircleDetailInfos(final int id){
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
                onRefreshCompelete();
                if(adapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestCircleDetailInfos(id);
                        }
                    });
                }else{
                    recyclerView.setStartLoadMore();
                    ToastUtil.show(BallQCircleNoteDetailActivity.this,"请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&!obj.isEmpty()){
                        JSONObject dataMap=obj.getJSONObject("dataMap");
                        if(dataMap!=null&&!dataMap.isEmpty()){
                            hideLoad();
                            BallQCircleNoteEntity noteEntity=dataMap.getObject("topic",BallQCircleNoteEntity.class);
                            if(noteEntity!=null){
                                circleNoteEntity=noteEntity;
                                initBallQCircleNoteDetailInfos(headerView,noteEntity);
                                requestCircleNoteCommentInfos(noteId,sortType,onlyAuthor,1,false,0);
                                return;
                            }
                        }
                    }
                }
                onRefreshCompelete();
                showEmptyInfo();
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
            adapter.setOnLongClickUserHeaderListener(this);
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
        if(layoutContent.getChildCount()>0){
            layoutContent.removeAllViews();
        }
        initContentType(info.getContents(), layoutContent);
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
                        imgClickListener = getImageClickListener(imgUrls);
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

    private View.OnClickListener getImageClickListener(final ArrayList<BallQNoteContentEntity> imgUrls) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.tag);
                Intent intent = new Intent(BallQCircleNoteDetailActivity.this, BallQImageBrowseActivity.class);
                intent.putExtra("index", index);
                intent.putParcelableArrayListExtra("images", imgUrls);
                startActivity(intent);
            }
        };
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
        GlideImageLoader.loadImage(this, content.getContent(), R.color.default_circle_img, iv);
        layoutContent.addView(iv);
    }

    private void requestCircleNoteCommentInfos(final int id, final int sortType, final int onlyAuthor, final int pages, final boolean isLoadMore,final int scrollPosition){
        String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/" + id
                + "/comments?sortType=" + sortType
                + "&onlyAuthor=" + onlyAuthor
                + "&pageNo=" + pages
                + "&pageSize=10";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {

            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e("error:" + error.getMessage());
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                    if (commentEntityList == null || commentEntityList.isEmpty()) {
                        recyclerView.setLoadMoreDataFailed(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setStartLoadMore();
                                requestCircleNoteCommentInfos(id, sortType, onlyAuthor, pages, isLoadMore, scrollPosition);
                            }
                        });
                    } else {
                        ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");
                    }
                } else {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                }
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        JSONObject dataMap = obj.getJSONObject("dataMap");
                        if (dataMap != null && !dataMap.isEmpty()) {
                            JSONArray arrays = dataMap.getJSONArray("comments");
                            if (arrays != null && !arrays.isEmpty()) {
                                if (!isLoadMore && !commentEntityList.isEmpty()) {
                                    commentEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(arrays, commentEntityList, BallQCircleUserCommentEntity.class);
                                adapter.notifyDataSetChanged();
                                if (arrays.size() < 10) {
                                    recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                                } else {
                                    recyclerView.setStartLoadMore();
                                    if (isLoadMore) {
                                        currentPages++;
                                    } else {
                                        currentPages = 2;
                                    }
                                }
                                if (scrollPosition > 0) {
                                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                    linearLayoutManager.scrollToPositionWithOffset(scrollPosition, 0);
                                }
                                return;
                            }
                        }
                    }
                }
                if (isLoadMore) {
                    if (!commentEntityList.isEmpty()) {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    } else {
                        recyclerView.setLoadMoreDataComplete();
                    }
                } else {
                    if (commentEntityList.isEmpty()) {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
                    onRefreshCompelete();
                }
            }
        });
    }

    private void getCircleUserInfo(int id){
        if(UserInfoUtil.checkLogin(this)){
            checkUserLike(id);
            checkUserCollect(id);
        }
    }

    private void checkUserLike(int id){
        String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/islike/" + id;
        HashMap<String, String> params = new HashMap<>(1);
        params.put("userId", UserInfoUtil.getUserId(this));
        KLog.e("userId:" + UserInfoUtil.getUserId(this));

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                //ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int statusCode = obj.getIntValue("statusCode");
                        if (statusCode == 200) {
                            JSONObject dataMap = obj.getJSONObject("dataMap");
                            if (dataMap != null && !dataMap.isEmpty()) {
                                ivLike.setSelected(dataMap.getIntValue("isLike") == 1);
                                ivLike.setTag("isLike");
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

    private void checkUserCollect(int id){
        KLog.e("检查是否收藏...");
        String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/iscollect/" + id;
        HashMap<String, String> params = new HashMap<>(1);
        params.put("userId", UserInfoUtil.getUserId(this));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int statusCode = obj.getIntValue("statusCode");
                        if (statusCode == 200) {
                            JSONObject dataMap = obj.getJSONObject("dataMap");
                            if (dataMap != null && !dataMap.isEmpty()) {
                                isCollected = dataMap.getIntValue("isCollect");
                                fid = dataMap.getString("fid");
                                if (menuCollect != null) {
                                    menuCollect.setSelected(isCollected == 1);
                                }
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

    private void userCollect(int id){
        if(!UserInfoUtil.checkLogin(this)){
            UserInfoUtil.userLogin(this);
        }else {
            String url = HttpUrls.HOST_URL_V5 + "user/favorites/add/?etype=2&eid=" + id;
            String delUrl = HttpUrls.HOST_URL_V5 + "user/favorites/del/?etype=2&fid=" + fid;
            HashMap<String, String> params = new HashMap<>();
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));

            if (isCollected == 1 && !TextUtils.isEmpty(fid)) {
                url = delUrl;
            }
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {

                }
                @Override
                public void onError(Call call, Exception error) {
                    ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty()) {
                            int status = obj.getIntValue("status");
                            if (status == 0) {
                                if (isCollected == 1) {
                                    isCollected = 0;
                                    menuCollect.setSelected(false);
                                    ToastUtil.show(BallQCircleNoteDetailActivity.this, "取消收藏成功");
                                } else {
                                    fid = obj.getString("data");
                                    isCollected = 1;
                                    menuCollect.setSelected(true);
                                    ToastUtil.show(BallQCircleNoteDetailActivity.this, "收藏成功");
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
    }

    private void userLike(int id){
        if(!UserInfoUtil.checkLogin(this)){
            UserInfoUtil.userLogin(this);
        }else{
            String url = HttpUrls.CIRCLE_HOST_URL+ "bbs/topic/like/" + id;
            final HashMap<String, String> params = new HashMap<>();
            params.put("userId",UserInfoUtil.getUserId(this));
            params.put("op", ivLike.isSelected() ? "0" : "1");
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {
                    ivLike.setEnabled(false);
                }

                @Override
                public void onError(Call call, Exception error) {
                    ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject obj=JSONObject.parseObject(response);
                        if(obj!=null&&!obj.isEmpty()){
                            int statusCode=obj.getIntValue("statusCode");
                            boolean isLike=ivLike.isSelected();
                            if(isLike){
                                ivLike.setSelected(false);
                                ToastUtil.show(BallQCircleNoteDetailActivity.this,"取消点赞成功");
                            }else{
                                ivLike.setSelected(true);
                                ToastUtil.show(BallQCircleNoteDetailActivity.this,"点赞成功");
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call) {
                    ivLike.setEnabled(true);
                }
            });
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
        int id=view.getId();
        switch(id){
            case R.id.layout_titlebar_next:
                showMenuView(view);
                break;
            case R.id.ivLike:
                userLike(noteId);
                break;
            case R.id.iv_share:
                weChatShare(circleNoteEntity);
                break;
            case R.id.btnPublish:
                userComment();
                break;
        }
        onMenuItemClicked(view);
    }

    private void onMenuItemClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.drawable.ballq_circle_share_selector:
                weChatShare(circleNoteEntity);
                menuView.dismiss();
                break;
            case R.drawable.ballq_circle_collection_selector:
                userCollect(noteId);
                menuView.dismiss();
                break;
            case R.drawable.ballq_circle_reward_selector:
                userReward(circleNoteEntity);
                menuView.dismiss();
                break;
            case R.drawable.ballq_circle_sort_selector:
                sortCircleComments(v);
                menuView.dismiss();
                break;
            case R.drawable.ballq_circle_first_selector:
                searchAuthorComments(v);
                menuView.dismiss();
                break;
            case R.drawable.ballq_circle_delete_selector:
                deleteCircleNote(noteId);
                menuView.dismiss();
                break;
        }
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onLoadMore() {
        if (recyclerView.isRefreshing()) {
            //KLog.e("刷新数据中....");
            recyclerView.setRefreshingTip("刷新数据中...");
        } else {
            // KLog.e("currentPage:" + currentPages);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestCircleNoteCommentInfos(noteId, sortType, onlyAuthor, currentPages, true, 0);
                }
            }, 300);
        }
    }

    @Override
    public void onRefresh() {
        if (recyclerView.isLoadMoreing()) {
            onRefreshCompelete();
        } else {
            recyclerView.setRefreshing();
            requestCircleDetailInfos(noteId);
        }
    }

    private void showMenuView(View view) {
        recyclerView.stopScroll();
        if (circleNoteEntity != null) {
            if (menuView == null) {
                menuView = new BallQCircleNoteMenu(this);
                menuView.addItem(R.drawable.ballq_circle_first_selector, BallQCircleNoteDetailActivity.this);
                menuView.addItem(R.drawable.ballq_circle_sort_selector, BallQCircleNoteDetailActivity.this);
                int type = getMenuType();
                menuDelete = (ImageView) menuView.addItem(type, BallQCircleNoteDetailActivity.this);
                menuCollect = (ImageView) menuView.addItem(R.drawable.ballq_circle_collection_selector, BallQCircleNoteDetailActivity.this);
                /**表明已获取是否已收藏的状态,但没有更新视图显示*/
                menuCollect.setSelected(isCollected==1);
                menuView.addItem(R.drawable.ballq_circle_share_selector, BallQCircleNoteDetailActivity.this);
            } else {
                int type = getMenuType();
                if (menuDelete != null) {
                    menuDelete.setBackgroundResource(type);
                    menuDelete.setId(type);
                }
            }
            menuView.onShow(view, 0, titleBar.getBottom() - view.getBottom());
        }
    }

    private int getMenuType() {
        int type = R.drawable.ballq_circle_reward_selector;
        if (UserInfoUtil.checkLogin(this)) {
            String userId = UserInfoUtil.getUserId(this);
            KLog.e("UserId:" + userId);
            BallQUserEntity user = circleNoteEntity.getCreater();
            if (user != null) {
                String id = String.valueOf(user.getUserId());
                if (userId.equals(id)) {
                    KLog.e("用户自己的帖子");
                    type = R.drawable.ballq_circle_delete_selector;
                }
                KLog.e("userid:" + id);
            }
        }
        return type;
    }

    private void sortCircleComments(View view){
        ImageView item= (ImageView) view;
        if(item.isSelected()){
            item.setSelected(false);
            sortType=1;
        }else{
            item.setSelected(true);
            sortType=0;
        }
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        if(adapter!=null){
            if(commentEntityList!=null&&!commentEntityList.isEmpty()){
                commentEntityList.clear();
                adapter.notifyDataSetChanged();
            }
        }
        setRefreshing();
        requestCircleNoteCommentInfos(noteId,sortType,onlyAuthor,1,false,1);
    }

    private void searchAuthorComments(View view){
        ImageView item= (ImageView) view;
        if(item.isSelected()){
            item.setSelected(false);
            onlyAuthor=0;
        }else{
            item.setSelected(true);
            onlyAuthor=1;
        }
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        if(adapter!=null){
            if(commentEntityList!=null&&!commentEntityList.isEmpty()) {
                commentEntityList.clear();
                adapter.notifyDataSetChanged();
            }
        }
        setRefreshing();
        requestCircleNoteCommentInfos(noteId,sortType,onlyAuthor,1,false,1);
    }

    private void weChatShare(BallQCircleNoteEntity info){
        if(info!=null&&!TextUtils.isEmpty(info.getShareUrl())){
            if (shareDialog == null) {
                shareDialog = new ShareDialog(this);
                String title = null;
                if (TextUtils.isEmpty(info.getTitle())) {
                    title = info.getCreater().getFirstName() + "在球商APP上发的帖子";
                } else {
                    title = info.getTitle();
                }
                String describ = info.getSummaryText();
                shareDialog.setShareType("0")
                        .setShareTitle(title)
                        .setShareExcerpt(describ)
                        .setShareUrl(info.getShareUrl());
            }
            shareDialog.show();
        }
    }

    private void userReward(BallQCircleNoteEntity info){
        if(info!=null) {
            if (UserInfoUtil.checkLogin(this)) {
                UserRewardActivity.userReward(this, "topic", String.valueOf(info.getCreater().getUserId()), info.getId(), info.getCreater().getPortrait(), info.getCreater().getIsV());
            }else{
                UserInfoUtil.userLogin(this);
            }
        }
    }

    private void deleteCircleNote(int id){
        String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/delete/" + id;
        HashMap<String, String> params = new HashMap<>(1);
        params.put("userId",UserInfoUtil.getUserId(this));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                showProgressDialog("请求中...");
            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                dimssProgressDialog();
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("statusCode");
                        if(status==200){
                            ToastUtil.show(BallQCircleNoteDetailActivity.this, "删除成功");
                            EventObject object=new EventObject();
                            object.addReceiver(BallQFindCircleNoteListFragment.class);
                            object.getData().putInt("sectionId", circleNoteEntity.getSectionId());
                            EventObject.postEventObject(object,"delete_circle_note");
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    @Override
    public void onLongClickUserHead(View v, int position) {
        BallQCircleUserCommentEntity info=commentEntityList.get(position);
        if(info!=null){
            etComment.requestFocus();
            SoftInputUtil.showSoftInput(this, etComment);
            replyerName = "@" + info.getCreator().getFirstName().trim() + ":";
            replyerId = String.valueOf(info.getCreator().getUserId());
            etComment.setHint(replyerName);
        }
    }

    private void userComment(){
        SoftInputUtil.hideSoftInput(this);
        if(!UserInfoUtil.checkLogin(this)){
            UserInfoUtil.userLogin(this);
        }else {
            String comment_text = etComment.getText().toString().trim();
            if (TextUtils.isEmpty(comment_text)) {
                ToastUtil.show(this, "请输入评论内容");
                return;
            }
            String url = HttpUrls.CIRCLE_HOST_URL + "bbs/topic/" + noteId
                    + "/comment/add/";

            KLog.e("sendComment url = " + url);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", UserInfoUtil.getUserId(this));
            params.put("topicId", String.valueOf(noteId));
            KLog.e("repliedId:" + replyerId);
            if (!TextUtils.isEmpty(replyerId)) {
                params.put("repliedId", replyerId);
                if (!comment_text.contains(replyerName)) {
                    comment_text = replyerName + comment_text;
                }
            }
            KLog.e("comment_text:" + comment_text);
            params.put("content", comment_text);

            HttpClientUtil.getHttpClientUtil().uploadFiles(Tag, url, null, params, new HttpClientUtil.ProgressResponseCallBack() {
                @Override
                public void onBefore(Request request) {
                    showProgressDialog("请求中...");

                }
                @Override
                public void onError(Call call, Exception error) {
                    ToastUtil.show(BallQCircleNoteDetailActivity.this, "请求失败");

                }
                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty()) {
                            int statusCode = obj.getIntValue("statusCode");
                            if (statusCode == 200) {
                                etComment.setHint("发表评论");
                                etComment.clearFocus();
                                etComment.setText("");
                                cacheCommentInfo="";
                                ToastUtil.show(BallQCircleNoteDetailActivity.this, "评论成功");
                                setRefreshing();
                                requestCircleNoteCommentInfos(noteId, sortType, onlyAuthor, 1, false, 1);
                            }
                        }
                    }
                }
                @Override
                public void onFinish(Call call) {
                    dimssProgressDialog();
                }

                @Override
                public void loadingProgress(int progress) {

                }
            });
        }

    }


}

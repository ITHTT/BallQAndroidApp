package com.tysci.ballq.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQCircleSectionEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQAddImageAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BallQPublishCircleNoteActivity extends BaseActivity implements BallQAddImageAdapter.OnDeletePictureListener
,SlidingTabLayout.OnTabSelectListener{
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.et_bbs_title)
    protected EditText etTitle;
    @Bind(R.id.et_bbs_content)
    protected EditText etContent;
    @Bind(R.id.rv_pictures)
    protected RecyclerView rvImages;
    @Bind(R.id.tv_tip)
    protected TextView tvTip;

    private final int MAX_PICTURES=9;

    private List<BallQCircleSectionEntity> circleSectionEntityList=null;
    private int currentItem=0;
    private int sectionId;

    private List<String> imgUrls;
    private BallQAddImageAdapter adapter=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_publish_circle_note;
    }

    @Override
    protected void initViews() {
        setTitle("发表帖子");
        setTitleRightAttributes();
        tabLayout.setOnTabSelectListener(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(linearLayoutManager);

        imgUrls=new ArrayList<>(10);
        imgUrls.add("drawable://" + R.mipmap.icon_add_pictures);
        adapter=new BallQAddImageAdapter(imgUrls);
        adapter.setOnDeletePictureListener(this);
        rvImages.setAdapter(adapter);
    }

    public void setTitleRightAttributes(){
        TextView btnRight=titleBar.getRightMenuTextView();
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("发布");
        btnRight.setBackgroundResource(R.drawable.bt_ok_select_bg);
        btnRight.setGravity(Gravity.CENTER);
        btnRight.setWidth(CommonUtils.dip2px(this, 60));
        btnRight.setHeight(CommonUtils.dip2px(this, 30));
        btnRight.setTextColor(this.getResources().getColor(R.color.gold));
        btnRight.setTextSize(14);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestBetting();
                publishCircleNote();

            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        currentItem=intent.getIntExtra("index", 0);
        circleSectionEntityList=intent.getParcelableArrayListExtra("circle_sections");
        if(circleSectionEntityList!=null){
            sectionId=circleSectionEntityList.get(currentItem).getId();
            setTabDatas(circleSectionEntityList);
            tabLayout.setCurrentTab(currentItem);
        }

    }

    private void setTabDatas(List<BallQCircleSectionEntity>datas){
        if(datas!=null&&!datas.isEmpty()){
            int size=datas.size();
            String[] titles=new String[size];
            for(int i=0;i<size;i++){
                titles[i]=datas.get(i).getName();
            }
            tabLayout.setTabDatas(titles);
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

    @Override
    public void onDeletePicture(int counts) {
        tvTip.setText("已选"+(counts)+"张,还可以添加"+(MAX_PICTURES-counts)+"张");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x0001){
            if(resultCode==RESULT_OK){
                if(data!=null){
                    ArrayList<String> images = data.getStringArrayListExtra("selected_photos");
                    if(images!=null&&images.size()>0){
                        int size=imgUrls.size();
                        imgUrls.addAll(size-1,images);
                        adapter.notifyDataSetChanged();
                        tvTip.setText("已选"+(imgUrls.size()-1)+"张,还可以添加"+(MAX_PICTURES-imgUrls.size()+1)+"张");
                    }
                }
            }
        }
    }

    private void publishCircleNote(){
        String title=etTitle.getText().toString();
        String content=etContent.getText().toString();
        if(TextUtils.isEmpty(title)&&TextUtils.isEmpty(content)&&imgUrls.size()<=1){
            ToastUtil.show(this,"请输入帖子内容");
            etTitle.requestFocus();
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        params.put("userId", UserInfoUtil.getUserId(this));
        params.put("sectionId", String.valueOf(sectionId));

        String url= HttpUrls.CIRCLE_HOST_URL+"bbs/topic/publish";

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("提交中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        HttpClientUtil.getHttpClientUtil().uploadImages(Tag, url, null, params, imgUrls, new HttpClientUtil.ProgressResponseCallBack() {
            @Override
            public void loadingProgress(int progress) {
                KLog.e("progress:"+progress);
            }

            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e("error:"+error.getMessage());
                dialog.dismiss();

            }

            @Override
            public void onSuccess(Call call, String response) {
                dialog.dismiss();
                KLog.e(response);
            }

            @Override
            public void onFinish(Call call) {


            }
        });

    }

    @Override
    public void onTabSelect(int position) {
        sectionId=circleSectionEntityList.get(position).getId();
    }

    @Override
    public void onTabReselect(int position) {

    }
}

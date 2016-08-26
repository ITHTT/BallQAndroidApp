package com.tysci.ballq.views.widgets.multiphotopicker.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.Photo;
import com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters.PhotoPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/4/16.
 */
public class PhotoPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private ViewPager viewPager;
    private ImageView ivPhotoSelectedMark;

    private int photoMaxSelectCounts=PhotoPickerActivity.DEFAULT_MAX_SELECTED_PHOTOS;

    private int currentPosition=0;
    private List<Photo> photos=null;
    private List<String>selectedPhotos=null;
    private PhotoPagerItemAdapter photoPagerItemAdapter=null;
    private TextView btnRight;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo_pager;
    }

    @Override
    protected void initViews() {
        btnRight=titleBar.getRightMenuTextView();
        viewPager=(ViewPager)this.findViewById(com.tysci.ballq.R.id.photo_view_pager);
        viewPager.addOnPageChangeListener(this);
        ivPhotoSelectedMark=(ImageView)this.findViewById(R.id.iv_photo_select_mark);
        ivPhotoSelectedMark.setOnClickListener(this);
        getDatas();
        addTitleBarRightItem();
        photoPagerItemAdapter=new PhotoPagerItemAdapter(this,photos);
        viewPager.setAdapter(photoPagerItemAdapter);
        viewPager.setCurrentItem(currentPosition);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState) {

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

    private void addTitleBarRightItem(){
        int paddingTop= CommonUtils.dip2px(this, 6);
        int paddingLeft= CommonUtils.dip2px(this, 10);
        btnRight.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
        btnRight.setTextColor(Color.parseColor("#ffffff"));
        btnRight.setTextSize(12);
        btnRight.setVisibility(View.VISIBLE);
        int size=0;
        if(selectedPhotos!=null){
            size=selectedPhotos.size();
        }
        btnRight.setText("完成("+size+"/" + photoMaxSelectCounts + ")");
        btnRight.setBackgroundResource(R.drawable.bt_image_select);
        btnRight.setGravity(Gravity.CENTER);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putStringArrayListExtra("selected_photos", (ArrayList<String>) selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getDatas(){
        Intent intent=this.getIntent();
        currentPosition=intent.getIntExtra("position",0);
        photos=intent.getParcelableArrayListExtra("photos");
        selectedPhotos=intent.getStringArrayListExtra("selected_photos");
        photoMaxSelectCounts=intent.getIntExtra("max_select_photos",PhotoPickerActivity.DEFAULT_MAX_SELECTED_PHOTOS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_photo_select_mark:
                onSelectPhoto();
                break;
        }
    }

    protected void back() {
        Intent intent=new Intent();
        intent.putStringArrayListExtra("selected_photos", (ArrayList<String>) selectedPhotos);
        setResult(RESULT_FIRST_USER, intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    private void onSelectPhoto(){
        Photo photo=photos.get(currentPosition);
        boolean isSelected=isSelectedPhoto(photo);
        if(isSelected){
            selectedPhotos.remove(photo.getPath());
            ivPhotoSelectedMark.setImageResource(R.mipmap.icon_photo_unselected);
        }else{
            if(selectedPhotos.size()<photoMaxSelectCounts) {
                selectedPhotos.add(photo.getPath());
                ivPhotoSelectedMark.setImageResource(R.mipmap.icon_photo_selected);
            }else{
                ToastUtil.show(this, "您最多可选择" + photoMaxSelectCounts + "张图片");
            }
        }
        btnRight.setText("完成(" + selectedPhotos.size() + "/" + photoMaxSelectCounts + ")");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition=position;
        setTitle((currentPosition + 1) + "/" + photos.size());
        Photo photo=photos.get(position);
        if(isSelectedPhoto(photo)){
            ivPhotoSelectedMark.setImageResource(R.mipmap.icon_photo_selected);
        }else{
            ivPhotoSelectedMark.setImageResource(R.mipmap.icon_photo_unselected);
        }
    }

    /**
     * 判断当前图片是否已经选择
     * @param photo
     * @return
     */
    private boolean isSelectedPhoto(Photo photo){
        int size=selectedPhotos.size();
        for(int i=0;i<size;i++){
            if(selectedPhotos.get(i).equals(photo.getPath())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

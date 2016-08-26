package com.tysci.ballq.views.widgets.multiphotopicker.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.PhotoDirectory;
import com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters.PhotoDirectoryItemAdapter;
import com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters.PhotoItemAdapter;
import com.tysci.ballq.views.widgets.multiphotopicker.utils.ImageMediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/4/12.
 */
public class PhotoPickerActivity extends BaseActivity implements PhotoDirectoryItemAdapter.OnItemClickListener
,PhotoItemAdapter.OnSelectPhotoListener{
    /**默认可选的最多的图片数量*/
    public static final int DEFAULT_MAX_SELECTED_PHOTOS=9;
    private RecyclerView photoRecyclerView;
    private TextView tvPhotoDirectoryName;
    private TextView tvDirectoryPhotoCounts;
    private TextView btnRight;
    private List<PhotoDirectory> directoryList;
    private PhotoDirectoryItemAdapter photoDirectoryItemAdapter;
    private PhotoItemAdapter photoItemAdapter=null;

    private int photoMaxSelectCounts=DEFAULT_MAX_SELECTED_PHOTOS;

    private ListPopupWindow directoryListPopupWindow=null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo_picker;
    }

    protected void initViews() {
        setTitle("相册");
        btnRight=titleBar.getRightMenuTextView();
        getDatas();
        addTitleBarRightItem();
        photoRecyclerView= (RecyclerView) this.findViewById(R.id.rv_photos);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        tvPhotoDirectoryName=(TextView)this.findViewById(R.id.tv_photo_directory);
        tvPhotoDirectoryName.setOnClickListener(this);
        tvDirectoryPhotoCounts=(TextView)this.findViewById(R.id.tv_directory_photo_counts);
        initPhotoDirectoryListPopupWindow();
        getPhotoDirectory();
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
        int id=view.getId();
        switch (id){
            case R.id.tv_photo_directory:
                showPhotoDirectoryPopupWindow();
                break;
        }
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    private void getDatas(){
        photoMaxSelectCounts=this.getIntent().getIntExtra("max_photo_counts",DEFAULT_MAX_SELECTED_PHOTOS);
    }

    private void addTitleBarRightItem(){
        int paddingTop= CommonUtils.dip2px(this, 6);
        int paddingLeft= CommonUtils.dip2px(this, 10);
        btnRight.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
        btnRight.setTextColor(Color.parseColor("#ffffff"));
        btnRight.setTextSize(12);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("完成(0/" + photoMaxSelectCounts + ")");
        btnRight.setBackgroundResource(R.drawable.bt_image_select);
        btnRight.setGravity(Gravity.CENTER);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selected_photos", (ArrayList<String>) photoItemAdapter.getSelectedPhotos());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initPhotoDirectoryListPopupWindow(){
        directoryList=new ArrayList<>();
        photoDirectoryItemAdapter=new PhotoDirectoryItemAdapter(this,directoryList);
        photoDirectoryItemAdapter.setOnItemClickListener(this);
        directoryListPopupWindow=new ListPopupWindow(this);
        directoryListPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        setPhotoDirectoryListPopupWindowHeight(directoryListPopupWindow);
        directoryListPopupWindow.setAnchorView(this.findViewById(R.id.layout_photo_directorys));
        directoryListPopupWindow.setAdapter(photoDirectoryItemAdapter);
        directoryListPopupWindow.setModal(true);
        directoryListPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        //directoryListPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
    }

    private void setPhotoDirectoryListPopupWindowHeight(ListPopupWindow popupWindow){
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int height= (int) (wm.getDefaultDisplay().getHeight()*0.75);
        popupWindow.setHeight(height);
    }

    private void getPhotoDirectory(){
        ImageMediaStoreUtil.getPhotoDirs(this, null, new ImageMediaStoreUtil.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> directories) {
                if (directories != null && directories.size() > 0) {
                    if (directoryList == null) {
                        directoryList = new ArrayList<PhotoDirectory>();
                        directoryList.addAll(directories);
                    } else {
                        if (directoryList.size() > 0) {
                            directoryList.clear();
                        }
                        directoryList.addAll(directories);
                    }
                    KLog.e("图片目录数量:" + directoryList.size());
                    photoDirectoryItemAdapter.notifyDataSetChanged();
                    if (photoItemAdapter == null) {
                        photoItemAdapter = new PhotoItemAdapter(directoryList.get(0), photoMaxSelectCounts);
                        photoItemAdapter.setOnSelectPhotoListener(PhotoPickerActivity.this);
                        photoRecyclerView.setAdapter(photoItemAdapter);
                    } else {
                        photoItemAdapter.setPhotoDirectory(directoryList.get(0));
                    }
                    tvPhotoDirectoryName.setText(directoryList.get(0).getDirectoryName());
                    tvDirectoryPhotoCounts.setText(directoryList.get(0).getPhotos().size() + "张");
                }
            }
        });
    }

    private void showPhotoDirectoryPopupWindow(){
        if(directoryListPopupWindow.isShowing()){
            directoryListPopupWindow.dismiss();
        }else{
            directoryListPopupWindow.show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(directoryList!=null&&directoryList.size()>0) {
            PhotoDirectory photoDirectory = directoryList.get(position);
            photoItemAdapter.setPhotoDirectory(photoDirectory);
            tvDirectoryPhotoCounts.setText(photoDirectory.getPhotos().size()+"张");
            tvPhotoDirectoryName.setText(photoDirectory.getDirectoryName());
            directoryListPopupWindow.dismiss();
        }
    }

    @Override
    public void onSelectedPhoto(int selelctedPhotos, String photoPath, boolean isSelected) {
        btnRight.setText("完成(" + selelctedPhotos + "/" + photoMaxSelectCounts + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x0001){
            List<String> selectedPhotos=data.getStringArrayListExtra("selected_photos");
            if(selectedPhotos!=null){
                photoItemAdapter.setSelectedPhotos(selectedPhotos);
                btnRight.setText("完成(" + selectedPhotos.size() + "/" + photoMaxSelectCounts + ")");
                if(resultCode==RESULT_OK){
                    Intent intent=new Intent();
                    intent.putStringArrayListExtra("selected_photos", (ArrayList<String>) selectedPhotos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

}

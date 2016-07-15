package com.tysci.ballq.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.dialog.EditUserNicknameDialog;
import com.tysci.ballq.dialog.EditUserPortraitDialog;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.SettingItemView;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-14 0014.
 * 设置
 */
public class BallQSettingActivity extends BaseActivity implements EditUserNicknameDialog.EditNicknameSuccessCallback {
    @Bind(R.id.setting_user_icon)
    SettingItemView userIconItem;
    @Bind(R.id.setting_user_nickname)
    SettingItemView userNicknameItem;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        // 用户信息展示
        UserInfoEntity userInfo = UserInfoUtil.getUserInfo(this);
        if (userInfo != null) {
            userIconItem.setIcon(userInfo.getPt());
            userNicknameItem.setName(userInfo.getFname());
        }
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

    @OnClick({R.id.setting_user_icon, R.id.setting_user_nickname, R.id.tv_exit})
    public void onSettingItemClick(View view) {
        switch (view.getId()) {
            case R.id.setting_user_icon:
                EditUserPortraitDialog editUserPortraitDialog = new EditUserPortraitDialog(this);
                Window window = editUserPortraitDialog.getWindow();
                window.setWindowAnimations(R.style.DialogFromBottom);
                window.setGravity(Gravity.BOTTOM);
                editUserPortraitDialog.show();
                break;
            case R.id.setting_user_nickname:
                EditUserNicknameDialog editUserNicknameDialog = new EditUserNicknameDialog(this);
                editUserNicknameDialog.setCallback(this);
                editUserNicknameDialog.show();
                break;
            case R.id.tv_exit:
                UserInfoUtil.exitLogin(this);
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EditUserPortraitDialog.PHOTO_ALBUM:
                    startPhotoZoom(data.getData());
                    break;
                case EditUserPortraitDialog.PHOTO_TAKE:
                    File temp = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + EditUserPortraitDialog.PHOTO_NAME);
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                case EditUserPortraitDialog.PHOTO_CUT:
                    KLog.e("PHOTO_CUT");
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }
    }

    private void setPicToView(Intent picData) {
        Bundle extras = picData.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            updateUserHeaderIcon(photo);
        }
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, EditUserPortraitDialog.PHOTO_CUT);
    }

    private void updateUserHeaderIcon(final Bitmap photo) {
        HashMap<String, String> map;
        if (UserInfoUtil.checkLogin(this)) {
            map = new HashMap<>();
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        } else {
            return;
        }
        final String PHOTO_NAME = "BallQUserTmpPhoto.jpg";
        File f = new File(Environment.getExternalStorageDirectory().getPath(), PHOTO_NAME);
        FileUtil.writeBitmapToFile(f, photo);
        HttpClientUtil.getHttpClientUtil().uploadPortrait(Tag, HttpUrls.HOST_URL_V5 + "user/edit_profile/", new String[]{"pt"}, new File[]{f}, map, new HttpClientUtil.ProgressResponseCallBack() {
            @Override
            public void loadingProgress(int progress) {
                showLoading();
            }

            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                hideLoad();
            }

            @Override
            public void onSuccess(Call call, String response) {
                hideLoad();
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (object.getInteger("status") == 307) {
                    ToastUtil.show(BallQSettingActivity.this, "成功修改头像");
                    userIconItem.setIcon(photo);
                    updateUserInfo();
                } else {
                    ToastUtil.show(BallQSettingActivity.this, object.getString("message"));
                }
            }

            @Override
            public void onFinish(Call call) {
            }
        });
    }

    public void updateUserInfo() {
        UserInfoUtil.getUserInfo(this, Tag, UserInfoUtil.getUserId(this), UserInfoUtil.checkLogin(this), null);
    }

    @Override
    public void callback(EditUserNicknameDialog dialog, String nicknameNew) {
        userNicknameItem.setName(nicknameNew);
        updateUserInfo();
        dialog.dismiss();
    }
}

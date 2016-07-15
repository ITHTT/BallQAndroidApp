package com.tysci.ballq.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;
import com.tysci.ballq.utils.ScreenUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LinDe on 2016-07-15 0015.
 * 修改头像
 */
public class EditUserPortraitDialog extends BaseDialog {
    /**
     * 相册
     */
    public static final int PHOTO_ALBUM = 1;
    /**
     * 拍照
     */
    public static final int PHOTO_TAKE = 2;
    /**
     * 裁剪
     */
    public static final int PHOTO_CUT = 3;

    public static final String PHOTO_NAME;

    static {
        PHOTO_NAME = "BallQHeader.jpg";
    }

    @Bind(R.id.layout_parent)
    LinearLayout layout_parent;

    public EditUserPortraitDialog(Activity context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_edit_user_portrait;
    }

    @Override
    protected void initializing(Bundle savedInstanceState) {
        ViewGroup.LayoutParams lp = layout_parent.getLayoutParams();
        DisplayMetrics dm = ScreenUtil.getDisplayMetrics(this.getContext());
        lp.width = dm.widthPixels;
        layout_parent.setLayoutParams(lp);
    }

    /**
     * 从相册获取
     */
    @OnClick(R.id.tv_from_photo_album)
    public void onAlbumClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getOwnerActivity().startActivityForResult(intent, PHOTO_ALBUM);
        dismiss();
    }

    /**
     * 拍照
     */
    @OnClick(R.id.tv_take_photo)
    public void onTakePhotoClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_NAME)));
        getOwnerActivity().startActivityForResult(intent, PHOTO_TAKE);
        dismiss();
    }

    @OnClick(R.id.tv_cancel)
    public void onCancel(View view) {
        dismiss();
    }

}

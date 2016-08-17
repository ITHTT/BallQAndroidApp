package com.tysci.ballq.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.ScreenUtil;
import com.tysci.ballq.utils.ToastUtil;

import java.io.File;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016-08-17 0017.
 */
public class SaveBitmapDialog extends BaseDialog
{
    private Bitmap mBitmap;

    public SaveBitmapDialog(Activity context)
    {
        super(context);
    }

    public void setBitmap(Bitmap bitmap)
    {
        mBitmap = bitmap;
    }

    @Override
    protected int getContentView()
    {
        return R.layout.dialog_save_bitmap;
    }

    @Override
    protected void initializing(Bundle savedInstanceState)
    {
        ViewGroup layout = (ViewGroup) this.findViewById(R.id.layout_content);
        ViewGroup.LayoutParams lp = layout.getLayoutParams();
        DisplayMetrics dm = ScreenUtil.getDisplayMetrics(this.getContext());
        lp.width = dm.widthPixels;
        layout.setLayoutParams(lp);
    }

    @OnClick(R.id.tv_save_bitmap_to_sd)
    protected void onSaveBitmapToSD_Click(View view)
    {
        final Context context = getContext();
        if (mBitmap == null)
        {
            ToastUtil.show(context, "保存失败");
            return;
        }
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/BallQ/Download/" + System.currentTimeMillis() + ".png";
        if (FileUtil.writeBitmapToFile(new File(filePath), mBitmap))
        {
            ToastUtil.show(context, "已成功将图片保存至" + filePath);
        }
        else
        {
            ToastUtil.show(context, "保存失败");
        }
        dismiss();
    }

    @OnClick(R.id.tv_cancel)
    protected void onCancelClick(View view)
    {
        dismiss();
    }
}

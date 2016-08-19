package com.tysci.ballq.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;

/**
 * Created by LinDe
 * on 2016-08-17 0017.
 */
public class SpinKitProgressDialog extends BaseDialog
{

    public SpinKitProgressDialog(Context context)
    {
        super(context);
        setCancelable(false);
    }

    public SpinKitProgressDialog(Activity context)
    {
        super(context);
        setCancelable(false);
    }

    @Override
    protected int getContentView()
    {
        return R.layout.dialog_spin_kit_progress;
    }

    @Override
    protected void initializing(Bundle savedInstanceState)
    {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

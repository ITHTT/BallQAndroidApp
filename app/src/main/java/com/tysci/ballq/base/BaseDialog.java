package com.tysci.ballq.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.tysci.ballq.R;

import butterknife.ButterKnife;

/**
 * Created by LinDe on 2016-07-15 0015.
 *
 * @author LinDe
 */
public abstract class BaseDialog extends Dialog {
    protected final String TAG=getClass().getSimpleName();
    public BaseDialog(Activity context) {
        super(context, R.style.CustomDialogStyle);
        setOwnerActivity(context);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initializing(savedInstanceState);
    }

    protected abstract
    @LayoutRes
    int getContentView();

    protected abstract void initializing(Bundle savedInstanceState);

    @Override
    public void dismiss() {
        ButterKnife.unbind(this);
        super.dismiss();
    }
}

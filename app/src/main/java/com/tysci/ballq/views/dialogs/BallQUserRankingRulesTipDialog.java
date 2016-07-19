package com.tysci.ballq.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/7/19.
 */
public class BallQUserRankingRulesTipDialog extends Dialog implements View.OnClickListener{
    private Window window;
    private Context context;
    private View view;

    public BallQUserRankingRulesTipDialog(Context context) {
        super(context,R.style.CustomDialogStyle);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window = this.getWindow();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initViews(context);
    }

    private void initViews(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_user_rank_rules, null);
        this.setContentView(view);

        view.findViewById(R.id.iv_delete).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogWindowAttrs();
    }

    protected void setDialogWindowAttrs() {
        // TODO Auto-generated method stub
        Activity activity = (Activity) context;
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = window.getAttributes();
        //p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth() * 0.85);
        window.setAttributes(p);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_delete:
                dismiss();
                break;
        }

    }
}

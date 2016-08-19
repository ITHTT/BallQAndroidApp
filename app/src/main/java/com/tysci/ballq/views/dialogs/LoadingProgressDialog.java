package com.tysci.ballq.views.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;

/**
 * Created by HTT on 2016/1/15.
 *
 * @deprecated use {@link com.tysci.ballq.dialog.SpinKitProgressDialog}
 */
@Deprecated
public class LoadingProgressDialog extends BaseDialog
{
    public LoadingProgressDialog(Context context)
    {
        super(context);
        setCancelable(false);
    }

    public LoadingProgressDialog(Activity context)
    {
        super(context);
        setCancelable(false);
    }
//    private Context context;
//    private View view;
//    //private ImageView ivProgress;
//    private TextView tvMessage;
//
//    public LoadingProgressDialog(Context context)
//    {
//        super(context);
//        initViews(context);
//    }

    //    private void initViews(Context context)
//    {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawable(
//                new ColorDrawable(Color.TRANSPARENT));
//        this.setCanceledOnTouchOutside(false);
//        this.context = context;
//        view = getLayoutInflater().inflate(R.layout.dialog_loading_progress, null);
////        ivProgress=(ImageView)view.findViewById(R.id.iv_progress_spinner);
////        ivProgress.setImageResource(R.anim.round_spinner);
//        tvMessage = (TextView) view.findViewById(R.id.tv_message);
//        this.setContentView(view);
//    }
//
    public void setMessage(String message)
    {
//        tvMessage.setText(message);
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

    //
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        Window window = this.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = 0.3f;
//        window.setAttributes(lp);
//    }
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

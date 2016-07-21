package com.tysci.ballq.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/7/21.
 */
public class BallQInputMoneysDialog extends Dialog {
    private EditText etComment;
    private TextView tvPost;
    private TextView tvTitle;
    private OnSubmitListener onSubmitListener;

    public BallQInputMoneysDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        initViews(context);
    }

    private void initViews(Context context){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_add_barrage);
        etComment=(EditText)this.findViewById(R.id.editText1);
        tvPost=(TextView)this.findViewById(R.id.tvCommit);
        tvPost.setText("确定");
        tvTitle=(TextView)this.findViewById(R.id.tvTitle);
        tvTitle.setText("其他金额");
        etComment.setHint("请输入金额(元)");
        etComment.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvPost.setEnabled(false);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPost.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=etComment.getText().toString();
                if(onSubmitListener!=null){
                    onSubmitListener.onSubmit(text);
                }
                //dismiss();
            }
        });

        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener{
        void onSubmit(String text);
    }

}

package com.tysci.ballq.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallQAddBarrageDialog extends Dialog {
    private EditText etComment;
    private TextView tvPost;
    private OnPostBarrageListener onPostBarrageListener=null;

    public BallQAddBarrageDialog(Context context) {
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
        tvPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(onPostBarrageListener!=null){
                    onPostBarrageListener.onPostBarrage(etComment.getText().toString());
                }
            }
        });
    }

    public void setOnPostBarrageListener(OnPostBarrageListener onPostBarrageListener) {
        this.onPostBarrageListener = onPostBarrageListener;
    }

    public interface OnPostBarrageListener{
       void onPostBarrage(String content);
    }
}

package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/1/14.
 */
public class LoadDataLayout extends LinearLayout{
    private LinearLayout layoutLoading;
    private ProgressBar loadProgressBar;
    private TextView tvLoadMsg;

    private LinearLayout layoutLoadResult;
    private ImageView ivLoadResultIcon;
    private TextView tvLoadResultText;
    private TextView btClick;


    public LoadDataLayout(Context context) {
        super(context);
        initViews(context);
    }

    public LoadDataLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LoadDataLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadDataLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_loading_data,this,true);
        layoutLoading=(LinearLayout)this.findViewById(R.id.layout_loading_data);
        loadProgressBar=(ProgressBar)this.findViewById(R.id.pb_loading_progressbar);
        tvLoadMsg=(TextView)this.findViewById(R.id.loading_msg);
        btClick=(TextView)this.findViewById(R.id.tv_click_info);

        layoutLoadResult=(LinearLayout)this.findViewById(R.id.layout_load_result);
        ivLoadResultIcon=(ImageView)this.findViewById(R.id.message_icon);
        tvLoadResultText=(TextView)this.findViewById(R.id.message_info);
    }

    public void showLoading(String msg){
        if(this.getVisibility()!=View.VISIBLE){
            this.setVisibility(View.VISIBLE);
        }
        if(layoutLoadResult.getVisibility()!= View.GONE){
            layoutLoadResult.setVisibility(View.GONE);
        }

        if(layoutLoading.getVisibility()!=View.VISIBLE){
            layoutLoading.setVisibility(View.VISIBLE);
        }
        tvLoadMsg.setText(msg);
    }

    public void setLoadingTextColor(int color){
        tvLoadMsg.setTextColor(color);
        tvLoadResultText.setTextColor(color);
    }

    private void showLoadingState(){
        if(this.getVisibility()!=View.VISIBLE){
            this.setVisibility(View.VISIBLE);
        }

        if(layoutLoadResult.getVisibility()!= View.GONE){
            layoutLoadResult.setVisibility(View.GONE);
        }

        if(layoutLoading.getVisibility()!=View.VISIBLE){
            layoutLoading.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(){
        showLoadingState();
    }


    private void showLoadResultState(){
        if(this.getVisibility()!=View.VISIBLE){
            this.setVisibility(View.VISIBLE);
        }

        if(layoutLoadResult.getVisibility()!= View.VISIBLE){
            layoutLoadResult.setVisibility(View.VISIBLE);
        }

        if(layoutLoading.getVisibility()!=View.GONE){
            layoutLoading.setVisibility(View.GONE);
        }
    }

    private void setLoadResult(int iconRes,String text){
        ivLoadResultIcon.setImageResource(iconRes);
        tvLoadResultText.setText(text);
    }

    public void setLoadEmpty(String msg){
        showLoadResultState();
    }

    public void setLoadEmpty(){
        showLoadResultState();
    }

    public void setLoadError(String msg,OnClickListener listener){
        showLoadResultState();
        setLoadResult(R.mipmap.page_icon_failed, msg);
        btClick.setOnClickListener(listener);
    }

    public void setLoadError(OnClickListener listener){
        showLoadResultState();
        btClick.setOnClickListener(listener);
    }

    public void setLoadError(String msg){
        showLoadResultState();
    }

    public void setLoadError(){
        showLoadResultState();
    }

    public void hideLoad(){
        if(this.getVisibility()!=View.GONE){
            this.setVisibility(View.GONE);
        }

    }
}

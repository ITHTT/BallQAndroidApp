package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/7/12.
 */
public class MainBottomMenuView extends LinearLayout{
    private ImageView ivMenuIcon;
    private TextView tvMenuName;

    private int menuIconRes;
    private String menuName;
    private boolean isChecked=false;
    private int selectedColor= Color.parseColor("#eacb70");
    private int normalColor=Color.parseColor("#ffffff");

    public MainBottomMenuView(Context context) {
        super(context);
        initViews(context,null);
    }

    public MainBottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public MainBottomMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainBottomMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    private void initViews(Context context,AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_main_bottom_menu_item,this,true);
        ivMenuIcon=(ImageView)this.findViewById(R.id.iv_menu_icon);
        tvMenuName=(TextView)this.findViewById(R.id.tv_menu_name);
        tvMenuName.setTextColor(normalColor);
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainBottomMenuView);
            menuIconRes=ta.getResourceId(R.styleable.MainBottomMenuView_main_menu_icon, -1);
            menuName=ta.getString(R.styleable.MainBottomMenuView_main_menu_name);
            isChecked=ta.getBoolean(R.styleable.MainBottomMenuView_main_menu_is_checked,false);
            ta.recycle();
            if(menuIconRes>=0){
                ivMenuIcon.setImageResource(menuIconRes);
            }
            if(!TextUtils.isEmpty(menuName)){
                tvMenuName.setText(menuName);
            }
        }
    }

    public void setMenuChecked(boolean isChecked){
        this.isChecked=isChecked;
        ivMenuIcon.setSelected(isChecked);
        if(this.isChecked){
            tvMenuName.setTextColor(selectedColor);
        }else{
            tvMenuName.setTextColor(normalColor);

        }
    }
}

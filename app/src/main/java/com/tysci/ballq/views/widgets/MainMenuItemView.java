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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by HTT on 2016/5/28.
 */
public class MainMenuItemView extends LinearLayout {
    private View selectMark;
    private ImageView ivMenuIcon;
    private TextView tvMenuTitle;
    private TextView tvMenuBrief;
    private TextView tvMenuMsg;
    private ViewGroup layoutMenuContent;

    private boolean isChecked;
    private int menuIconRes;
    private String menuName;
    private String menuBrief;

    private int checkedColor=Color.parseColor("#050404");
    private int textColor=Color.parseColor("#2e2e2e");

    public MainMenuItemView(Context context) {
        super(context);
        initViews(context,null);
    }

    public MainMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public MainMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainMenuItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    private void initViews(Context context,AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_main_menu_item,this,true);
        selectMark=this.findViewById(R.id.selected_mark);
        ivMenuIcon=(ImageView)this.findViewById(R.id.iv_menu_icon);
        tvMenuTitle=(TextView)this.findViewById(R.id.tv_menu_title);
        tvMenuMsg=(TextView)this.findViewById(R.id.tv_menu_message);
        tvMenuBrief=(TextView)this.findViewById(R.id.tv_menu_brief);
        layoutMenuContent= (ViewGroup) this.findViewById(R.id.layout_menu_content);
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainMenuItemView);
            menuIconRes=ta.getResourceId(R.styleable.MainMenuItemView_menu_icon, -1);
            menuName=ta.getString(R.styleable.MainMenuItemView_menu_name);
            menuBrief=ta.getString(R.styleable.MainMenuItemView_menu_brief);
            isChecked=ta.getBoolean(R.styleable.MainMenuItemView_is_checked,false);
            checkedColor=ta.getColor(R.styleable.MainMenuItemView_checked_color,checkedColor);
            textColor=ta.getColor(R.styleable.MainMenuItemView_text_color,textColor);
            ta.recycle();
            if(menuIconRes>=0){
                ivMenuIcon.setImageResource(menuIconRes);
            }
            if(!TextUtils.isEmpty(menuName)){
                tvMenuTitle.setText(menuName);
            }
            if(!TextUtils.isEmpty(menuBrief)){
                tvMenuBrief.setVisibility(View.VISIBLE);
                tvMenuBrief.setText(menuBrief);
            }else{
                tvMenuBrief.setVisibility(View.GONE);
            }
        }
        tvMenuTitle.setTextColor(textColor);
        setCheckedState(isChecked);
    }

    public void setCheckedState(boolean isChecked){
        this.isChecked=isChecked;
        if(this.isChecked){
            layoutMenuContent.setBackgroundColor(checkedColor);
            selectMark.setVisibility(View.VISIBLE);
        }else{
            layoutMenuContent.setBackgroundResource(android.R.color.transparent);
            selectMark.setVisibility(View.INVISIBLE);
        }
    }


}

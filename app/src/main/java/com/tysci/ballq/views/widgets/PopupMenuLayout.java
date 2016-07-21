package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/6/30.
 */
public class PopupMenuLayout extends FrameLayout {
    public final static int POPUP_DOWN=1;
    public final static int POPUP_UP=0;
    private Context context;
    /**弹出方向，向上或向下*/
    private int direction=POPUP_UP;
    /**菜单项*/
    private List<View> menuItems=null;
    /**菜单项的大小*/
    private int menuItemWidth=45;
    /**菜单项的高度*/
    private int menuItemHeight=45;
    /**菜单项间的间距*/
    private int itemPadding=10;
    /**菜单项与目标视图间的间距*/
    private int itemToTargetPadding=20;
    /**动画时间*/
    private int duration = 300;
    /**延迟单位时间*/
    private int delay=80;
    /**目标视图*/
    private View targetView;
    /**目标视图相对于该视图的位置*/
    private int targetX;
    private int targetY;

    private boolean isShowing;
    private boolean isShowingAnimationPlaying;
    private boolean isHideAnimationPlaying;

    private OnPopupMenuShowListener onPopupMenuShowListener;


    public PopupMenuLayout(Context context) {
        super(context);
        initViews(context,null);
    }

    public PopupMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public PopupMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PopupMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    private void initViews(Context context,AttributeSet attrs){
        this.context=context;
        menuItems=new ArrayList<>(5);
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            menuItemWidth=a.getDimensionPixelSize(R.styleable.PopupMenuLayout_menu_item_width, CommonUtils.dip2px(context,45));
            menuItemHeight=a.getDimensionPixelSize(R.styleable.PopupMenuLayout_menu_item_height,CommonUtils.dip2px(context,45));
            itemPadding=a.getDimensionPixelSize(R.styleable.PopupMenuLayout_menu_item_padding,CommonUtils.dip2px(context,10));
            itemToTargetPadding=a.getDimensionPixelSize(R.styleable.PopupMenuLayout_menu_target_padding,CommonUtils.dip2px(context,20));
            a.recycle();
        }
    }

    public void setOnPopupMenuShowListener(OnPopupMenuShowListener onPopupMenuShowListener) {
        this.onPopupMenuShowListener = onPopupMenuShowListener;
    }

    public void setTargetView(View view){
        this.targetView=view;
        this.targetView.post(new Runnable() {
            @Override
            public void run() {
                calculateTargetXY();
                int size=menuItems.size();
                for(int i=0;i<size;i++){
                    View view=menuItems.get(i);
                    LayoutParams layoutParams = new LayoutParams(menuItemWidth, menuItemWidth);
                    layoutParams.leftMargin=targetX+targetView.getWidth()/2-menuItemWidth/2;
                    addView(view,layoutParams);
                    view.setVisibility(View.GONE);
                    menuItems.add(view);
                }
            }
        });

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowingAnimationPlaying && !isHideAnimationPlaying) {
                    if (!isShowing) {
                        showAnimation();
                        if (onPopupMenuShowListener != null) {
                            onPopupMenuShowListener.onMenuShow();
                        }
                    } else {
                        hideAnimation();
                        if (onPopupMenuShowListener != null) {
                            onPopupMenuShowListener.onMenuDimiss();
                        }
                    }
                }
            }
        });
    }

    public void addMenu(View view){
        menuItems.add(view);
    }

    private void calculateTargetXY(){
        View view=targetView;
        while(view!=null&&view!=this){
           targetY+=view.getTop();
           targetX+=view.getLeft();
           view= (View) view.getParent();
        }
    }

    public void toggle(){
        if(!isShowingAnimationPlaying&&!isHideAnimationPlaying) {
            if(!isShowing) {
                showAnimation();
                if(onPopupMenuShowListener!=null){
                    onPopupMenuShowListener.onMenuShow();
                }
            }else{
                hideAnimation();
                if(onPopupMenuShowListener!=null){
                    onPopupMenuShowListener.onMenuDimiss();
                }
            }
        }
    }

    /**
     * 显示动画
     */
    private void showAnimation(){
        isShowing=true;
        isShowingAnimationPlaying=true;
        int size=menuItems.size();
       for(int i=0;i<size;i++){
           final int position=i;
                    final View imageView=menuItems.get(i);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.bringToFront();
                    ObjectAnimator yAnimator = ObjectAnimator.ofFloat(imageView, "y", targetY+targetView.getHeight() / 2-20, targetY+targetView.getHeight()+itemToTargetPadding + (size-i-1) * (menuItemHeight + itemPadding));
                    yAnimator.setDuration(duration);
                    yAnimator.setStartDelay(i*delay);
                    yAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    yAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(position==4){
                                isShowingAnimationPlaying=false;
                            }
                            imageView.setClickable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    yAnimator.start();

                    float scaleW = 0;
                    float scaleH = 0;
                    imageView.setScaleX(scaleW);
                    ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX",
                            scaleW,
                            1f).setDuration(duration);
                    scaleXAnimator.setStartDelay(i*delay);
                    scaleXAnimator.start();

                    imageView.setScaleY(scaleH);
                    ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY",
                            scaleH,
                            1f).setDuration(duration);
                    scaleYAnimator.setStartDelay(i*delay);
                    scaleYAnimator.start();
       }
    }


    private void hideAnimation(){
        isShowing=false;
        isHideAnimationPlaying=true;
        int size=menuItems.size();
        for(int i=0;i<size;i++){
            final int position=i;
            final View imageView=menuItems.get(i);
            imageView.bringToFront();
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(imageView, "y",targetY+targetView.getHeight() +itemToTargetPadding+ (size-i-1) * (menuItemHeight + itemPadding),targetY+targetView.getHeight() / 2-20);
            yAnimator.setDuration(duration);
            yAnimator.setStartDelay((size-i-1)*delay);
            yAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            yAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(position==4){
                        isHideAnimationPlaying=false;
                    }
                    imageView.setClickable(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            yAnimator.start();

            float scaleW = 1f;
            float scaleH = 1f;
            imageView.setScaleX(scaleW);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX",
                    scaleW,
                    0f).setDuration(duration);
            scaleXAnimator.setStartDelay((size-i-1)*delay);
            scaleXAnimator.start();

            imageView.setScaleY(scaleH);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY",
                    scaleH,
                    0f).setDuration(duration);
            scaleYAnimator.setStartDelay((size-i-1)*delay);
            scaleYAnimator.start();
        }
    }

    public List<View> getMenuItems() {
        return menuItems;
    }

    public boolean isShowing(){
        return this.isShowing;
    }

    public interface OnPopupMenuShowListener{
        void onMenuShow();
        void onMenuDimiss();
    }
}

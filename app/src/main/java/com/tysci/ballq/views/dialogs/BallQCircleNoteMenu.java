package com.tysci.ballq.views.dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.widgets.PromotedActionsLibrary;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BallQCircleNoteMenu extends PopupWindow implements PopupWindow.OnDismissListener{
    private FrameLayout frameLayout;
    private PromotedActionsLibrary promotedActionsLibrary;
    private Context context;

    public BallQCircleNoteMenu(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.layout_circle_menu,null);
        setContentView(view);

        frameLayout= (FrameLayout) view.findViewById(R.id.container);

        promotedActionsLibrary=new PromotedActionsLibrary();
        promotedActionsLibrary.setup(context, frameLayout);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        this.setOutsideTouchable(true);
        this.setFocusable(true);

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        this.setOnDismissListener(this);
        this.setAnimationStyle(R.style.MenuAnim);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                View v=frameLayout.getChildAt(0);
                if(v!=null) {
                    int y = (int) event.getY();
                    int x = (int) event.getX();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y > v.getBottom() || x < v.getLeft()) {
                            dismiss();
                        }
                    }
                }
                return true;
            }
        });
    }

    public View addItem(int res,View.OnClickListener listener){
        return promotedActionsLibrary.addItem(res,res,listener);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onShow(View view,int x,int y) {
        showAsDropDown(view, x, y);
        promotedActionsLibrary.openPromotedActions().start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDismiss() {
        if(this.isShowing()){
            //promotedActionsLibrary.openPromotedActions().start();
            KLog.e("窗口显示:");
        }else{
            // promotedActionsLibrary.closePromotedActions().start();
            KLog.e("窗口关闭");
        }
    }
}

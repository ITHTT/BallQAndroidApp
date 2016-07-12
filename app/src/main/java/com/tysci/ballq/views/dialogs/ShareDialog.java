package com.tysci.ballq.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tysci.ballq.R;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.WeChatUtil;
import com.tysci.ballq.wxapi.WXEntryActivity;

/**
 * Created by HTT on 2016/7/5.
 */
public class ShareDialog extends Dialog implements View.OnClickListener{
    /**分享类型*/
    private String shareType;
    private String title;
    private String excerpt;
    private String shareUrl;

    public ShareDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        initViews(context);
    }

    private void initViews(Context context){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_share);
        this.findViewById(R.id.layoutWeChatShareFriends).setOnClickListener(this);
        this.findViewById(R.id.layoutWeChatShareFriendCircles).setOnClickListener(this);
    }

    public ShareDialog setShareTitle(String title){
        this.title=title;
        return this;
    }

    public ShareDialog setShareUrl(String url){
        this.shareUrl=url;
        return this;
    }

    public ShareDialog setShareExcerpt(String excerpt){
        this.excerpt=excerpt;
        return this;
    }

    public ShareDialog setShareType(String type){
        this.shareType=type;
        return this;
    }

    private void share(int type){
        WeChatUtil.shareWebPage(getContext(),type,title,excerpt,shareUrl);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layoutWeChatShareFriendCircles:
                WXEntryActivity.REQUEST_TAG=1;
                share(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.layoutWeChatShareFriends:
                WXEntryActivity.REQUEST_TAG=1;
                share(SendMessageToWX.Req.WXSceneSession);
                break;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KLog.e("销毁分享对话框。。。");
    }
}

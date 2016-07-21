package com.tysci.ballq.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.WeChatUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        WeChatUtil.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChatUtil.wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0)
    {

    }

    @Override
    public void onResp(final BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            switch (resp.errCode)
            {
                case 0:
                    ToastUtil.show(this,"微信支付成功");
                    break;
                case -1:
                    ToastUtil.show(this,"微信支付失败");
                    break;
                case -2:
                    ToastUtil.show(this,"微信支付取消");
                    break;
            }
            WXPayEntryActivity.this.finish();
        }
    }
}

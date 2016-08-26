package com.tysci.ballq.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.pingplusplus.android.Pingpp;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.dialog.SpinKitProgressDialog;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.PingPayAdapter;
import com.tysci.ballq.views.widgets.NoScrollGridView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-18 0018.
 * <p/>
 * ping++ sdk 示例程序，仅供开发者参考。
 * 【说明文档】https://github.com/PingPlusPlus/pingpp-android/blob/master/docs/ping%2B%2B安卓SDK使用文档.md
 * <p/>
 * 【注意】运行该示例，需要用户填写一个YOUR_URL。
 * <p/>
 * ping++ sdk 使用流程如下：
 * 1）客户端已经有订单号、订单金额、支付渠道
 * 2）客户端请求服务端获得charge。服务端生成charge的方式参考ping++ 官方文档，地址 https://pingxx.com/guidance/server/import
 * 3）收到服务端的charge，调用ping++ sdk 。
 * 4）onActivityResult 中获得支付结构。
 * 5）如果支付成功。服务端会收到ping++ 异步通知，支付成功依据服务端异步通知为准。
 */
public class PingPayActivity extends BaseActivity implements TextWatcher
{
    /**
     * 开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
     * 服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import
     * <p/>
     * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
     * 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
//    private static String final URL = "http://218.244.151.190/demo/charge";
    private static final String URL = HttpUrls.HOST_URL_V5 + "user/app_pay/";
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";

    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    @Bind(R.id.etOtherMoney)
    EditText etOtherMoney;

    @Bind(R.id.gridView)
    NoScrollGridView gridView;
    private PingPayAdapter adapter;

    private String currentAmount;

    private boolean isPaying;// 用作判断 防止重复提交支付
    private SpinKitProgressDialog mSpinKitProgressDialog;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ping_pay;
    }

    @Override
    protected void initViews()
    {
        setTitleText("球商充值");

        currentAmount = "";
        etOtherMoney.addTextChangedListener(this);

        adapter = new PingPayAdapter();
        gridView.setAdapter(adapter);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {

    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        adapter.removeCheckAmount();

        etOtherMoney.removeTextChangedListener(this);

        if (!s.toString().equals(currentAmount))
        {
            etOtherMoney.removeTextChangedListener(this);
            String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
            String cleanString = s.toString().replaceAll(replaceable, "");

            if (cleanString.equals("") || new BigDecimal(cleanString).toString().equals("0"))
            {
                etOtherMoney.setText(null);
            }
            else
            {
                double parsed = Double.parseDouble(cleanString);
                String formatted = NumberFormat.getCurrencyInstance(Locale.CHINA).format((parsed / 100));
                currentAmount = formatted;
                etOtherMoney.setText(formatted);
                etOtherMoney.setSelection(formatted.length());
            }
        }

        etOtherMoney.addTextChangedListener(this);
    }

    /**
     * 微信支付、支付宝支付
     */
    @OnClick({R.id.layoutPayWeChat, R.id.layoutPayALi})
    protected void onPayWeChatClick(View view)
    {
        if (isPaying)
            return;
        isPaying = true;

        String channel = null;
        switch (view.getId())
        {
            case R.id.layoutPayWeChat:
                channel = CHANNEL_WECHAT;
                break;
            case R.id.layoutPayALi:
                channel = CHANNEL_ALIPAY;
                break;
        }
        if (channel == null)
            return;
        String amount = String.valueOf(adapter.getCheckAmount() * 100);
        if (TextUtils.isEmpty(amount) || amount.equals(String.valueOf(0)))
        {
            amount = etOtherMoney.getText().toString();
            if (TextUtils.isEmpty(amount))
                return;
            String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
            String cleanString = amount.replaceAll(replaceable, "");
            amount = new BigDecimal(cleanString).toString();
        }

        HashMap<String, String> map = new HashMap<>();
        if (UserInfoUtil.checkLogin(this))
        {
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        }
        map.put("channel", channel);
        map.put("amount", amount);
        KLog.e(map);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, URL, map, new HttpClientUtil.ProgressResponseCallBack()
        {
            @Override
            public void loadingProgress(int progress)
            {

            }

            @Override
            public void onBefore(Request request)
            {
                if (mSpinKitProgressDialog == null)
                    mSpinKitProgressDialog = new SpinKitProgressDialog(PingPayActivity.this);
                mSpinKitProgressDialog.show();
            }

            @Override
            public void onError(Call call, Exception error)
            {
                showMsg("", "请求出错", getResources().getString(R.string.request_error));
                isPaying = false;
                if (mSpinKitProgressDialog != null && mSpinKitProgressDialog.isShowing())
                    mSpinKitProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                Pingpp.createPayment(PingPayActivity.this, response);
            }

            @Override
            public void onFinish(Call call)
            {
            }
        });
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        isPaying = false;
        if (mSpinKitProgressDialog != null && mSpinKitProgressDialog.isShowing())
            mSpinKitProgressDialog.dismiss();

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                KLog.e(result);
                KLog.e(errorMsg);
                KLog.e(extraMsg);
                if ("cancel".equals(result))
                {
                    errorMsg = "已取消此次充值";
                }
                else if ("success".equals(result))
                {
                    errorMsg = "充值成功";
                }
                else if ("fail".equals(result))
                {
                    errorMsg = "充值失败";
                }
                else if ("wx_app_not_installed".equals(errorMsg))
                {
                    errorMsg = "未安装微信";
                }
                showMsg("", errorMsg, extraMsg);
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2)
    {
        String str = title;
        if (null != msg1 && msg1.length() != 0)
        {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0)
        {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PingPayActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }
}

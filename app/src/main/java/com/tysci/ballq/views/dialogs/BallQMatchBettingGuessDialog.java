package com.tysci.ballq.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQUserGuessBettingTipOffActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchGuessBettingEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.MatchGuessBettingMoneyAdapter;
import com.tysci.ballq.views.widgets.LoadDataLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/22.
 */
public class BallQMatchBettingGuessDialog extends Dialog implements MatchGuessBettingMoneyAdapter.OnCheckItemListener, CompoundButton.OnCheckedChangeListener
,View.OnClickListener{
    private Window window;
    private Context context;
    private View view;
    private String tag=null;

    private GridView gvBettingMoneys;
    private MatchGuessBettingMoneyAdapter adapter;
    private List<String> bettingMoneys;
    private CheckBox checkBox;
    private TextView tvBettingResult;
    private TextView tvUserMoneys;
    private TextView tvProfit;
    private TextView tvTipInfo;
    private Button btCancle;
    private Button btOk;
    private LoadDataLayout loadDataLayout=null;
    private LinearLayout layoutBettingInfo=null;

    /**
     * 用户金币数
     */
    private float userMoneys;
    /**
     * 用户总投注数
     */
    private int allBettingMoneys = 0;

    private String bettingType;
    private BallQMatchGuessBettingEntity bettingInfo;
    private BallQMatchEntity ballQData = null;
    private float bettingPoint;
    private String bettingResult;
    private int bettingMoney = 0;

    private OnBettingClickListener onBettingClickListener = null;



    public BallQMatchBettingGuessDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window = this.getWindow();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initViews(context);
    }

    private void initViews(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_ballq_match_guess_betting, null);
        this.setContentView(view);

        gvBettingMoneys = (GridView) view.findViewById(R.id.gv_game_betting_moneys);
        bettingMoneys = Arrays.asList(context.getResources().getStringArray(R.array.betting_moneys));
        adapter = new MatchGuessBettingMoneyAdapter(context, bettingMoneys);
        adapter.setOnCheckItemListener(this);
        gvBettingMoneys.setAdapter(adapter);

        tvBettingResult = (TextView) view.findViewById(R.id.tv_game_betting_result);
        tvUserMoneys = (TextView) view.findViewById(R.id.tv_user_moneys);
        tvProfit = (TextView) view.findViewById(R.id.tv_game_betting_profit);
        tvTipInfo = (TextView) view.findViewById(R.id.tv_moneys_tip);

        checkBox = (CheckBox) view.findViewById(R.id.cb_mark);
        checkBox.setOnCheckedChangeListener(this);

        btCancle = (Button) view.findViewById(R.id.bt_cancel);
        btCancle.setOnClickListener(this);
        btOk = (Button) view.findViewById(R.id.bt_ok);
        btOk.setOnClickListener(this);

        loadDataLayout= (LoadDataLayout) view.findViewById(R.id.layout_loading_data);
        layoutBettingInfo=(LinearLayout)view.findViewById(R.id.layout_betting_guess_info);

        if (UserInfoUtil.isVIPUser(this.getContext())) {
            view.findViewById(R.id.layout_vip).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.layout_vip).setVisibility(View.GONE);
        }

        loadDataLayout.showLoading();
        getUserAccountInfo();

    }

    private void getUserAccountInfo(){
        String url= HttpUrls.HOST_URL_V5+"my_account/";
        HashMap<String,String> params=new HashMap<>(2);
        if (UserInfoUtil.checkLogin(context)) {
            params.put("user", UserInfoUtil.getUserId(context));
            params.put("token", UserInfoUtil.getUserToken(context));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                loadDataLayout.hideLoad();
                layoutBettingInfo.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        if(obj.getIntValue("status")==0){
                            JSONArray datas=obj.getJSONArray("data");
                            if(datas!=null&&!datas.isEmpty()){
                                JSONObject data=datas.getJSONObject(0);
                                if(data!=null&&!data.isEmpty()){
                                    userMoneys=data.getFloat("gold")/100;
                                    setUserGlods(userMoneys);

                                }
                            }
                        }

                    }
                }

            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    public void setOnBettingClickListener(OnBettingClickListener onBettingClickListener) {
        this.onBettingClickListener = onBettingClickListener;
    }

    public void setTag(String tag){
        this.tag=tag;
    }

    public void setBettingInfo(String bettingInfo) {
        bettingResult = bettingInfo;
        tvBettingResult.setText("您已选择:" + bettingInfo);
    }

    public void setUserGlods(float glods) {
        this.userMoneys = glods;
        tvUserMoneys.setText("(当前可用金币" + String.format(Locale.getDefault(),"%.2f",glods) + ")");
    }

    public void setBettingType(String type) {
        this.bettingType = type;
    }

    public void setBallQData(BallQMatchEntity data) {
        this.ballQData = data;
    }

    public void setBettingInfoType(BallQMatchGuessBettingEntity info, String type) {
        this.bettingType = type;
        this.bettingInfo = info;
        String data = info.getOdata();
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject obj = JSONObject.parseObject(data);
                String bettingValue = obj.getString(type);
                bettingPoint = Float.parseFloat(bettingValue);
                KLog.e("bettingPoint:" + bettingPoint);
                bettingPoint -= 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        checkBox.setChecked(false);
        adapter.reset();
        bettingInfo = null;
        bettingResult = null;
        bettingMoney = 0;
        bettingType = null;
        tvTipInfo.setVisibility(View.GONE);
        tvProfit.setText("预期收益:0.00");
    }

    public void addAllBettingMoneys(int moneys) {
        allBettingMoneys += moneys;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogWindowAttrs();
    }

    protected void setDialogWindowAttrs() {
        // TODO Auto-generated method stub
        Activity activity = (Activity) context;
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = window.getAttributes();
        //p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth() * 0.95);
        window.setAttributes(p);
    }

    @Override
    public void onCheckedItem(int postion, String value) {
        KLog.e("value:" + value);
        if (!TextUtils.isEmpty(value) && TextUtils.isDigitsOnly(value)) {
            bettingMoney = Integer.parseInt(value);
            if (userMoneys - allBettingMoneys < bettingMoney) {
                tvTipInfo.setVisibility(View.VISIBLE);
            } else {
                tvTipInfo.setVisibility(View.GONE);
                calculateBettingProfit(bettingMoney);
            }
        }
    }

    private void calculateBettingProfit(int bettingMoney) {
        float profit = bettingMoney * bettingPoint;
        tvProfit.setText("预期收益:" + String.format(Locale.getDefault(), "%.2f", profit));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            adapter.setCheckable(false);
            bettingMoney = 0;
        } else {
            adapter.setCheckable(true);
        }
    }

    private void onBettingClick() {
        boolean isVip = UserInfoUtil.isVIPUser(getContext());
        boolean isChecked = checkBox.isChecked();
        if (bettingMoney == 0 && !isVip) {
            ToastUtil.show(context, "请选择投注金额");
            return;
        }

        if (bettingMoney == 0 && isVip && !isChecked) {
            ToastUtil.show(context, "请选择投注金额");
            return;
        }

        boolean isNoGold = userMoneys - allBettingMoneys < bettingMoney;
        if (isNoGold && !isVip) {
            ToastUtil.show(context, "您金币不足，请充值");
            return;
        }

        if (isNoGold && isVip && !isChecked) {
            ToastUtil.show(context, "您金币不足，请充值");
            return;
        }

        allBettingMoneys += bettingMoney;
        BallQMatchGuessBettingEntity data = new BallQMatchGuessBettingEntity();
        data.setDataType(1);
        data.setId(bettingInfo.getId());
        data.setBettingInfo(bettingResult);
        data.setBettingMoney(bettingMoney);
        data.setOtype(bettingInfo.getOtype());
        data.setBettingType(bettingType);
        if (isVip) {
            Intent intent = new Intent(context, BallQUserGuessBettingTipOffActivity.class);
            intent.putExtra("betting_data", data);
            intent.putExtra("match_data", ballQData);
            intent.putExtra("is_tip",checkBox.isChecked());
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, 0x0001);
        } else {
            if (onBettingClickListener != null) {
                onBettingClickListener.onBettingClick(data);
            }
            dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_cancel) {
            dismiss();
        } else if (id == R.id.bt_ok) {
//            if(bettingMoney==0){
//                ToastUtil2.show(context,"请选择投注金额");
//                return;
//            }
//            if(userMoneys-allBettingMoneys<bettingMoney){
//                ToastUtil2.show(context,"您金币不足，请充值");
//                return;
//            }
//            allBettingMoneys+=bettingMoney;
//            BallQMatchGuessBettingEntity data=new BallQMatchGuessBettingEntity();
//            data.setDataType(1);
//            data.setId(bettingInfo.getId());
//            data.setBettingInfo(bettingResult);
//            data.setBettingMoney(bettingMoney);
//            data.setOtype(bettingInfo.getOtype());
//            data.setBettingType(bettingType);
//            if(UserProfileUtils.isUserVip()){
//                Intent intent=new Intent(context, BallQMatchBettingTipEditActivity.class);
//                context.startActivity(intent);
//            }else {
//                if (onBettingClickListener != null) {
//                    onBettingClickListener.onBettingClick(data);
//                }
//            }
//            dismiss();

            onBettingClick();
        }
    }

    public interface OnBettingClickListener {
        void onBettingClick(BallQMatchGuessBettingEntity info);
    }
}

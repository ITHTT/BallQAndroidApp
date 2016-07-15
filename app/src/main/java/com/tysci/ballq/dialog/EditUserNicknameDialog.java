package com.tysci.ballq.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CharacterUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-15 0015.
 * 修改昵称
 */
public class EditUserNicknameDialog extends BaseDialog {
    @Bind(R.id.edit_text_input)
    EditText nickname_new;

    private EditNicknameSuccessCallback callback;

    public EditUserNicknameDialog(Activity context) {
        super(context);
    }

    public void setCallback(EditNicknameSuccessCallback callback) {
        this.callback = callback;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_edit_user_nickname;
    }

    @Override
    protected void initializing(Bundle savedInstanceState) {
    }

    @OnClick(R.id.tv_cancel)
    public void onCancelClick(View view) {
        nickname_new.setText("");
        dismiss();
    }

    @OnClick(R.id.tv_commit)
    public void onCommitClick(View view) {
        checkNickname();
    }

    public String getNicknameNew() {
        if (nickname_new != null) {
            return nickname_new.getText().toString();
        }
        return "";
    }

    private void checkNickname() {
        String nicknameNew = getNicknameNew();
        if (TextUtils.isEmpty(nicknameNew)) {
            ToastUtil.show(getContext(), "昵称不能为空");
            return;
        }
        nicknameNew = nicknameNew.replaceAll("\n", "");
        nicknameNew = nicknameNew.replaceAll("", "");
        int i = 0;
        for (char c : nicknameNew.toCharArray()) {
            if (CharacterUtil.isChinese(c)) {
                i += 3;
            } else {
                i += 1;
            }
        }
        if (i > 30) {
            ToastUtil.show(getContext(), "昵称字符超过限制");
            return;
        }
        final String nicknameResult = CharacterUtil.filterEmoji(nicknameNew);
        final Context context = getContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(context));
        map.put("token", UserInfoUtil.getUserToken(context));
        map.put("nickname", nicknameResult);
        map.put("check_type", "nickname");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(TAG, HttpUrls.HOST_URL_V1 + "user/check_user_name/", null, map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(getContext(), "网络错误，请检查");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (object.getInteger("status") == 0 && object.getString("message").equalsIgnoreCase("ok")) {
                    nickname_new.setText(nicknameResult);
                    commitUpdateNickname(nicknameResult);
                } else {
                    ToastUtil.show(context, object.getString("message"));
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void commitUpdateNickname(final String nickname) {
        final Context context = getContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(context));
        map.put("token", UserInfoUtil.getUserToken(context));
        map.put("fname", nickname);

        HttpClientUtil.getHttpClientUtil().sendPostRequest(TAG, HttpUrls.HOST_URL_V5 + "user/edit_profile/", map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (object.getInteger("status") == 307) {
                    ToastUtil.show(getContext(), "昵称修改成功");
                    // TODO: 2016-07-01 修改昵称成功后续操作
                    if (callback != null) {
                        callback.callback(EditUserNicknameDialog.this, nickname);
                    }
                    dismiss();
                } else {
                    ToastUtil.show(getContext(), object.getString("message"));
                    dismiss();
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    public interface EditNicknameSuccessCallback {
        void callback(EditUserNicknameDialog dialog, String nicknameNew);
    }
}

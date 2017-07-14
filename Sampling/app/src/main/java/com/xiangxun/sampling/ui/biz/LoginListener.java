package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.ResultData.LoginData;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.ApiUrl;
import com.xiangxun.sampling.common.http.DcHttpClient;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 用户登录获取数据传递给了接口
 */
public class LoginListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {

    }

    @Override
    public void onStop(Dialog loading) {

    }

    public void onLogin(String name, String pass, String deviceId, final FrameListener<LoginData> listener) {


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
            listener.onFaild(0, "用户名密码不为空");
            return;
        }

        if (pass.contains(" ") && name.contains(" ")) {
            listener.onFaild(0, "用户名密码不能包含空格");
            return;
        }
        Pattern pattern = Pattern
                .compile("([^\\._\\w\\u4e00-\\u9fa5])*");
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            listener.onFaild(0, "用户名不能包含表情");
            return;
        }


        String url = ApiUrl.login(XiangXunApplication.getInstance());
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("account", name);
        params.put("pwd", pass);
        params.put("imei", deviceId);
//        params.put("crc", getCRC(params));
        DcHttpClient.getInstance().postWithURL(XiangXunApplication.getInstance(), url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (null != object) {
                    DLog.json(object.toString());
                    try {
                        LoginData loginData = Utils.getGson().fromJson(object.toString(), LoginData.class);
                        listener.onSucces(loginData);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        listener.onFaild(0, "解析异常！");
                    }
                } else {

                    listener.onFaild(0, "登录失败，请检查网络！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                listener.onFaild(0, "登录失败，请检查网络！");
            }
        });

    }

    public interface LoginInterface extends FrameView {

        void onLoginSuccess();

        void onLoginFailed(String info);

        String getUserName();

        String getPassword();

        void end();
    }


}

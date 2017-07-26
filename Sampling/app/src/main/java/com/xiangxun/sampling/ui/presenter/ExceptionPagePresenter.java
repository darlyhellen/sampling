package com.xiangxun.sampling.ui.presenter;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.image.BitmapChangeUtil;
import com.xiangxun.sampling.common.retrofit.paramer.ExceptionPageParamer;
import com.xiangxun.sampling.ui.biz.ExceptionPageListener;
import com.xiangxun.sampling.ui.main.SamplingExPageActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 地塊異常上傳
 */
public class ExceptionPagePresenter {

    private String TAG = getClass().getSimpleName();
    private ExceptionPageListener userBiz;
    private ExceptionPageListener.ExceptionPageInterface main;
    private LoadDialog loading;

    public ExceptionPagePresenter(ExceptionPageListener.ExceptionPageInterface main) {
        this.main = main;
        this.userBiz = new ExceptionPageListener();
        loading = new LoadDialog((SamplingExPageActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 上传异常地块信息
     */
    public void addException() {
        userBiz.onStart(loading);
        if (TextUtils.isEmpty(main.getLatitude())) {
            ToastApp.showToast("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getLatitude())) {
            ToastApp.showToast("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getLongitude())) {
            ToastApp.showToast("纬度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getLandid())) {
            ToastApp.showToast("地块ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getDeclare())) {
            ToastApp.showToast("说明信息不能为空");
            return;
        }
        ExceptionPageParamer paramer = new ExceptionPageParamer();
        paramer.setLatitude(main.getLatitude());
        paramer.setLongitude(main.getLongitude());
        paramer.setLandid(main.getLandid());
        paramer.setDeclare(main.getDeclare());
        JSONObject ob = new JSONObject();
        try {
            if (main.getImages() != null && main.getImages().size() > 1) {
                main.getImages().remove(main.getImages().size() - 1);
                for (String arg : main.getImages()) {
                    ob.put(arg.substring(arg.lastIndexOf("/"), arg.length()), BitmapChangeUtil.convertIconToString(BitmapFactory.decodeFile(arg)));
                }
            }
        } catch (JSONException e) {

        }
        paramer.setImages(ob.toString());
        userBiz.addException(paramer, new FrameListener<HisExceptionInfo>() {
            @Override
            public void onSucces(HisExceptionInfo data) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onDateSuccess(data.result);
            }

            @Override
            public void onFaild(int code, String info) {
                main.setEnableClick();
                main.onDateFailed(info);
            }
        });
    }
}
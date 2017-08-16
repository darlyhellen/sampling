package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.biz.ExceptionPageListener;
import com.xiangxun.sampling.ui.main.SamplingExPageActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


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

        if (TextUtils.isEmpty(main.getLatitude())) {
            ToastApp.showToast("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getLatitude())) {
            ToastApp.showToast("纬度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getLongitude())) {
            ToastApp.showToast("经度不能为空");
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
        //构建body
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("latitude", main.getLatitude())
                .addFormDataPart("longitude", main.getLongitude())
                .addFormDataPart("landBlockId", main.getLandid())
                .addFormDataPart("describe", main.getDeclare());

        if (main.getImages() != null && main.getImages().size() > 1) {
            main.getImages().remove(main.getImages().size() - 1);
            for (String arg : main.getImages()) {
                File file = new File(arg);
                builder.addFormDataPart("images", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        RequestBody requestBody = builder.build();
        userBiz.onStart(loading);
        userBiz.addException(requestBody, new FrameListener<HisExceptionInfo>() {
            @Override
            public void onSucces(HisExceptionInfo data) {
                File root = new File(Api.SENCE.concat("exceptionimage"));
                RecursionDeleteFile(root);
                userBiz.onStop(loading);
                main.onDateSuccess(data.result);
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
                main.onDateFailed(info);
                ToastApp.showToast(info);
            }
        });
    }

    /**
     * @param file TODO：删除文件夹下所有文件。
     */
    public void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
}
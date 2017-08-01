package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.orm.SugarRecord;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SamplingDBListener;
import com.xiangxun.sampling.ui.biz.SamplingDBListener.SamplingDBInterface;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 本地数据库展示
 */
public class SamplingDBPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingDBListener biz;
    private SamplingDBInterface view;
    private LoadDialog loading;

    public SamplingDBPresenter(SamplingDBInterface view) {
        this.view = view;
        this.biz = new SamplingDBListener();
        loading = new LoadDialog((BaseActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void upAll(final String id, final String pointId) {
        if (TextUtils.isEmpty(id)) {
            ToastApp.showToast("上传id不能为空");
            return;
        }
        final List<SenceSamplingSugar> sence = SugarRecord.find(SenceSamplingSugar.class, "samplingId = ?", id);
        final List<MediaSugar> sugar = SugarRecord.find(MediaSugar.class, "samplingId = ?", id);
        //构建body
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("", id, RequestBody.create(MediaType.parse("image/*"), ""));
        if (sugar != null && sugar.size() != 0) {
            for (MediaSugar media : sugar) {
                File file = new File(media.getUrl());
                if ("image".equals(media.getType())) {
                    builder.addFormDataPart(media.getType(), id + "@" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                }
                if ("video".equals(media.getType())) {
                    builder.addFormDataPart(media.getType(), id + "@" + file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
                }
            }
        } else {
            ToastApp.showToast("上传图片和视频不能为空");
            return;
        }
        RequestBody body = builder.build();
        biz.onStart(loading);
        biz.upAll(body, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                biz.onStop(loading);
                //上传成功后，删除本地数据库信息
                for (MediaSugar media : sugar) {
                    media.delete();
                }
                for (SenceSamplingSugar media : sence) {
                    media.delete();
                }
                RecursionDeleteFile(new File(Api.SENCE.concat(pointId)));
                view.onUpSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                view.onUpFailed();
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
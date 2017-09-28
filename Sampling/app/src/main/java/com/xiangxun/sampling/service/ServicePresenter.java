package com.xiangxun.sampling.service;

import com.orm.SugarRecord;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SamplingDBListener;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 本地数据库展示
 */
public class ServicePresenter {

    private SamplingDBListener biz;

    public ServicePresenter() {
        this.biz = new SamplingDBListener();
    }

    public void serviceUpAll() {
        final List<MediaSugar> sugar = SugarRecord.listAll(MediaSugar.class);
        //构建body
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (MediaSugar media : sugar) {
            File file = new File(media.getUrl());
            if ("image".equals(media.getType())) {
                builder.addFormDataPart(media.getType(), media.getSamplingId() + "@" + media.getSamplingCode() + "@" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            if ("video".equals(media.getType())) {
                builder.addFormDataPart(media.getType(), media.getSamplingId() + "@" + media.getSamplingCode() + "@" + file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
            }
        }
        RequestBody body = builder.build();
        biz.upAll(body, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                //上传成功后，删除本地数据库信息
                SugarRecord.deleteAll(MediaSugar.class);
                SugarRecord.deleteAll(SenceSamplingSugar.class);
                //删除文件信息
                RecursionDeleteFile(new File(Api.VIDEO));
                ToastApp.showToast("全部信息上传完成");
            }

            @Override
            public void onFaild(int code, String info) {
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
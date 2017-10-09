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


    public void upAll(final List<String> ids, final boolean isAll) {
        if (ids == null){
            ToastApp.showToast("上传id不能为空");
            return;
        }
        if (ids.size() == 0){
            ToastApp.showToast("上传id不能为空");
            return;
        }
        //构建body
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("", "", RequestBody.create(MediaType.parse("image/*"), ""));
        for (String id:ids) {
            if (TextUtils.isEmpty(id)){
                //假如中间一个id为空，则跳出继续下个id查找。
                continue;
            }
            List<MediaSugar> sugar = SugarRecord.find(MediaSugar.class, "samplingId = ?", id);
            if (sugar != null && sugar.size() != 0) {
                for (MediaSugar media : sugar) {
                    File file = new File(media.getUrl());
                    if ("image".equals(media.getType())) {
                        builder.addFormDataPart(media.getType(), id + "@" + media.getSamplingCode() + "@" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    }
                    if ("video".equals(media.getType())) {
                        builder.addFormDataPart(media.getType(), id + "@" + media.getSamplingCode() + "@" + file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
                    }
                }
            } else {
                //这个采样记录没有图片和视频信息。
                continue;
            }
        }
        RequestBody body = builder.build();
        biz.onStart(loading);
        biz.upAll(body, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                if (isAll){
                    //上传成功后，删除本地数据库信息
                    SugarRecord.deleteAll(MediaSugar.class);
                    SugarRecord.deleteAll(SenceSamplingSugar.class);
                    //删除文件信息
                    RecursionDeleteFile(new File(Api.VIDEO));
                }else {
                    //上传成功后，删除本地数据库信息
                    for (String id : ids) {
                        if (TextUtils.isEmpty(id)) {
                            //假如中间一个id为空，则跳出继续下个id查找。
                            continue;
                        }
                        List<MediaSugar> sugar = SugarRecord.find(MediaSugar.class, "samplingId = ?", id);
                        //上传成功后，删除图片数据库里面的信息。
                        if (sugar != null && sugar.size() != 0) {
                            for (MediaSugar media : sugar) {
                                media.delete();
                            }
                        }
                        List<SenceSamplingSugar> sence = SugarRecord.find(SenceSamplingSugar.class, "samplingId = ?", id);
                        if (sence != null && sence.size() != 0) {
                            for (SenceSamplingSugar media : sence) {
                                media.delete();
                            }
                        }
                    }
                }
                biz.onStop(loading);
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
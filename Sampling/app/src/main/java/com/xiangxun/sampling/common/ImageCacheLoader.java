package com.xiangxun.sampling.common;


import com.xiangxun.sampling.bean.PhotoInfo;
import com.xiangxun.sampling.common.http.DcHttpClient;

/**
 * @package: com.huatek.api.utils
 * @ClassName: ImageCacheLoader
 * @Description: 从本地加载图片工具
 * @author: aaron_han
 * @data: 2015-1-27 下午4:01:14
 */
public class ImageCacheLoader {
    private static ImageCacheLoader loader;

    public static ImageCacheLoader getInstance() {
        if (loader == null) {
            loader = new ImageCacheLoader();
        }
        return loader;
    }

    public void getLocalImage(String filePath, LocalNetWorkView lv, GetLocalCallBack gCallBack, boolean isFlag) {
        if (filePath == null) {
            return;
        }
        lv.filePath = filePath;
        lv.isFlag = isFlag;
        lv.setGCallBack(gCallBack);
        DcHttpClient.getInstance().mLocalQueue.put(lv);
    }

    // 上传图片方法
    public void upLoadImage(PhotoInfo photoInfo, UpLoadCallBack uCallBack) {
        photoInfo.setUCallBack(uCallBack);
        DcHttpClient.getInstance().mUpLoadQueue.put(photoInfo);
    }

    public interface GetLocalCallBack {
        void localSuccess(Object o);

        void localFalse(Object o);
    }

    // 上传是否成功接口回调
    public interface UpLoadCallBack {
        void upSuccess(Object o);

        void upFalse(Object o);
    }

}
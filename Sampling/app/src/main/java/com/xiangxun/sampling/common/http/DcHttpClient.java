package com.xiangxun.sampling.common.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xiangxun.sampling.common.BitmapCache;
import com.xiangxun.sampling.common.LocalQueue;
import com.xiangxun.sampling.common.UpLoadImageQueue;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.vollynet.Network;
import com.xiangxun.vollynet.Request;
import com.xiangxun.vollynet.RequestQueue;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;
import com.xiangxun.vollynet.toolbox.BasicNetwork;
import com.xiangxun.vollynet.toolbox.DiskBasedCache;
import com.xiangxun.vollynet.toolbox.HurlStack;
import com.xiangxun.vollynet.toolbox.ImageLoader;
import com.xiangxun.vollynet.toolbox.ImageLoader.ImageCache;
import com.xiangxun.vollynet.toolbox.ImageLoader.ImageListener;
import com.xiangxun.vollynet.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @package: com.huatek.api.common
 * @ClassName: DcHttpClient
 * @Description: 联网控制器 添加联网请求 以及图片缓存处理工具
 * @author: aaron_han
 * @data: 2015-1-16 下午4:46:42
 */

public class DcHttpClient {
    private static DcHttpClient dcHttpClient;
    public RequestQueue mRequestQueue;// 请求队列
    public BitmapCache mBitmapCache = null;// 网络下载图片缓存容器
    public ImageLoader mImageLoader = null;// 网络图片下载器
    public LocalQueue mLocalQueue;// 本地加载图片队列
    public UpLoadImageQueue mUpLoadQueue;// 本地上传队列
    public Context con;

    public static DcHttpClient getInstance() {
        if (dcHttpClient == null) {
            dcHttpClient = new DcHttpClient();
        }
        return dcHttpClient;
    }

    private DcHttpClient() {
        super();
    }

    /**
     * @throws
     * @Title:
     * @Description: 初始化HttpCLient
     * @param: @param con
     * @return: void
     */
    public void init(Context con) {
        this.con = con;
        File sdDir = Environment.getExternalStorageDirectory();
        File file = new File(sdDir, "Sampling/cache");
        DiskBasedCache cache = new DiskBasedCache(file, 20 * 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mBitmapCache = BitmapCache.getInstance();
        mLocalQueue = new LocalQueue(mBitmapCache, con);
        mUpLoadQueue = new UpLoadImageQueue();
        mImageLoader = new ImageLoader(mRequestQueue, (ImageCache) mBitmapCache);
        mLocalQueue.start();
        mRequestQueue.start();
        mUpLoadQueue.start();
    }

    /**
     * @throws
     * @Title:
     * @Description: 关闭网络相关线程
     * @param:
     * @return: void
     */
    public void stop() {
        if (mLocalQueue != null) {
            mLocalQueue.stop();
        }
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
        if (mUpLoadQueue != null) {
            mUpLoadQueue.stop();
        }
    }

    /**
     * @throws
     * @Title:
     * @Description:清理硬盘内存
     * @param:
     * @return:
     */
    public void cleanDiskCache() {
        mRequestQueue.getCache().clear();
    }

    /**
     * @throws
     * @Title:
     * @Description:清理应用内存缓存
     * @param:
     * @return:
     */
    public void cleanLruCache() {
        mBitmapCache.clean();
    }

    /**
     * @throws
     * @Title:
     * @Description: 断开一条请求
     * @param: @param tag
     * @return: void
     */
    public void cancelRequest(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    // 添加一个request请求
    public void addRequest(Request<?> req) {
        mRequestQueue.add(req);
    }

    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded
     * encoded string.
     */
    private String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                if (!TextUtils.isEmpty(entry.getValue())) {
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                }
                encodedParams.append('&');
            }
            encodedParams.deleteCharAt(encodedParams.length() - 1);
            DLog.i(getClass().getSimpleName(), encodedParams.toString());
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     * @Description: get请求
     * @return: void
     */
    public void getWithURL(Context context, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        if (params != null && params.size() > 0) {
            if (url.contains("?")) {
                url = url + "&" + encodeParameters(params, "UTF-8");
            } else {
                url = url + "?" + encodeParameters(params, "UTF-8");
            }
        }
        DcJsonRequest jsObjRequest = new DcJsonRequest(context, Request.Method.GET, url, params, listener, errorListener);
        jsObjRequest.setShouldCache(false);
        this.addRequest(jsObjRequest);
    }

    /**
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     * @param isCahe
     * @Description: 缓存
     * @return: void
     */
    public void getWithURLOrCache(Context context, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean isCahe) {
        if (params != null && params.size() > 0) {
            if (url.contains("?")) {
                url = url + "&" + encodeParameters(params, "UTF-8");
            } else {
                url = url + "?" + encodeParameters(params, "UTF-8");
            }
        }
        DcJsonRequest jsObjRequest = new DcJsonRequest(context, Request.Method.GET, url, params, listener, errorListener);
        jsObjRequest.setShouldCache(isCahe);
        this.addRequest(jsObjRequest);
    }

    /**
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     * @Description: post请求
     * @return: void
     */
    public void postWithURL(Context context, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        DcJsonRequest jsObjRequest = new DcJsonRequest(context, Request.Method.POST, url, params, listener, errorListener);
        jsObjRequest.setShouldCache(false);
        this.addRequest(jsObjRequest);
    }

    /**
     * @param url
     * @param imageView
     * @param id
     * @Description: 图片联网请求
     * @return: void
     */
    public void getImageForNIView(String url, NetworkImageView imageView, int id) {
        imageView.setDefaultImageResId(id);
        imageView.setErrorImageResId(id);
        imageView.setImageUrl(url, mImageLoader);
    }

    /**
     * @param niv
     * @param imgUrl
     * @param resId
     * @param responseImageListener
     * @Description: 请求图片 设置事件监听
     * @return: void
     */
    public void requestImage(final ImageView niv, final String imgUrl, final int resId, final ResponseImageListener responseImageListener) {
        // if (TextUtils.isEmpty(imgUrl)) {
        // niv.setImageResource(resId);
        // return;
        // }
        if (responseImageListener != null)
            responseImageListener.onLoadingStarted();
        niv.setImageResource(resId);
        mImageLoader.get(imgUrl, new ImageListener() {
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                niv.setImageBitmap(response.getBitmap());
                if (responseImageListener != null)
                    responseImageListener.onLoadingComplete(response, isImmediate);
            }

            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                niv.setImageResource(resId);
                if (responseImageListener != null)
                    responseImageListener.onLoadingFailed(error);
            }
        });
    }

    /**
     * @param niv
     * @param imgUrl
     * @param resId
     * @Description: 请求图片 设置事件监听
     * @return: void
     */
    public void requestImage(final ImageView niv, final String imgUrl, final int resId) {
        niv.setImageResource(resId);
        mImageLoader.get(imgUrl, new ImageListener() {
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                niv.setImageBitmap(response.getBitmap());
            }

            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                niv.setImageResource(resId);
            }
        });
    }

    public void requestImageScaled(final ImageView niv, final String imgUrl, final int resId) {
        niv.setImageResource(resId);
        mImageLoader.get(imgUrl, new ImageListener() {
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bmp = response.getBitmap();
                if (bmp != null) {
                    Bitmap output = Bitmap.createScaledBitmap(bmp, 640, 480, true);
                    niv.setImageBitmap(output);
                } else {
                    niv.setImageResource(resId);
                }
            }

            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                niv.setImageResource(resId);
            }
        });
    }


    /**
     * @package: com.huatek.api.common
     * @ClassName: ResponseImageListener
     * @Description: 图片下载监听接口
     * @author: aaron_han
     * @data: 2015-1-29 上午11:28:08
     */
    public interface ResponseImageListener {
        public void onLoadingStarted();

        public void onLoadingFailed(VolleyError error);

        public void onLoadingComplete(ImageLoader.ImageContainer response, boolean isImmediate);
    }

}
package com.xiangxun.video.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * @author 张宇辉 zhangyuhui@octmami.com
 * @ClassName: ToastApp
 * @Description: TODO(Toast统一管理类)
 * @date 2014年10月24日 下午3:08:55
 */
public class ToastApp {

    private static final int TOASTTIME = Toast.LENGTH_SHORT;

    private static Toast toast = null;

    private static ToastApp instance = null;

    private boolean show = true;

    private static Context context;

    private ToastApp() {
    }

    public static ToastApp getInstance() {
        if (instance == null) {
            instance = new ToastApp();
        }
        return instance;
    }

    public void init(Context mcontext) {
        context = mcontext;
    }

    public boolean isShow() {
        return show;
    }


    /**
     * @auther Darly Fronch 2015 下午2:25:56 TODO显示Toast
     */
    public static void showToast(Context context, String msg) {
        if (!getInstance().isShow())
            return;

        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(Context context, int msg) {
        if (!getInstance().isShow())
            return;

        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(String msg) {
        if (!getInstance().isShow())
            return;

        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(int msg) {
        if (!getInstance().isShow())
            return;

        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * @auther Darly Fronch 2015 下午2:25:56 TODO显示Toast
     */
    public static void mustShow(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void mustShow(Context context, int msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void mustShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void mustShow(int msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, TOASTTIME);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
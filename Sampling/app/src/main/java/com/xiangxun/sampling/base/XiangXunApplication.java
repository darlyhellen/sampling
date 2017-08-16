package com.xiangxun.sampling.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.orm.SugarContext;
import com.xiangxun.sampling.BuildConfig;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.service.MainService;
import com.xiangxun.video.camera.VCamera;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class XiangXunApplication extends Application {
    private MainService mService;
    private Camera mCamera;
    private static final String lOCALE_CHANGED = Intent.ACTION_LOCALE_CHANGED;

    private String VideoServerIp;
    private int VideoServerPort;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    private static XiangXunApplication sApplication;
    private String mDevId;
    private ExecutorService mThreadPool;

    @Override
    public void onCreate() {
        sApplication = this;
        super.onCreate();
        SugarContext.init(this);
        Intent sintent = new Intent(this, MainService.class);
        bindService(sintent, conn, Service.BIND_AUTO_CREATE);
        registerReceiver(mReceiver, new IntentFilter(lOCALE_CHANGED));
        // 网络对象初始化
        ImageLoaderUtil.init(this);
        if (SystemCfg.getWidth(this) == 0 || SystemCfg.getHeight(this) == 0) {
            calculate();
        }
        // 日志记录
        DLog.init(BuildConfig.DEBUG, "sampling");

        //  VCamera.setVideoCachePath(FileUtils.getRecorderPath());
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(false);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
        //定位功能模块初始化
        LocationTools.init(this);
    }

    public static XiangXunApplication getInstance() {
        return sApplication;
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MainService.mService) service).getService();
        }
    };

    public MainService getMainService() {
        if (mService != null) {
            return mService;
        }
        throw new RuntimeException("MainService should be created before accessed");
    }


    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    public String getUserName() {
        return SystemCfg.getUserName(getInstance());
    }

    public String getUserId() {
        return SystemCfg.getUserId(getInstance());
    }


    public String getDevId() {
        if (mDevId == null) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mDevId = tm.getDeviceId();
        }
        return mDevId;
    }

    public ExecutorService getThreadPool() {

        if (mThreadPool == null) {
            mThreadPool = Executors.newSingleThreadExecutor();
        }
        return mThreadPool;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(lOCALE_CHANGED)) {
                // Permissions.reInitPermissions(getInstance());
            }
        }
    };


    /**
     * 计算屏幕宽高以及后续辅助参数。都可以在这里进行完善
     */
    private void calculate() {
        WindowManager wm = (WindowManager) getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        SystemCfg.setWidth(getInstance(), wm.getDefaultDisplay().getWidth());
        SystemCfg.setHeight(getInstance(), wm.getDefaultDisplay().getHeight());
    }


    public static void createFiles() {
        File root = new File(Api.Root);
        if (!root.exists()) {
            root.mkdir();
        }
        //初始化视频录制功能参数。
        File boot = new File(Api.VIDEO);
        if (!boot.exists()) {
            boot.mkdir();
        }
        File sence = new File(Api.SENCE);
        if (!sence.exists()) {
            sence.mkdir();
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String curVersionName = null;
        try {
            PackageManager pm = sApplication.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(sApplication.getPackageName(), 0);
            curVersionName = pi.versionName;
        } catch (Exception e) {
        }
        return curVersionName;
    }

    public int getVersionCode() {
        int version = 0;

        try {
            PackageInfo e = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = e.versionCode;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return version;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //数据库Sugar关闭
        SugarContext.terminate();
    }
}

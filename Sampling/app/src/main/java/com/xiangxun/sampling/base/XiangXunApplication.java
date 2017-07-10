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
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.WindowManager;


import com.xiangxun.sampling.BuildConfig;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.Api;
import com.xiangxun.sampling.common.http.DcHttpClient;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
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

        Intent sintent = new Intent(this, MainService.class);
        bindService(sintent, conn, Service.BIND_AUTO_CREATE);
        registerReceiver(mReceiver, new IntentFilter(lOCALE_CHANGED));
        // 网络对象初始化
        DcHttpClient.getInstance().init(getBaseContext());
        ImageLoaderUtil.init(this);
        createFiles();
        if (SystemCfg.getWidth(this) == 0 || SystemCfg.getHeight(this) == 0) {
            calculate();
        }
        // 日志记录
        DLog.init(BuildConfig.DEBUG, "sampling");
        //初始化视频录制功能参数。
        File boot = new File(Api.Root + "/video/");
        if (!boot.exists()) {
            boot.mkdir();
        }
        VCamera.setVideoCachePath(boot + "/recoder/");
        //  VCamera.setVideoCachePath(FileUtils.getRecorderPath());
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(false);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);


//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()//
//                .detectCustomSlowCalls()//
//                .detectDiskReads()//
//                .detectDiskWrites()//
//                .detectNetwork()//
//                .penaltyLog()//
//                .penaltyFlashScreen()//
//                .build());
//        try {
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()//
//                    .detectLeakedClosableObjects()//
//                    .detectLeakedSqlLiteObjects()//
//                    .setClassInstanceLimit(Class.forName("com.apress.proandroid.SomeClass"), 100)//
//                    .penaltyLog()//
//                    .build());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


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


    public void createFiles() {
        File boot = new File(Api.Root);
        if (!boot.exists()) {
            boot.mkdir();
        }
    }
}

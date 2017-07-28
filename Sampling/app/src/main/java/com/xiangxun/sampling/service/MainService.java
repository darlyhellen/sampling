package com.xiangxun.sampling.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.orm.SugarRecord;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.ui.presenter.SamplingDBPresenter;

import java.util.List;


public class MainService extends Service {
    private Thread tSendData;

    private mService mBinder = new mService();

    private ServicePresenter presenter;

    public class SendThread implements Runnable {
        public void run() {
            List<MediaSugar> data = SugarRecord.listAll(MediaSugar.class);
            //调用接口进行数据传输
            //有数据才进行接口请求，否则无法封装成RequestBody
            if (data!=null&&data.size()>0) {
                DLog.i(getClass().getSimpleName(), "查找到WIFI环境，直接传递所有现场采样数据。" + data);
                presenter.serviceUpAll();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    public class mService extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void start() {
        presenter = new ServicePresenter();
        if (tSendData != null && tSendData.isAlive()) {
            tSendData.interrupt();
            tSendData = null;
        }
        if (NetUtils.isWifi(this)) {
            tSendData = new Thread(new SendThread());
            tSendData.setName("SendThread");
            tSendData.start(); // 联网发送数据
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}

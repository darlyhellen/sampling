package com.xiangxun.sampling.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.db.SenceSamplingSugar;

import java.util.List;


public class MainService extends Service {
    private Thread tSendData;

    private mService mBinder = new mService();

    public class SendThread implements Runnable {
        public void run() {
            List<SenceSamplingSugar> data = SugarRecord.listAll(SenceSamplingSugar.class);
            //调用接口进行数据传输
            DLog.i(getClass().getSimpleName(), "接口起吊，传递大数据" + data);
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

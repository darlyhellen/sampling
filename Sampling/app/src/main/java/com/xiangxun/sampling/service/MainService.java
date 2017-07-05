package com.xiangxun.sampling.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;


public class MainService extends Service {
	private Thread tSendData;
	//private int isFlag = ConstantStatus.UPLOAD_ALLDATA;
	private recver mrecver;

	private class recver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			MainService.this.startActivity(arg1);
		}

	}

	private mService mBinder = new mService();

	public class SendThread implements Runnable {
		//DataManager dm = new DataManager(MainService.this);;
		public void run() {
			try {
				if (true) {
//					switch (isFlag) {
//					case ConstantStatus.UPLOAD_GPSDATA:
//					case ConstantStatus.UPLOAD_VIODATA:
//					case ConstantStatus.UPLOAD_FIELDPUNISHDATA:
//					case ConstantStatus.UPLOAD_ENFORCEMENTDATA:
//					case ConstantStatus.UPLOAD_DUTYDATA:
//					case ConstantStatus.UPLOAD_ACCIDENTDATA:
//					case ConstantStatus.UPLOAD_LAWCHECKDATA:
//					case ConstantStatus.UPLOAD_WARNACKDATA:
//					case ConstantStatus.UPLOAD_ALLDATA:
//					case ConstantStatus.UPLOAD_CHANGE_INFO:
//						// 上传GPS信息
////						int countGPSAll = DBManager.getInstance().getUnUpGpsCount();
////						if (countGPSAll > 0) {
////							dm.upGpsData();
////						}
//						// 上传事故信息
//						int countAccidentAll = DBManager.getInstance().getUnUpAccidentCount();
//						if (countAccidentAll > 0) {
//							dm.upAccidentData();
//						}
//						// 上传隐患信息
//						int countDangerAll = DBManager.getInstance().getUnUpDangerCount();
//						if (countDangerAll > 0) {
//							dm.upDangerData();
//						}
//						// 上传违法信息
//						int countIllegalAll = DBManager.getInstance().getUnUpIllegalCount();
//						if (countIllegalAll > 0) {
//							dm.upIllegalData();
//						}
//						//整改跟踪上传
//						int changeResult = DBManager.getInstance().getUnUpChangeInfo();
//						if (changeResult > 0) {
//							dm.upChangeInfiData();
//						}
//						//卷宗上传
//						int  changeFile = DBManager.getInstance().getUnUpFileInfo();
//						if ( changeFile > 0) {
//							dm.upFileData();
//						}
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_REDELIVER_INTENT; // ��kill����������������
	}

	public class mService extends Binder {
		public MainService getService() {
			return MainService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mrecver = new recver();
		this.registerReceiver(mrecver, new IntentFilter("rebukview"));
		tSendData = new Thread(new SendThread());
		tSendData.setName("SendThread");
		tSendData.start(); // 联网发送数据
	}

	public void start(int isFlag) {
		//this.isFlag = isFlag;
		if (tSendData != null && tSendData.isAlive()) {
			tSendData.interrupt();
			tSendData = null;
		}
		tSendData = new Thread(new SendThread());
		tSendData.setName("SendThread");
		tSendData.start();
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(mrecver);
		super.onDestroy();
		//DBManager.getInstance().close();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void clearGPSLimitData() {
//		DataManager dm = new DataManager(MainService.this);
//		dm.clearGPSLimitData();
	}

}

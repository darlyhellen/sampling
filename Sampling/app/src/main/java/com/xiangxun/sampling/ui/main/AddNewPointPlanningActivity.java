package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 新增修改计划中的点位信息。
 */
@ContentBinder(R.layout.activity_planning_add)
public class AddNewPointPlanningActivity extends BaseActivity implements AMapLocationListener {
    private SamplingPlanning planning;
    private SamplingPoint point;

    @ViewsBinder(R.id.id_add_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_add_projectname)
    private DetailView name;
    @ViewsBinder(R.id.id_add_projecdept)
    private DetailView dept;
    @ViewsBinder(R.id.id_add_projectposition)
    private DetailView position;
    @ViewsBinder(R.id.id_add_latitude)
    private DetailView latitude;
    @ViewsBinder(R.id.id_add_longitude)
    private DetailView longitude;
    @ViewsBinder(R.id.id_add_desc)
    private DetailView desc;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        point = (SamplingPoint) getIntent().getSerializableExtra("SamplingPoint");


    }

    @Override
    protected void loadData() {
        name.isEdit(false);
        name.setInfo("计划名称：", planning.getTitle(),"");
        dept.isEdit(false);
        dept.setInfo("实施机构：", planning.getDepate(),"");
        position.isEdit(false);
        position.setInfo("采样选址：", planning.getPlace(),"");
        if (point == null) {
            //新增点位
            titleView.setTitle("新增" + planning.getTitle() + "点位");
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            //启动定位
            mlocationClient.startLocation();

        } else {
            //修改点位
            titleView.setTitle("修改" + planning.getTitle() + "点位");
            latitude.isEdit(true);
            latitude.setInfo("经度：", String.valueOf(point.getLatitude()),"");
            longitude.isEdit(true);
            longitude.setInfo("纬度：", String.valueOf(point.getLongitude()),"");
            desc.isEdit(true);
            desc.setInfo("说明：", point.getDesc(),"");
        }
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        titleView.setRightViewRightTextOneListener("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交服务端,进行重新获取.
            }
        });
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(amapLocation.getLatitude()),"");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(amapLocation.getLongitude()),"");
                desc.isEdit(true);
                desc.setInfo("说明：", amapLocation.getAddress(),"");
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(0),"");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(0),"");
                desc.isEdit(true);
                desc.setInfo("说明：", " ","");
                ToastApp.showToast("请链接网络或者打开GPS进行定位");
            }
            mlocationClient.stopLocation();
        }
    }

    @Override
    protected void onStart() {
        DLog.d(getClass().getSimpleName(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        DLog.d(getClass().getSimpleName(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        if (mlocationClient != null && !mlocationClient.isStarted()) {
            mlocationClient.startLocation();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        DLog.d(getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DLog.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }


}

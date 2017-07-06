package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

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

import java.util.Iterator;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 新增修改计划中的点位信息。
 */
@ContentBinder(R.layout.activity_planning_add)
public class AddNewPointPlanningActivity extends BaseActivity {
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

    private LocationManager locationManager;
    private GpsStatus gpsstatus;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        point = (SamplingPoint) getIntent().getSerializableExtra("SamplingPoint");


    }

    @Override
    protected void loadData() {
        name.isEdit(false);
        name.setInfo("计划名称：", planning.getTitle());
        dept.isEdit(false);
        dept.setInfo("实施机构：", planning.getDepate());
        position.isEdit(false);
        position.setInfo("采样选址：", planning.getPlace());
        if (point == null) {
            //新增点位
            titleView.setTitle("新增" + planning.getTitle() + "点位");
            //获取到LocationManager对象
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //根据设置的Criteria对象，获取最符合此标准的provider对象
            String currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();

            //根据当前provider对象获取最后一次位置信息
            Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
            //如果位置信息为null，则请求更新位置信息
            if (currentLocation == null) {
                locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
            }
            //增加GPS状态监听器
            locationManager.addGpsStatusListener(gpsListener);

            //直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            currentLocation = locationManager.getLastKnownLocation(currentProvider);
            if (currentLocation != null) {
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(currentLocation.getLatitude()));
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(currentLocation.getLongitude()));
                desc.isEdit(true);
                desc.setInfo("说明：", " ");
            } else {
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(0));
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(0));
                desc.isEdit(true);
                desc.setInfo("说明：", " ");
            }
        } else {
            //修改点位
            titleView.setTitle("修改" + planning.getTitle() + "点位");
            latitude.isEdit(true);
            latitude.setInfo("经度：", String.valueOf(point.getLatitude()));
            longitude.isEdit(true);
            longitude.setInfo("纬度：", String.valueOf(point.getLongitude()));
            desc.isEdit(true);
            desc.setInfo("说明：", point.getDesc());
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
        titleView.setRightViewRightOneListener(R.mipmap.key_end, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交服务端,进行重新获取.
            }
        });
    }


    private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        //GPS状态发生变化时触发
        @Override
        public void onGpsStatusChanged(int event) {
            //获取当前状态
            gpsstatus = locationManager.getGpsStatus(null);
            switch (event) {
                //第一次定位时的事件
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    break;
                //开始定位的事件
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                //发送GPS卫星状态事件
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    ToastApp.showToast("GPS_EVENT_SATELLITE_STATUS");
                    Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();
                    Iterator<GpsSatellite> it = allSatellites.iterator();
                    int count = 0;
                    while (it.hasNext()) {
                        count++;
                    }
                    ToastApp.showToast("Satellite Count:" + count);
                    break;
                //停止定位事件
                case GpsStatus.GPS_EVENT_STOPPED:
                    DLog.d("Location", "GPS_EVENT_STOPPED");
                    break;
            }
        }
    };


    //创建位置监听器
    private LocationListener locationListener = new LocationListener() {
        //位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
            DLog.d("Location", "onLocationChanged");
        }

        //provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
            DLog.d("Location", "onProviderDisabled");
        }

        //provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
            DLog.d("Location", "onProviderEnabled");
        }

        //状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            DLog.d("Location", "onStatusChanged");
        }
    };
}

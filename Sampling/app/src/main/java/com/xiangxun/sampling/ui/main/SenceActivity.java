package com.xiangxun.sampling.ui.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.supermap.android.maps.CoordinateReferenceSystem;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Overlay;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 現場情況的展示頁面
 */
@ContentBinder(R.layout.activity_sence)
public class SenceActivity extends BaseActivity implements AMapLocationListener,OnClickListener {
    private static String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-ETuoKeQi/rest/maps/地区面@地区面";
    private SamplingPlanning planning;
    private SamplingPoint point;
    @ViewsBinder(R.id.id_user_sence_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_user_sence_mapview)
    protected MapView mapView;
    @ViewsBinder(R.id.id_user_sence_lat)
    private DetailView latitude;
    @ViewsBinder(R.id.id_user_sence_lont)
    private DetailView longitude;
    @ViewsBinder(R.id.id_user_sence_type)
    private DetailView type;
    @ViewsBinder(R.id.id_user_sence_name)
    private DetailView name;
    @ViewsBinder(R.id.id_user_sence_params)
    private DetailView params;
    @ViewsBinder(R.id.id_user_sence_project)
    private DetailView project;
    @ViewsBinder(R.id.id_user_sence_other)
    private DetailView other;
    @ViewsBinder(R.id.id_user_sence_location)
    private ImageView loca;


    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        point = (SamplingPoint) getIntent().getSerializableExtra("SamplingPoint");
        titleView.setTitle("现场采样");
        mapView.setLayoutParams(new TableRow.LayoutParams(SystemCfg.getWidth(this) / 3, SystemCfg.getWidth(this) / 3));
    }

    @Override
    protected void loadData() {
        type.isEdit(false);
        type.setInfo("采样类型:", point.getType()+" ");
        name.isEdit(false);
        name.setInfo("样品名称:", point.getName()+" ");
        params.isEdit(false);
        params.setInfo("样品深度:", point.getDeep()+" ");
        project.isEdit(false);
        project.setInfo("待测项目:", point.getProj()+" ");
        other.isEdit(true);
        other.setInfo("其他說明:", point.getNote()+" ");
        //啟動定位
        startLocate();
    }

    private void startChao(AMapLocation amapLocation) {
        LayerView baseLayerView = new LayerView(this);
        Point2D center = new Point2D(amapLocation.getLongitude(), amapLocation.getLatitude());
        baseLayerView.setURL(DEFAULT_URL);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        mapView.getController().setCenter(center);
        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(8);
        Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_point_select);
        DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
        OverlayItem overlayItem = new OverlayItem(center, point.getDesc(), point.getId());
        overlay.addItem(overlayItem);
        mapView.getOverlays().add(new CustomOverlay(center, amapLocation.getAddress()));
        mapView.getOverlays().add(overlay);

        // 重新onDraw一次
        mapView.invalidate();
    }



    /**
     * 自定义Overlay
     */
    class CustomOverlay extends Overlay {

        private Point2D center;
        private String title;

        public CustomOverlay(Point2D center, String title) {
            this.center = center;
            this.title = title;
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            Point point = mapView.getProjection().toPixels(center, null);
            paint.setTextSize(24);
            paint.setStrokeWidth(0.8f);
            paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText(title, point.x, point.y, paint);
        }
    }


    private void startLocate() {
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
    }

    @Override
    protected void initListener() {
        loca.setOnClickListener(this);
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_user_sence_location:
                startLocate();
                break;
        }
    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(amapLocation.getLatitude()));
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(amapLocation.getLongitude()));
                startChao(amapLocation);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(0));
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(0));
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

    @Override
    public void onBackPressed() {
        if (mapView != null) {
            mapView.destroy();
        }
        super.onBackPressed();
    }
}

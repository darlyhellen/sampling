package com.xiangxun.sampling.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.supermap.android.commons.EventStatus;
import com.supermap.android.maps.CoordinateReferenceSystem;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.measure.MeasureParameters;
import com.supermap.android.maps.measure.MeasureResult;
import com.supermap.android.maps.measure.MeasureService;
import com.supermap.services.components.commontypes.Point2D;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.biz.ChaoTuListener;
import com.xiangxun.sampling.ui.biz.ChaoTuListener.ChaoTuInterface;
import com.xiangxun.sampling.ui.biz.SamplingPointListener.SamplingPointInterface;
import com.xiangxun.sampling.ui.presenter.ChaoTuPresenter;
import com.xiangxun.sampling.ui.presenter.SamplingPointPresenter;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 使用超图地图展示点位信息。
 */
@ContentBinder(R.layout.activity_chaotu)
public class ChaoTuActivity extends BaseActivity implements SamplingPointInterface,LocationToolsListener,ChaoTuInterface {
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;
    private Scheme planning;
    private List<PlannningData.Pointly> data;

    private List<HisPlanningData.HisPoint> info;

    private com.supermap.android.maps.Point2D center;

    private SamplingPointPresenter presenter;
    private ChaoTuPresenter chaoTuPresenter;
    private  boolean isSence;



    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        baseLayerView.setURL(Api.getMalink());
        mapView.addLayer(baseLayerView);
        // 启用内置放大缩小控件
        //设置地图缩放
        //mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(3);
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        isSence = getIntent().getBooleanExtra("isSence",false);
        if (planning == null) {
            titleView.setTitle("点位分布");
        } else {

            if (isSence) {
                titleView.setTitle(planning.missionName + "点位分布");
                //在这里先查看是否有缓存文件，有缓存点位文件，进行展示，然后请求更新点位。
                chaoTuPresenter = new ChaoTuPresenter(this);
                chaoTuPresenter.point(planning.missionId,planning.sampleCode);
            }else {
                titleView.setTitle(planning.name + "点位分布");
                Object ob = SharePreferHelp.getValue(planning.id);
                if (data == null) {
                    if (ob != null) {
                        data = ((PlannningData.ResultPointData) ob).result;
                    }
                }
                //在这里先查看是否有缓存文件，有缓存点位文件，进行展示，然后请求更新点位。
                presenter = new SamplingPointPresenter(this);
                presenter.point(planning.id, ob == null ? null : ((PlannningData.ResultPointData) ob).resTime);
            }
        }

        if (!Api.TESTING) {
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
    }

    @Override
    protected void loadData() {

    }

    private void dats(AMapLocation location) {
        Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_sence_undown);
        Drawable drawablenormal = getResources().getDrawable(R.mipmap.ic_sence_down);
        Drawable userPostion = getResources().getDrawable(R.mipmap.ic_user_location);
        DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
        if (Api.TESTING){
            //测试定位
            center = new com.supermap.android.maps.Point2D(Api.longitude,Api.latitude);
            mapView.getController().setCenter(center);
            OverlayItem overlayItem = new OverlayItem(center, "", "");
            overlayItem.setMarker(userPostion);
            overlay.addItem(overlayItem);
        }else if (location!=null){
            //进行增加位置信息
            center = new com.supermap.android.maps.Point2D(location.getLongitude(),location.getLatitude());
            mapView.getController().setCenter(center);
            OverlayItem overlayItem = new OverlayItem(center, "", "");
            overlayItem.setMarker(userPostion);
            overlay.addItem(overlayItem);
        }
        if (isSence){
            if (info != null && info.size() != 0) {
                for (HisPlanningData.HisPoint point : info) {
                    com.supermap.android.maps.Point2D poind = new com.supermap.android.maps.Point2D(point.longitude, point.latitude);
                    OverlayItem overlayItem = new OverlayItem(poind, "", point.id);
                    overlayItem.setMarker(drawablenormal);
                    overlay.addItem(overlayItem);
                }
            }
        }else {
            if (data != null && data.size() != 0) {
                for (PlannningData.Pointly point : data) {
                    com.supermap.android.maps.Point2D poind = new com.supermap.android.maps.Point2D(point.data.longitude, point.data.latitude);
                    OverlayItem overlayItem = new OverlayItem(poind, "", point.data.id);
                    if (point.data.isSampling != 0) {
                        overlayItem.setMarker(drawablenormal);
                    } else {
                        overlayItem.setMarker(drawableBlue);
                    }
                    overlay.addItem(overlayItem);
                }
            }
        }
        mapView.getOverlays().add(overlay);
        mapView.invalidate();
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }


    //获取点位成功信息记录。
    @Override
    public void onLoginSuccess(List<PlannningData.Pointly> info) {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
        }
        dats(null);
    }

    @Override
    public void onLoginFailed() {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
        }
        dats(null);
    }

    @Override
    public void onUpSuccess() {

    }

    @Override
    public void onUpFailed() {

    }

    @Override
    public void onItemImageClick(PlannningData.Scheme planning,SenceSamplingSugar point, PlannningData.Point dats) {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        LocationTools.getInstance().stop();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.stopClearCacheTimer();// 停止和销毁 清除运行时服务器中缓存瓦片的定时器。
            mapView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        dats(amapLocation);
    }

    @Override
    public void locationFail() {

    }

    @Override
    public void onDateSuccess(List<HisPlanningData.HisPoint> info) {
        this.info = info;
        dats(null);
    }

    @Override
    public void onDateFailed(String info) {

    }
}

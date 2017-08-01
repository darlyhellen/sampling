package com.xiangxun.sampling.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

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
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.biz.SamplingPointListener.SamplingPointInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPointPresenter;
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
public class ChaoTuActivity extends BaseActivity implements SamplingPointInterface {
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;
    private Scheme planning;
    private List<PlannningData.Pointly> data;

    private com.supermap.android.maps.Point2D center;

    private SamplingPointPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        if (Api.TESTING) {
            baseLayerView.setURL(Api.TESTCHAOTU);
        } else {
            baseLayerView.setURL(Api.CHAOTU);
        }
        mapView.addLayer(baseLayerView);
        // 启用内置放大缩小控件
        //设置地图缩放
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(3);
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        if (planning == null) {
            titleView.setTitle("点位分布");

        } else {
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

    @Override
    protected void loadData() {

    }

    private void dats() {
        if (data != null && data.size() != 0) {
            center = new com.supermap.android.maps.Point2D(data.get(0).data.longitude, data.get(0).data.latitude);
            mapView.getController().setCenter(center);
            Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_sence_undown);
            Drawable drawablenormal = getResources().getDrawable(R.mipmap.ic_sence_down);
            DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
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
            mapView.getOverlays().add(overlay);
            mapView.invalidate();
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
    }


    //获取点位成功信息记录。
    @Override
    public void onLoginSuccess(List<PlannningData.Pointly> info) {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
        }
        dats();
    }

    @Override
    public void onLoginFailed() {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
        }
        dats();
    }

    @Override
    public void onUpSuccess() {

    }

    @Override
    public void onUpFailed() {

    }

    @Override
    public void onItemImageClick(SenceSamplingSugar point, PlannningData.Point dats) {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }


    @Override
    public void onBackPressed() {
        if (mapView != null) {
            mapView.destroy();
        }
        super.onBackPressed();
    }

}

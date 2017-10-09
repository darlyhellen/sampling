package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.supermap.android.commons.EventStatus;
import com.supermap.android.maps.CoordinateReferenceSystem;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.ItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.measure.MeasureMode;
import com.supermap.android.maps.measure.MeasureParameters;
import com.supermap.android.maps.measure.MeasureResult;
import com.supermap.android.maps.measure.MeasureService;
import com.supermap.android.maps.query.FilterParameter;
import com.supermap.android.maps.query.QuertyResultInfo;
import com.supermap.android.maps.query.QueryByGeometryParameters;
import com.supermap.android.maps.query.QueryByGeometryService;
import com.supermap.android.maps.query.QueryEventListener;
import com.supermap.android.maps.query.QueryResult;
import com.supermap.android.maps.query.SpatialQueryMode;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;
import com.supermap.services.components.commontypes.Point2D;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.GroundTypeInfo;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.biz.GroundChooseListener.GroundChooseInterface;
import com.xiangxun.sampling.ui.presenter.GroundChoosePresenter;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 使用超图地图进行地块区域选择。 返回截图文件。
 */
@ContentBinder(R.layout.activity_chaotu)
public class GroundChooseActivity extends BaseActivity implements OnClickListener, GroundChooseInterface {
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;

    private com.supermap.android.maps.Point2D center;

    private List<GroundTypeInfo.Ground> data;

    private GroundChoosePresenter presenter;

    private LoadDialog loading;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        baseLayerView.setURL(Api.getMalink());
        center = new com.supermap.android.maps.Point2D(Double.parseDouble(getIntent().getStringExtra("longitude")), Double.parseDouble(getIntent().getStringExtra("latitude")));
        mapView.getController().setCenter(center);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        // 启用内置放大缩小控件
        // mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(2);
        titleView.setTitle("选择地块");
        presenter = new GroundChoosePresenter(this);
        presenter.block();
        loading = new LoadDialog(this);
        loading.setTitle(R.string.st_loading);
        loading.show();
    }

    @Override
    protected void loadData() {
    }


    private void datas() {
        if (data != null && data.size() > 0) {
//            Point2D[] pts = new Point2D[data.size()];
//            for (int i = 0; i < data.size(); i++) {
//                Point2D s = new Point2D((float) data.get(i).longitude, (float) data.get(i).latitude);
//                pts[i] = s;
//            }
//            Measure_Area(pts);

            Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_sence_undown);
            DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
            for (GroundTypeInfo.Ground point : data) {
                com.supermap.android.maps.Point2D poind = new com.supermap.android.maps.Point2D(point.longitude, point.latitude);
                OverlayItem overlayItem = new OverlayItem(poind, point.name, point.id);
                overlayItem.setMarker(drawableBlue);
                overlay.addItem(overlayItem);
            }
            overlay.setOnFocusChangeListener(new SelectedOverlay());
            mapView.getOverlays().add(overlay);
            mapView.invalidate();
        }
    }

    // 面积量算结果
    public void Measure_Area(Point2D[] pts) {
        // 构造查询参数
        MeasureParameters parameters = new MeasureParameters();
        parameters.point2Ds = pts;
        MeasureService service = null;
        service = new MeasureService(Api.getMalink());
        MyMeasureEventListener listener = new MyMeasureEventListener();
        service.process(parameters, listener, MeasureMode.AREA);
        try {
            listener.waitUntilProcessed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 量算监听器类
    class MyMeasureEventListener extends MeasureService.MeasureEventListener {
        @Override
        public void onMeasureStatusChanged(Object sourceObject, EventStatus status) {
            // 量算结果
            MeasureResult result = (MeasureResult) sourceObject;
        }
    }

    private void geometryQuery(Point2D[] pts) {
        QueryByGeometryParameters p = new QueryByGeometryParameters();
        p.spatialQueryMode = SpatialQueryMode.INTERSECT;// 必设，空间查询模式，默认相交
        // 构建查询几何对象
        Geometry g = new Geometry();
        g.type = GeometryType.REGION;
        g.points = pts;
        g.parts = new int[]{4};
        p.geometry = g;
        FilterParameter fp = new FilterParameter();
        fp.name = "绵竹市";// 必设参数，图层名称格式：数据集名称@数据源别名
        p.filterParameters = new FilterParameter[]{fp};
        QueryByGeometryService qs = new QueryByGeometryService(Api.getMalink());
        qs.process(p, new MyQueryEventListener());
    }

    public class MyQueryEventListener extends QueryEventListener {
        @Override
        public void onQueryStatusChanged(Object sourceObject, EventStatus status) {
            DLog.i(getClass().getSimpleName(), status);
            if (sourceObject instanceof QueryResult && status.equals(EventStatus.PROCESS_COMPLETE)) {
                QueryResult qr = (QueryResult) sourceObject;
                DLog.i(getClass().getSimpleName(), qr.quertyResultInfo + "" + qr.resourceInfo);
                QuertyResultInfo info = qr.quertyResultInfo;
                DLog.i(getClass().getSimpleName(), info.currentCount + "" + info.totalCount + "" + info.recordsets + "" + info.customResponse);
            }
        }
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view_ok:
                ToastApp.showToast("点击确认");
                break;
        }
    }

    @Override
    public void onDateSuccess(List<GroundTypeInfo.Ground> result) {
        data = result;
        datas();
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            OverlayItem item = (OverlayItem) msg.obj;
            DLog.i(getClass().getSimpleName(), item.getTitle() + item.getSnippet() + item);
            Intent intent = new Intent();
            intent.putExtra("name", item.getTitle());
            intent.putExtra("id", item.getSnippet());
            setResult(Activity.RESULT_OK, intent);
            onBackPressed();
        }
    };

    @Override
    public void onDateFailed(String info) {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        ToastApp.showToast(info);
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    /**
     * Overlay焦点获取事件
     */
    class SelectedOverlay implements ItemizedOverlay.OnFocusChangeListener {

        @Override
        public void onFocusChanged(ItemizedOverlay overlay, OverlayItem item) {
            DLog.i(getClass().getSimpleName(), item.getTitle() + item.getSnippet() + item);
            Message m = new Message();
            m.obj = item;
            handler.sendMessage(m);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.stopClearCacheTimer();// 停止和销毁 清除运行时服务器中缓存瓦片的定时器。
            mapView.destroy();
        }
        super.onDestroy();
    }
}

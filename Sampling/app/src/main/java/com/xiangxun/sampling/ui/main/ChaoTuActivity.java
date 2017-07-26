package com.xiangxun.sampling.ui.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.supermap.android.commons.EventStatus;
import com.supermap.android.maps.CoordinateReferenceSystem;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.ItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Overlay;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.supermap.android.maps.query.FilterParameter;
import com.supermap.android.maps.query.QueryByBoundsParameters;
import com.supermap.android.maps.query.QueryByBoundsService;
import com.supermap.android.maps.query.QueryEventListener;
import com.supermap.android.maps.query.QueryResult;
import com.supermap.services.components.commontypes.Rectangle2D;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.db.SenceSamplingSugar;
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
    private static final String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;
    private Scheme planning;
    private List<PlannningData.Pointly> data;

    private Point2D center;

    private SamplingPointPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        baseLayerView.setURL(DEFAULT_URL);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(false);
        mapView.setClickable(false);
        mapView.getController().setZoom(10);
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        if (planning == null) {
            titleView.setTitle("点位分布");
            return;
        }
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
        boundsQuery();
    }

    @Override
    protected void loadData() {
        if (data != null) {
            center = new Point2D(data.get(0).data.longitude, data.get(0).data.latitude);
            mapView.getController().setCenter(center);
            Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_unsamply_normal);
            Drawable drawablenormal = getResources().getDrawable(R.mipmap.ic_samply_normal);
            DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
            for (PlannningData.Pointly point : data) {
                Point2D poind = new Point2D(point.data.longitude, point.data.latitude);
                OverlayItem overlayItem = new OverlayItem(poind, "", point.data.id);
                if (point.data.isSamply()) {
                    overlayItem.setMarker(drawableBlue);
                } else {
                    overlayItem.setMarker(drawablenormal);
                }
                overlay.addItem(overlayItem);
            }
            overlay.setOnFocusChangeListener(new SelectedOverlay());
            //mapView.getOverlays().add(new CustomOverlay());
            mapView.getOverlays().add(overlay);

            // 重新onDraw一次
            mapView.invalidate();
        }
    }

    private void loadNoData() {
        LayerView baseLayerView = new LayerView(this);
        center = new Point2D(38.111231, 104.4561321);
        baseLayerView.setURL(DEFAULT_URL);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        mapView.getController().setCenter(center);

        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(false);
        mapView.setClickable(false);
        mapView.getController().setZoom(10);
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

    //范围查询
    private void boundsQuery() {
        QueryByBoundsParameters p = new QueryByBoundsParameters();
        // left, bottom, right, top 必设范围
        p.bounds = new Rectangle2D(104.4561321, 38.111231, 105.4561321, 39.111231);
        p.expectCount = 2;// 期望返回的条数
        FilterParameter fp = new FilterParameter();
        fp.name = "Capitals@World.1";// 必设参数，图层名称格式：数据集名称@数据源别名
        p.filterParameters = new FilterParameter[]{fp};
        QueryByBoundsService qs = new QueryByBoundsService(DEFAULT_URL);
        qs.process(p, new MyQueryEventListener());
    }

    public class MyQueryEventListener extends QueryEventListener {
        @Override
        public void onQueryStatusChanged(Object sourceObject, EventStatus status) {
            if (sourceObject instanceof QueryResult && status.equals(EventStatus.PROCESS_COMPLETE)) {
                QueryResult qr = (QueryResult) sourceObject;
            }
        }
    }


    //获取点位成功信息记录。
    @Override
    public void onLoginSuccess(List<PlannningData.Pointly> info) {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
            if (data != null && data.size() != 0) {
                loadData();
            } else {
                loadNoData();
            }
        } else {
            loadNoData();
        }
    }

    @Override
    public void onLoginFailed() {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((PlannningData.ResultPointData) s).result;
            //緩存點位
            if (data != null && data.size() != 0) {
                loadData();
            } else {
                loadNoData();
            }
        } else {
            loadNoData();
        }
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

    /**
     * Overlay焦点获取事件
     */
    class SelectedOverlay implements ItemizedOverlay.OnFocusChangeListener {

        @Override
        public void onFocusChanged(ItemizedOverlay overlay, OverlayItem item) {
//			// 地图中心漫游至当前OverlayItem
//			mapView.getController().animateTo(item.getPoint());
//			Toast.makeText(mapView.getContext().getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            // 弹出气泡展示消息
        }

    }

    /**
     * 自定义Overlay
     */
    class CustomOverlay extends Overlay {

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            Point point = mapView.getProjection().toPixels(center, null);
            paint.setTextSize(24);
            paint.setStrokeWidth(0.8f);
            paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText(planning.name, point.x, point.y, paint);
        }
    }


    @Override
    public void onBackPressed() {
        if (mapView != null) {
            mapView.destroy();
        }
        super.onBackPressed();
    }

}

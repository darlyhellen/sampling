package com.xiangxun.sampling.ui.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.supermap.android.maps.CoordinateReferenceSystem;
import com.supermap.android.maps.DefaultItemizedOverlay;
import com.supermap.android.maps.ItemizedOverlay;
import com.supermap.android.maps.LayerView;
import com.supermap.android.maps.MapView;
import com.supermap.android.maps.Overlay;
import com.supermap.android.maps.OverlayItem;
import com.supermap.android.maps.Point2D;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.widget.header.TitleView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 使用超图地图展示点位信息。
 */
@ContentBinder(R.layout.activity_chaotu)
public class ChaoTuActivity extends BaseActivity {
    private static final String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-ETuoKeQi/rest/maps/地区面@地区面";
    String url = "http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China";
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;
    private LayerView baseLayerView;
    private SamplingPlanning planning;
    private int titleBarHeight;

    private Point2D center;

    @Override
    protected void initView(Bundle savedInstanceState) {
        baseLayerView = new LayerView(this);
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        if (planning == null) {
            titleView.setTitle("点位分布");
            return;
        }
        titleView.setTitle(planning.getTitle() + "点位分布");
        center = new Point2D(planning.getPoints().get(0).getLongitude(), planning.getPoints().get(0).getLatitude());
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
        mapView.post(new Runnable() {
            public void run() {
                titleBarHeight = initHeight();
            }
        });
    }

    @Override
    protected void loadData() {

        Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_point_select);
        DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
        for (SamplingPoint point : planning.getPoints()) {
            Point2D poind = new Point2D(point.getLongitude(), point.getLatitude());
            OverlayItem overlayItem = new OverlayItem(poind, point.getDesc(), point.getId());
            overlay.addItem(overlayItem);
        }
        overlay.setOnFocusChangeListener(new SelectedOverlay());
        mapView.getOverlays().add(new CustomOverlay());
        mapView.getOverlays().add(overlay);

        // 重新onDraw一次
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

    /**
     * 计算标题栏的高度
     *
     * @return
     */
    private int initHeight() {
        Rect rect = new Rect();
        Window window = getWindow();
        mapView.getWindowVisibleDisplayFrame(rect);
        //状态栏的高度
        int statusBarHight = rect.top;
        //标题栏跟状态栏的总体高度
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //标题栏的高度
        int titleBarHeight = contentViewTop - statusBarHight;
        return titleBarHeight;
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
            canvas.drawText(planning.getTitle(), point.x, point.y, paint);
        }
    }


    @Override
    protected void onDestroy() {
        // LocationUtil.disposeLoction();
        if (mapView != null) {
            mapView.destroy();
        }
        super.onDestroy();
    }

}

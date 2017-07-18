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
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
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
    private static final String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-ETuoKeQi/rest/maps/地区面@地区面";
    String url = "http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China";
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
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        if (planning == null) {
            titleView.setTitle("点位分布");
            return;
        }
        titleView.setTitle(planning.name + "点位分布");

        Object ob = SharePreferHelp.getValue(planning.id);
        if (data == null) {
            if (ob != null) {
                data = (List<PlannningData.Pointly>) ob;
            }
        }
        //在这里先查看是否有缓存文件，有缓存点位文件，进行展示，然后请求更新点位。
        presenter = new SamplingPointPresenter(this);
        presenter.point(planning.id, ob == null ? null : String.valueOf(System.currentTimeMillis()));

        center = new Point2D(data.get(0).data.longitude, data.get(0).data.latitude);
        baseLayerView.setURL(DEFAULT_URL);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        mapView.getController().setCenter(center);

        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(10);
        mapView.post(new Runnable() {
            public void run() {
                initHeight();
            }
        });
    }

    @Override
    protected void loadData() {
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
        return contentViewTop - statusBarHight;
    }

    //获取点位成功信息记录。
    @Override
    public void onLoginSuccess(List<PlannningData.Pointly> info) {

    }

    @Override
    public void onLoginFailed() {

    }

    @Override
    public void end() {

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

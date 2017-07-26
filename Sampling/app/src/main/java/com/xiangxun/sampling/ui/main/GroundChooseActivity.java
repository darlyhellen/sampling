package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
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
import com.xiangxun.sampling.bean.GroundTypeInfo;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.GroundChooseListener.GroundChooseInterface;
import com.xiangxun.sampling.ui.presenter.GroundChoosePresenter;
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
    private static final String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-MianZhuShi2/rest/maps/绵竹市";//2是影像地图。没有是行政地图
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;

    private Point2D center;

    private List<GroundTypeInfo.Ground> data;

    private GroundChoosePresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        baseLayerView.setURL(DEFAULT_URL);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(10);
        titleView.setTitle("选择地块");
        presenter = new GroundChoosePresenter(this);
        presenter.block();
        boundsQuery();
    }

    @Override
    protected void loadData() {
        if (data != null) {
            center = new Point2D(data.get(0).longitude, data.get(0).latitude);
            mapView.getController().setCenter(center);
            Drawable drawableBlue = getResources().getDrawable(R.mipmap.ic_unsamply_normal);
            DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(drawableBlue);
            for (GroundTypeInfo.Ground point : data) {
                Point2D poind = new Point2D(point.longitude, point.latitude);
                OverlayItem overlayItem = new OverlayItem(poind, point.name, point.id);
                overlayItem.setMarker(drawableBlue);
                overlay.addItem(overlayItem);
            }
            overlay.setOnFocusChangeListener(new SelectedOverlay());
            //mapView.getOverlays().add(new CustomOverlay());
            mapView.getOverlays().add(overlay);

            // 重新onDraw一次
            mapView.invalidate();
        }else {
            LayerView baseLayerView = new LayerView(this);
            center = new Point2D(38.111231, 104.4561321);
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
        }
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


    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        titleView.setRightViewRightTextOneListener("确认", this);
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
        loadData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            OverlayItem item = (OverlayItem) msg.obj;
            Intent intent = new Intent();
            intent.putExtra("name", item.getTitle());
            intent.putExtra("id", item.getSnippet());
            setResult(Activity.RESULT_OK, intent);
            onBackPressed();
        }
    };

    @Override
    public void onDateFailed(String info) {

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
            Message m = new Message();
            m.obj = item;
            handler.sendMessage(m);
        }

    }

    /**
     * 自定义Overlay
     */
    class CustomOverlay extends Overlay {

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
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

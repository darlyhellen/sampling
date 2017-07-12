package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

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
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.Api;
import com.xiangxun.sampling.widget.header.TitleView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 使用超图地图进行地块区域选择。 返回截图文件。
 */
@ContentBinder(R.layout.activity_chaotu)
public class GroundChooseActivity extends BaseActivity implements OnClickListener {
    private static final String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-ETuoKeQi/rest/maps/地区面@地区面";
    String url = "http://10.10.15.201:8090/iserver/services/map-etkqimage/rest/maps/etkq@etkqimg";
    @ViewsBinder(R.id.id_chaotu_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_chaotu_mapview)
    protected MapView mapView;
    private SamplingPlanning planning;

    private Point2D center;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LayerView baseLayerView = new LayerView(this);
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        if (planning == null) {
            titleView.setTitle("选择地块");
            return;
        }
        titleView.setTitle(planning.getTitle() + "选择地块");
        center = new Point2D(planning.getPoints().get(0).getLongitude(), planning.getPoints().get(0).getLatitude());
        baseLayerView.setURL(url);
        CoordinateReferenceSystem crs = new CoordinateReferenceSystem();
        crs.wkid = 4326;
        baseLayerView.setCRS(crs);
        mapView.addLayer(baseLayerView);
        mapView.getController().setCenter(center);

        // 启用内置放大缩小控件
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.getController().setZoom(12);
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
        for (SamplingPoint point : planning.getPoints()) {
            Point2D poind = new Point2D(point.getLongitude(), point.getLatitude());
            OverlayItem overlayItem = new OverlayItem(poind, point.getDesc(), point.getId());
            if (point.isSamply()) {
                overlayItem.setMarker(drawableBlue);
            } else {
                overlayItem.setMarker(drawablenormal);
            }
            overlay.addItem(overlayItem);
        }
        overlay.setOnClickListener(new ItemizedOverlay.OnClickListener() {
            @Override
            public void onClicked(ItemizedOverlay itemizedOverlay, OverlayItem overlayItem) {
                DLog.i(overlayItem);
                handler.sendEmptyMessage(0);
            }
        });
        overlay.setOnFocusChangeListener(new SelectedOverlay());
        //mapView.getOverlays().add(new CustomOverlay());
        mapView.getOverlays().add(overlay);

        // 重新onDraw一次
        mapView.invalidate();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            File f = saveCustomViewBitmap(mapView);
            Intent intent = new Intent();
            intent.putExtra("URL", f == null ? null : f.getAbsolutePath());
            setResult(Activity.RESULT_OK, intent);
            onBackPressed();
        }
    };

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view_ok:
                ToastApp.showToast("点击确认");

                break;
        }
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
    public void onBackPressed() {
        if (mapView != null) {
            mapView.destroy();
        }
        super.onBackPressed();
    }

    //保存自定义view的截图
    private File saveCustomViewBitmap(View view) {
        //获取自定义view图片的大小
        Bitmap temBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
        Canvas canvas = new Canvas(temBitmap);
        view.draw(canvas);
        //输出到sd卡
        File file = new File(Api.Root.concat(planning.getId()).concat(".png"));
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream foStream = new FileOutputStream(file);
            temBitmap.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.flush();
            foStream.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }


}

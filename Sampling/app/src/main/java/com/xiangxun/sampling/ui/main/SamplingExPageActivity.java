
package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter.OnImageConsListener;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:新增地块异常页面，包括定位，图片等功能上传。
 */
@ContentBinder(R.layout.activity_sampling_exception)
public class SamplingExPageActivity extends BaseActivity implements AMapLocationListener, OnClickListener, OnImageConsListener {

    private boolean iShow;//是否是展示页面，true为展示页面，false为编辑页面,异常模块进来属于新增编辑,历史异常进来属于展示页面.
    @ViewsBinder(R.id.id_exception_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_user_sence_address)
    private DetailView address;
    @ViewsBinder(R.id.id_user_sence_lat)
    private DetailView latitude;
    @ViewsBinder(R.id.id_user_sence_lont)
    private DetailView longitude;
    @ViewsBinder(R.id.id_user_sence_location)
    private ImageView loca;
    @ViewsBinder(R.id.id_user_locations_name)
    private TextView locationname;


    @ViewsBinder(R.id.id_exception_map)
    private ImageView map;
    @ViewsBinder(R.id.id_exception_select)
    private TextView select;
    @ViewsBinder(R.id.id_exception__declare)
    private EditText declare;
    @ViewsBinder(R.id.id_exception_gird)
    private WholeGridView gridView;
    private List<String> images;
    private SenceImageAdapter imageAdapter;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        iShow = getIntent().getBooleanExtra("EXCEPTION", false);
        titleView.setTitle("地块异常上报");
        locationname.setText("异常定位：");
        select.setHint("点击选择异常类型");
    }

    @Override
    protected void loadData() {
        //初始化图片和视频信息所在位置。
        if (images == null) {
            images = new ArrayList<String>();
            images.add("add");
        }
        if (iShow) {
            File root = new File(Api.SENCE.concat("123"));
            if (root.exists()) {

                File[] file = root.listFiles();
                if (file != null) {
                    for (int i = 0; i < file.length; i++) {
                        DLog.i(file[i].getPath() + "---->" + file[i].getAbsolutePath());
                        if (file[i].getAbsolutePath().endsWith(".jpg")) {
                            images.add(images.size() - 1, file[i].getAbsolutePath());
                        }
                    }
                }
            }
        }
        imageAdapter = new SenceImageAdapter(images, R.layout.item_main_detail_image_adapter, this, this);
        gridView.setAdapter(imageAdapter);
        //啟動定位
        startLocate();
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
        titleView.setRightViewRightTextOneListener("保存", this);
        map.setOnClickListener(this);
        loca.setOnClickListener(this);
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                if (position == (images.size() - 1)) {
                    Intent intentCamera = new Intent(SamplingExPageActivity.this, CameraActivity.class);
                    intentCamera.putExtra("size", images.size());
                    intentCamera.putExtra("file", Api.SENCE.concat("123"));
                    intentCamera.setAction("Sence");
                    intentCamera.putExtra("LOGO", false);//不打印水印
                    startActivityForResult(intentCamera, 1);
                } else {
                    Intent intent = new Intent(SamplingExPageActivity.this, ShowImageViewActivity.class);
                    intent.putExtra("position", position);
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);//必须
                    intent.putExtra("locationY", location[1]);//必须
                    intent.putExtra("url", st);
                    intent.putExtra("width", view.getWidth());//必须
                    intent.putExtra("height", view.getHeight());//必须
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_user_sence_location:
                startLocate();
                break;
            case R.id.title_view_ok:
                ToastApp.showToast("点击保存");
                break;
            case R.id.id_exception_map:
                Intent intent = new Intent(this, GroundChooseActivity.class);
                intent.putExtra("SamplingPlanning", StaticListener.findData().get(0));
                startActivityForResult(intent, 900);
                break;
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(amapLocation.getLatitude()), "");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(amapLocation.getLongitude()), "");
                address.isEdit(true);
                address.setInfo("位置：", String.valueOf(TextUtils.isEmpty(amapLocation.getAddress()) ? "未知位置" : amapLocation.getAddress()), "");
                //startChao(amapLocation);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                address.isEdit(true);
                address.setInfo("地址：", String.valueOf("未知地方"), "");
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(0), "");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(0), "");
                ToastApp.showToast("请链接网络或者打开GPS进行定位");
            }
            mlocationClient.stopLocation();
        }
    }

    @Override
    public void onConsImageListener(View v, int position) {
        File file = new File(images.get(position));
        if (file.exists()) {
            file.delete();
        }
        images.remove(position);
        imageAdapter.setData(images);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 900 && resultCode == Activity.RESULT_OK) {
            String rul = data.getStringExtra("URL");
            ImageLoader.getInstance().displayImage("file://" + rul, map);
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
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
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


}

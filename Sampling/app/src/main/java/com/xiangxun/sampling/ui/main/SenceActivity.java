package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter.OnImageConsListener;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter.OnVideoConsListener;
import com.xiangxun.sampling.ui.biz.SenceListener.SenceInterface;
import com.xiangxun.sampling.ui.presenter.SencePresenter;
import com.xiangxun.sampling.widget.dialog.SelectItemDialog;
import com.xiangxun.sampling.widget.dialog.SelectItemDialog.SelectResultItemClick;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;
import com.xiangxun.video.camera.VCamera;
import com.xiangxun.video.ui.WechatRecoderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/7.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 現場情況的展示頁面
 */
@ContentBinder(R.layout.activity_sence)
public class SenceActivity extends BaseActivity implements AMapLocationListener, OnClickListener, OnImageConsListener, OnVideoConsListener, SenceInterface, SelectResultItemClick {
    private static String DEFAULT_URL = "http://10.10.15.201:8090/iserver/services/map-ETuoKeQi/rest/maps/地区面@地区面";
    private Scheme planning;
    private Pointly point;
    @ViewsBinder(R.id.id_user_sence_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_user_locations_name)
    private TextView locationname;
    @ViewsBinder(R.id.id_user_sence_address)
    private DetailView address;
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

    @ViewsBinder(R.id.id_user_sence_image_grid)
    private WholeGridView imageGrid;
    @ViewsBinder(R.id.id_user_sence_video_grid)
    private WholeGridView videoGrid;

    private SenceImageAdapter imageAdapter;
    private SenceVideoAdapter videoAdapter;

    private List<String> images;
    private List<String> videos;

    private SelectItemDialog typeDialog;

    private SencePresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        point = (Pointly) getIntent().getSerializableExtra("SamplingKey");
        titleView.setTitle("现场采样");
        locationname.setText("现场采样定位：");
        presenter = new SencePresenter(this);
    }


    @Override
    protected void loadData() {
        type.isEdit(false);
        type.setInfo("采样类型:", " ", "请点击选择类型");
        name.isEdit(true);
        name.setInfo("样品名称:", " ", "请输入样品名称");
        params.isEdit(true);
        params.setInfo("样品深度:", " ", "请输入样品深度");
        project.isEdit(true);
        project.setInfo("待测项目:", " ", "请输入待测项目");
        other.isEdit(true);
        other.setInfo("其他說明:", " ", "请输入说明备注信息");
        //初始化图片和视频信息所在位置。
        if (images == null) {
            images = new ArrayList<String>();
            images.add("add");
        }
        if (videos == null) {
            videos = new ArrayList<String>();
            videos.add("add");
        }

        File root = new File(Api.SENCE.concat(point.data.id));
        if (root.exists()) {

            File[] file = root.listFiles();
            if (file != null) {
                for (int i = 0; i < file.length; i++) {
                    DLog.i(file[i].getPath() + "---->" + file[i].getAbsolutePath());
                    if (file[i].getAbsolutePath().endsWith(".mp4")) {
                        videos.add(videos.size() - 1, file[i].getAbsolutePath());
                    }
                    if (file[i].getAbsolutePath().endsWith(".jpg")) {
                        images.add(images.size() - 1, file[i].getAbsolutePath());
                    }
                }
            }
        }

        imageAdapter = new SenceImageAdapter(images, R.layout.item_main_detail_image_adapter, this, this);
        imageGrid.setAdapter(imageAdapter);
        videoAdapter = new SenceVideoAdapter(videos, R.layout.item_main_detail_video_adapter, this, this);
        videoGrid.setAdapter(videoAdapter);
        //啟動定位
        startLocate();
    }

    //点击删除图片
    @Override
    public void onConsImageListener(View v, int position) {
        File file = new File(images.get(position));
        if (file.exists()) {
            file.delete();
        }
        images.remove(position);
        imageAdapter.setData(images);
    }

    //点击删除视频
    @Override
    public void onConsVideoListener(View v, int position) {
        File file = new File(videos.get(position));
        if (file.exists()) {
            file.delete();
        }
        videos.remove(position);
        videoAdapter.setData(videos);
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
        type.setOnClickListener(this);
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                if (position == (images.size() - 1)) {
                    Intent intentCamera = new Intent(SenceActivity.this, CameraActivity.class);
                    intentCamera.putExtra("size", images.size());
                    intentCamera.putExtra("file", Api.SENCE.concat(point.data.id));
                    intentCamera.setAction("Sence");
                    intentCamera.putExtra("LOGO", false);//不打印水印
                    startActivityForResult(intentCamera, 1);
                } else {
                    Intent intent = new Intent(SenceActivity.this, ShowImageViewActivity.class);
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
        videoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的视频页面
                String st = (String) parent.getItemAtPosition(position);
                if (position == (videos.size() - 1)) {
                    //跳转到视频录制页面
                    //设置对应根据点位的ID生成的文件夹，进行视频文件的保存
                    File root = new File(Api.SENCE.concat(point.data.id));
                    if (!root.exists()) {
                        root.mkdir();
                    }
                    VCamera.setVideoCachePath(root + "/cache/");
                    startActivityForResult(new Intent(SenceActivity.this, WechatRecoderActivity.class), 900);
                }
            }
        });


        loca.setOnClickListener(this);
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        titleView.setRightViewRightTextOneListener("保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isWifi(SenceActivity.this)) {
                    //整体上传，否则片段上传。
                    presenter.sampling(point.data, true);
                } else {
                    presenter.sampling(point.data, false);
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
            case R.id.id_user_sence_type:
                if (typeDialog == null) {
                    typeDialog = new SelectItemDialog(SenceActivity.this, new String[]{"土壤", "玉米", "大豆", "空气", "地下水", "地表水"}, "请选择类型");
                }
                typeDialog.setSelectResultItemClick(this);
                typeDialog.show();
                break;
        }
    }

    @Override
    public void resultOnClick(String result) {
        type.isEdit(false);
        type.setInfo("采样类型:", result, null);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 901 && requestCode == 900) {
            String video = data.getStringExtra("path");
            videos.add(videos.size() - 1, video);
            videoAdapter.setData(videos);
        } else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            List<String> photos = (List<String>) data.getSerializableExtra("camera_picture");
            images.addAll(images.size() - 1, photos);
            imageAdapter.setData(images);
        }
    }

    //ui规划
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(String info) {

    }

    @Override
    public String getaddress() {
        return address.getText();
    }

    @Override
    public String getlatitude() {
        return latitude.getText();
    }

    @Override
    public String getlongitude() {
        return longitude.getText();
    }

    @Override
    public String gettype() {
        return type.getValue().getText().toString().trim();
    }

    @Override
    public String getname() {
        return name.getText();
    }

    @Override
    public String getparams() {
        return params.getText();
    }

    @Override
    public String getproject() {
        return project.getText();
    }

    @Override
    public String getother() {
        return other.getText();
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public List<String> getVideos() {
        return videos;
    }

    @Override
    public void end() {
        onBackPressed();
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

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

}

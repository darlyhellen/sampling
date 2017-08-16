package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter.OnImageConsListener;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter;
import com.xiangxun.sampling.ui.adapter.SenceVideoAdapter.OnVideoConsListener;
import com.xiangxun.sampling.ui.biz.SenceListener.SenceInterface;
import com.xiangxun.sampling.ui.presenter.SencePresenter;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.dialog.SelectTypeRegionDialog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;
import com.xiangxun.video.camera.VCamera;
import com.xiangxun.video.ui.WechatRecoderActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
public class SenceActivity extends BaseActivity implements LocationToolsListener, OnClickListener, OnImageConsListener, OnVideoConsListener, SenceInterface, SelectTypeRegionDialog.SelectResultItemClick {
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
    @ViewsBinder(R.id.id_user_sence_video_submit)
    private Button submit;


    @ViewsBinder(R.id.id_user_sence_image_grid)
    private WholeGridView imageGrid;
    @ViewsBinder(R.id.id_user_sence_video_grid)
    private WholeGridView videoGrid;

    private SenceImageAdapter imageAdapter;
    private SenceVideoAdapter videoAdapter;

    private List<String> images;
    private List<String> videos;

    private SencePresenter presenter;

    //进行草稿保存状态。草稿状态存在。则进行原始数据展示。
    private SenceSamplingSugar sugar;
    private MsgDialog msgDialog;
    private SelectTypeRegionDialog typeDialog;


    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        //这句话解决了自动弹出输入按键
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        point = (Pointly) getIntent().getSerializableExtra("SamplingKey");
        sugar = (SenceSamplingSugar) SharePreferHelp.getValue("sugar" + point.data.id);
        titleView.setTitle("现场采样");
        locationname.setText("现场采样定位：");
        presenter = new SencePresenter(this);
    }


    @Override
    protected void loadData() {

        if (sugar != null) {
            //已经有草稿状态。直接进行全参数设置
            type.isEdit(false);
            type.setInfo("采样类型:", planning.sampleName, "");
            name.isEdit(true);
            name.setEditTextMaxLin(100);
            name.setInfo("样品名称:", sugar.getName(), null);
            params.isEdit(true);
            params.setEditTextMaxLin(10);
            params.setInfo("样品深度:", sugar.getDepth(), null);
            project.isEdit(false);
            project.setInfo("土壤类型:", sugar.getSoil_name(), null);
            project.setTag(sugar.getResult());
            other.isEdit(true);
            other.setInfo("其他說明:", "", null);
            address.isEdit(false);
            address.setInfo("采样地点：", sugar.getRegion_id(), null);
            latitude.isEdit(true);
            latitude.setInfo("纬度：", sugar.getLatitude(), null);
            longitude.isEdit(true);
            longitude.setInfo("经度：", sugar.getLongitude(), null);
            //初始化图片和视频信息所在位置。
            images = sugar.getImages();
            videos = sugar.getVideos();
            imageAdapter = new SenceImageAdapter(images, R.layout.item_main_detail_image_adapter, this, this);
            imageGrid.setAdapter(imageAdapter);
            videoAdapter = new SenceVideoAdapter(videos, R.layout.item_main_detail_video_adapter, this, this);
            videoGrid.setAdapter(videoAdapter);
        } else {
            //土壤采样
            type.isEdit(false);
            type.setInfo("采样类型:", planning.sampleName, "");
            name.isEdit(true);
            name.setEditTextMaxLin(100);
            name.setInfo("样品名称:", " ", "请输入样品名称");
            params.isEdit(true);
            params.setEditTextMaxLin(10);
            params.setInfo("样品深度:", " ", "请输入样品深度");
            project.isEdit(false);
            project.setInfo("土壤类型:", " ", "请选择土壤类型");
            other.isEdit(true);
            other.setInfo("其他說明:", "", null);
            address.isEdit(false);
            address.setInfo("采样地点：", " ", " ");

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
            if (Api.TESTING) {
                //测试环境下，经纬度写死。手动让其修改。
                latitude.isEdit(true);
                latitude.setInfo("纬度：", String.valueOf(Api.latitude), "");
                longitude.isEdit(true);
                longitude.setInfo("经度：", String.valueOf(Api.longitude), "");
                address.isEdit(false);
                address.setInfo("位置：", String.valueOf("绵竹市九龙镇"), "");
            } else {
                LocationTools.getInstance().setLocationToolsListener(this);
                LocationTools.getInstance().start();
            }
        }
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


    @Override
    protected void initListener() {
        project.setOnClickListener(this);
        submit.setOnClickListener(this);
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里跳转不同的图片页面
                String st = (String) parent.getItemAtPosition(position);
                if (position == (images.size() - 1)) {
                    if (ImageLoaderUtil.isCameraUseable()) {
                        Intent intentCamera = new Intent(SenceActivity.this, CameraActivity.class);
                        intentCamera.putExtra("size", images.size());
                        intentCamera.putExtra("file", Api.SENCE.concat(point.data.id));
                        intentCamera.setAction("Sence");
                        intentCamera.putExtra("LOGO", false);//不打印水印
                        startActivityForResult(intentCamera, 1);
                    } else {
                        ToastApp.showToast("需要调用摄像头权限，请在设置中打开摄像头权限");
                    }
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
                    if (ImageLoaderUtil.isCameraUseable()) {
                        //跳转到视频录制页面
                        //设置对应根据点位的ID生成的文件夹，进行视频文件的保存
                        File root = new File(Api.SENCE.concat(point.data.id));
                        if (!root.exists()) {
                            root.mkdir();
                        }
                        VCamera.setVideoCachePath(root + "/cache/");
                        startActivityForResult(new Intent(SenceActivity.this, WechatRecoderActivity.class), 900);
                    } else {
                        ToastApp.showToast("需要调用摄像头权限，请在设置中打开摄像头权限");
                    }
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


                if (TextUtils.isEmpty(getname())) {
                    ToastApp.showToast("请输入样品名称");
                    return;
                }
                if (TextUtils.isEmpty(getparams())) {
                    ToastApp.showToast("请输入样品深度");
                    return;
                }
                if (TextUtils.isEmpty(getproject())) {
                    ToastApp.showToast("请输入待测项目");
                    return;
                }
                if (gettype() == null) {
                    ToastApp.showToast("请选择采样类型");
                    return;
                }
                if (TextUtils.isEmpty(getaddress())) {
                    ToastApp.showToast("请输入地址信息");
                    return;
                }
                if (TextUtils.isEmpty(getlongitude())) {
                    ToastApp.showToast("请输入经度");
                    return;
                }
                if (TextUtils.isEmpty(getlatitude())) {
                    ToastApp.showToast("请输入纬度");
                    return;
                }
                SenceSamplingSugar paramer = new SenceSamplingSugar();
                paramer.setPointId(point.data.id);
                paramer.setSchemeId(point.data.schemeId);
                paramer.setRegion_id(getaddress());
                paramer.setLongitude(getlongitude());
                paramer.setLatitude(getlatitude());
                paramer.setResult(gettype());
                paramer.setSoil_type(gettype().code);
                paramer.setSoil_name(gettype().name);
                paramer.setName(getname());
                paramer.setDepth(getparams());
                paramer.setTest_item(getproject());
                paramer.setMissionId(planning.missionId);
                paramer.setImages(getImages());
                paramer.setVideos(getVideos());
                SharePreferHelp.putValue("sugar" + point.data.id, paramer);
                ToastApp.showToast("本地保存成功");
                setResult(Activity.RESULT_OK);
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_user_sence_project:
                //进行土壤类型请求
                presenter.landType("请选择土壤类型");
                break;
            case R.id.id_user_sence_location:
                //啟動定位
                if (Api.TESTING) {
                    //测试环境下，经纬度写死。手动让其修改。
                    latitude.isEdit(true);
                    latitude.setInfo("纬度：", String.valueOf(Api.latitude), "");
                    longitude.isEdit(true);
                    longitude.setInfo("经度：", String.valueOf(Api.longitude), "");
                    address.isEdit(false);
                    address.setInfo("位置：", String.valueOf("绵竹市九龙镇"), "");
                } else {
                    LocationTools.getInstance().start();
                }
                break;
            case R.id.id_user_sence_video_submit:
                //进行提示弹窗，询问用户是否确认修改上传状态。
                if (presenter.isave()) {
                    msgDialog = new MsgDialog(this);
                    msgDialog.setTiele("是否确认上传状态信息？");
                    msgDialog.setButLeftListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msgDialog.dismiss();
                        }
                    });
                    msgDialog.setButRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msgDialog.dismiss();
                            presenter.sampling(planning, point.data);
                        }
                    });
                    msgDialog.show();
                } else {
                    ToastApp.showToast("请先保存采样状态");
                }
                break;
        }

    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        //定位成功回调信息，设置相关消息
        latitude.isEdit(true);
        latitude.setInfo("纬度：", String.valueOf(amapLocation.getLatitude()), "");
        longitude.isEdit(true);
        longitude.setInfo("经度：", String.valueOf(amapLocation.getLongitude()), "");
        address.isEdit(false);
        address.setInfo("位置：", String.valueOf(amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict()), "");
    }

    @Override
    public void locationFail() {
        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        latitude.isEdit(true);
        latitude.setInfo("纬度：", String.valueOf(0), "");
        longitude.isEdit(true);
        longitude.setInfo("经度：", String.valueOf(0), "");
        address.isEdit(false);
        address.setInfo("位置：", "未知位置", "");
        LocationTools.getInstance().start();
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

    @Override
    public void resultOnClick(SenceLandRegion.LandRegion result, String title) {
        if ("请选择土壤类型".equals(title)) {
            project.isEdit(false);
            project.setInfo("土壤类型:", result.name, null);
            project.setTag(result);
        }
    }


    //ui规划
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoginSuccess() {
        setResult(Activity.RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onLoginFailed(String info) {

    }

    @Override
    public void onTypeRegionSuccess(String title, SenceLandRegion result) {
        typeDialog = new SelectTypeRegionDialog(SenceActivity.this, result.result, title);
        typeDialog.setSelectResultItemClick(this);
        typeDialog.show();
    }

    @Override
    public String getaddress() {
        return address.getValue().getText().toString().trim();
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
    public SenceLandRegion.LandRegion gettype() {
        return (SenceLandRegion.LandRegion) project.getTag();
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
        return type.getValue().getText().toString().trim();
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
        address.setClickable(false);
        type.setClickable(false);
    }

    @Override
    public void setEnableClick() {
        address.setClickable(true);
        type.setClickable(true);
    }


    @Override
    protected void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        if (Build.VERSION.SDK_INT >= 23) {
            // 缺少权限时, 进入权限配置页面

            AndPermission.with(this)
                    .requestCode(REQUEST_CODE)
                    .permission(PERMISSIONS_GROUP)
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int i, Rationale rationale) {
                            AndPermission.rationaleDialog(SenceActivity.this, rationale).show();
                        }
                    })
                    .callback(new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> list) {
                            // Successfully.
                            if (requestCode == REQUEST_CODE) {
                                // TODO ...
                                DLog.i(getClass().getSimpleName(), "摄像头，语音，定位权限已经打开");
                            }
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> list) {
                            // Failure.
                            if (requestCode == REQUEST_CODE) {
                                // TODO ...
                                ToastApp.showToast("摄像头，语音，定位授权失败,请手动授权");
                            }
                        }
                    })
                    .start();
        }
        LocationTools.getInstance().reStart();
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        LocationTools.getInstance().stop();
        super.onPause();
    }


}

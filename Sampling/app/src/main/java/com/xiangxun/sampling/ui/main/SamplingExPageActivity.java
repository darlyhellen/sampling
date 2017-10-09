
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter;
import com.xiangxun.sampling.ui.adapter.SenceImageAdapter.OnImageConsListener;
import com.xiangxun.sampling.ui.biz.ExceptionPageListener.ExceptionPageInterface;
import com.xiangxun.sampling.ui.presenter.ExceptionPagePresenter;
import com.xiangxun.sampling.widget.MaxLengthWatcher;
import com.xiangxun.sampling.widget.MaxLengthWatcher.MaxLengthUiListener;
import com.xiangxun.sampling.widget.dialog.MsgDialog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeGridView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
public class SamplingExPageActivity extends BaseActivity implements LocationToolsListener, OnClickListener, OnImageConsListener, ExceptionPageInterface, MaxLengthUiListener {

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

    //类型
    @ViewsBinder(R.id.id_exception_chos_type)
    private RelativeLayout type;
    @ViewsBinder(R.id.id_exception_select)
    private TextView select;
    //地块信息
    @ViewsBinder(R.id.id_exception_chos_land)
    private RelativeLayout chosLand;
    @ViewsBinder(R.id.id_exception_land)
    private TextView land;
    @ViewsBinder(R.id.id_exception__declare)
    private EditText declare;
    @ViewsBinder(R.id.id_order_equip_declare_num)
    private TextView declare_num;
    @ViewsBinder(R.id.id_exception_gird)
    private WholeGridView gridView;
    private List<String> images;
    private SenceImageAdapter imageAdapter;

    private String landid;

    private ExceptionPagePresenter presenter;

    private MsgDialog msgDialog;

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
        titleView.setTitle("地块异常上报");
        locationname.setText("异常定位：");
        presenter = new ExceptionPagePresenter(this);
    }

    @Override
    protected void loadData() {
        //初始化图片和视频信息所在位置。
        if (images == null) {
            images = new ArrayList<String>();
            images.add("add");
        }
        //有缓存的话
        File root = new File(Api.SENCE.concat("exceptionimage"));
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
        imageAdapter = new SenceImageAdapter(images, R.layout.item_main_detail_image_adapter, this, this);
        gridView.setAdapter(imageAdapter);
        //啟動定位
        if (Api.TESTING) {
            //测试环境下，经纬度写死。手动让其修改。
            latitude.isEdit(true);
            latitude.setInfo("纬度：", String.valueOf(Api.latitude), "");
            longitude.isEdit(true);
            longitude.setInfo("经度：", String.valueOf(Api.longitude), "");
            address.isEdit(false);
            address.setInfo("位置：", String.valueOf("绵竹市齐天镇"), "");
        } else {
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
    }


    @Override
    protected void initListener() {
        declare.addTextChangedListener(new MaxLengthWatcher(200, declare, this));
        declare_num.setText("0/200");
        //两个选择
        type.setOnClickListener(this);
        chosLand.setOnClickListener(this);

        titleView.setRightViewRightTextOneListener("保存", this);
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
                    if (ImageLoaderUtil.isCameraUseable()) {
                        Intent intentCamera = new Intent(SamplingExPageActivity.this, CameraActivity.class);
                        intentCamera.putExtra("size", images.size());
                        intentCamera.putExtra("file", Api.SENCE.concat("exceptionimage"));
                        intentCamera.setAction("Sence");
                        intentCamera.putExtra("LOGO", false);//不打印水印
                        startActivityForResult(intentCamera, 1);
                    } else {
                        ToastApp.showToast("需要调用摄像头权限，请在设置中打开摄像头权限");
                    }
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
                LocationTools.getInstance().start();
                break;
            case R.id.title_view_ok:
                //进行提示弹窗，询问用户是否确认修改上传状态。
                msgDialog = new MsgDialog(this);
                msgDialog.setTiele("是否确认上传异常地块信息？");
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
                        presenter.addException();
                    }
                });
                msgDialog.show();
                break;
            case R.id.id_exception_chos_land:
                //跳转到地块选择
                if (TextUtils.isEmpty(longitude.getText()) || TextUtils.isEmpty(latitude.getText())) {
                    ToastApp.showToast("经纬度信息为空，请重新定位");
                    return;
                }
                if ("0".equals(longitude.getText()) || "0".equals(latitude.getText())) {
                    ToastApp.showToast("定位失败，请重新定位");
                    return;
                }

                Intent intent = new Intent(this, GroundChooseActivity.class);
                intent.putExtra("longitude", longitude.getText());
                intent.putExtra("latitude", latitude.getText());
                startActivityForResult(intent, 900);
                break;
            case R.id.id_exception_chos_type:
                ToastApp.showToast("类型");
                break;
        }
    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
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
        address.isEdit(true);
        address.setInfo("地址：", String.valueOf("未知地方"), "");
        latitude.isEdit(true);
        latitude.setInfo("纬度：", String.valueOf(0), "");
        longitude.isEdit(false);
        longitude.setInfo("经度：", String.valueOf(0), "");
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
            DLog.i(data);
            land.setText(data.getStringExtra("name"));
            landid = data.getStringExtra("id");
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            List<String> photos = (List<String>) data.getSerializableExtra("camera_picture");
            images.addAll(images.size() - 1, photos);
            imageAdapter.setData(images);
        }
    }


    @Override
    public void onDateSuccess(List<HisExceptionInfo.HisException> result) {
        ToastApp.showToast("上传成功");
        onBackPressed();
    }

    @Override
    public void onDateFailed(String info) {

    }

    @Override
    public String getLatitude() {
        return latitude.getText();
    }

    @Override
    public String getLongitude() {
        return longitude.getText();
    }

    @Override
    public String getLandid() {
        return landid;
    }

    @Override
    public String getDeclare() {
        return declare.getText().toString().trim();
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void onUiChanged(int num) {
        declare_num.setText(num + "/200");
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
                            AndPermission.rationaleDialog(SamplingExPageActivity.this, rationale).show();
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

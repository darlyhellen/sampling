package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.biz.AddPointListener.AddPointInterface;
import com.xiangxun.sampling.ui.presenter.AddPointPresenter;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 新增修改计划中的点位信息。
 */
@ContentBinder(R.layout.activity_planning_add)
public class AddNewPointPlanningActivity extends BaseActivity implements LocationToolsListener, AddPointInterface, View.OnClickListener {
    private Scheme planning;
    private Pointly point;

    @ViewsBinder(R.id.id_add_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_add_projectname)
    private DetailView name;
    @ViewsBinder(R.id.id_add_projecdept)
    private DetailView dept;
    @ViewsBinder(R.id.id_add_projectposition)
    private DetailView position;
    @ViewsBinder(R.id.id_add_type)
    private DetailView type;
    @ViewsBinder(R.id.id_add_latitude)
    private DetailView latitude;
    @ViewsBinder(R.id.id_add_longitude)
    private DetailView longitude;
    @ViewsBinder(R.id.id_add_desc)
    private DetailView desc;

    private AddPointPresenter presenter;
    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        point = (Pointly) getIntent().getSerializableExtra("SamplingKey");
        presenter = new AddPointPresenter(this);

    }

    @Override
    protected void loadData() {
        name.isEdit(false);
        name.setInfo("计划名称：", planning.name, "");
        dept.isEdit(false);
        dept.setInfo("实施机构：", planning.dept, "");
        position.isEdit(false);
        position.setInfo("采样选址：", planning.regionName, "");
        type.isEdit(false);
        type.setInfo("采样类型：", planning.sampleName, "");
        if (point == null) {
            //新增点位
            titleView.setTitle("新增" + planning.name + "点位");
            if (Api.TESTING) {
                latitude.isEdit(true);
                latitude.setInfo("纬度：", String.valueOf(Api.latitude), "");
                longitude.isEdit(true);
                longitude.setInfo("经度：", String.valueOf(Api.longitude), "");
            } else {
                LocationTools.getInstance().setLocationToolsListener(this);
                LocationTools.getInstance().start();
            }
            titleView.setRightViewRightTextOneListener("保存", this);

        } else {
            //修改点位
            desc.isEdit(false);
            if (point.data.isSampling == 1) {
                desc.setInfo("是否已采样：", "是", "");
                titleView.setTitle("修改" + planning.name + "点位");
                latitude.isEdit(false);
                latitude.setInfo("纬度：", String.valueOf(point.data.latitude), "");
                longitude.isEdit(false);
                longitude.setInfo("经度：", String.valueOf(point.data.longitude), "");
            } else {
                desc.setInfo("是否已采样：", "否", "");
                titleView.setTitle("修改" + planning.name + "点位");
                latitude.isEdit(true);
                latitude.setInfo("纬度：", String.valueOf(point.data.latitude), "");
                longitude.isEdit(true);
                longitude.setInfo("经度：", String.valueOf(point.data.longitude), "");
                titleView.setRightViewRightTextOneListener("修改", this);
            }
        }
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view_back_img:
                onBackPressed();
                break;
            case R.id.title_view_ok:
                //提交服务端,进行重新获取.
                if (point == null) {
                    //新增点位信息
                    presenter.addPoint(planning);
                } else {
                    //修改点位信息。
                    point.data.latitude = Double.parseDouble(latitude());
                    point.data.longitude = Double.parseDouble(longitude());
                    presenter.updataPoint(planning, point);
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
        desc.isEdit(true);
        desc.setInfo("说明：", amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict(), "");
    }

    @Override
    public void locationFail() {
        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        latitude.isEdit(true);
        latitude.setInfo("纬度：", String.valueOf(0), "");
        longitude.isEdit(true);
        longitude.setInfo("经度：", String.valueOf(0), "");
        desc.isEdit(true);
        desc.setInfo("说明：", " ", "");
    }


    //网络请求，新增点位接口
    @Override
    public void onLoginSuccess() {
        setResult(Activity.RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onLoginFailed(String info) {

    }

    @Override
    public String longitude() {
        return longitude.getText();
    }

    @Override
    public String latitude() {
        return latitude.getText();
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
                            AndPermission.rationaleDialog(AddNewPointPlanningActivity.this, rationale).show();
                        }
                    })
                    .callback(new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> list) {
                            // Successfully.
                            if (requestCode == REQUEST_CODE) {
                                // TODO ...
                                DLog.i(getClass().getSimpleName(), "定位權限已經開啟");
                            }
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> list) {
                            // Failure.
                            if (requestCode == REQUEST_CODE) {
                                // TODO ...
                                ToastApp.showToast("定位授权失败,请手动授权");
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

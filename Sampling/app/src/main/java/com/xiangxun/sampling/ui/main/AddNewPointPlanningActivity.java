package com.xiangxun.sampling.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.biz.AddPointListener.AddPointInterface;
import com.xiangxun.sampling.ui.presenter.AddPointPresenter;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 新增修改计划中的点位信息。
 */
@ContentBinder(R.layout.activity_planning_add)
public class AddNewPointPlanningActivity extends BaseActivity implements AMapLocationListener, AddPointInterface, View.OnClickListener {
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

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    private AddPointPresenter presenter;

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
            titleView.setRightViewRightTextOneListener("保存", this);

        } else {
            //修改点位
            desc.isEdit(false);
            if (point.data.isSampling == 1) {
                desc.setInfo("是否已采样：", "是", "");
                titleView.setTitle("修改" + planning.name + "点位");
                latitude.isEdit(false);
                latitude.setInfo("经度：", String.valueOf(point.data.latitude), "");
                longitude.isEdit(false);
                longitude.setInfo("纬度：", String.valueOf(point.data.longitude), "");
            } else {
                desc.setInfo("是否已采样：", "否", "");
                titleView.setTitle("修改" + planning.name + "点位");
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(point.data.latitude), "");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(point.data.longitude), "");
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
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(amapLocation.getLatitude()), "");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(amapLocation.getLongitude()), "");
                desc.isEdit(true);
                desc.setInfo("说明：", amapLocation.getAddress(), "");
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                latitude.isEdit(true);
                latitude.setInfo("经度：", String.valueOf(0), "");
                longitude.isEdit(true);
                longitude.setInfo("纬度：", String.valueOf(0), "");
                desc.isEdit(true);
                desc.setInfo("说明：", " ", "");
                ToastApp.showToast("请链接网络或者打开GPS进行定位");
            }
            mlocationClient.stopLocation();
        }
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


}

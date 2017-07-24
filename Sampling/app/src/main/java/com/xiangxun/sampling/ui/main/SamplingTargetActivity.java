package com.xiangxun.sampling.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.SamplingTargetAdapter;
import com.xiangxun.sampling.ui.biz.TargetListener.TargetInterface;
import com.xiangxun.sampling.ui.presenter.TargetPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:指标查询查询各个功能
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingTargetActivity extends BaseActivity implements TargetInterface, AMapLocationListener {

    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView xlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private SamplingTargetAdapter adapter;

    private List<SimplingTarget> data;

    private TargetPresenter presenter;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("指标查询");
        presenter = new TargetPresenter(this);
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
    protected void loadData() {
        data = new ArrayList<SimplingTarget>();
        adapter = new SamplingTargetAdapter(data, R.layout.item_planning_list, this);
        xlist.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        titleView.setRightViewRightTextOneListener("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击筛选，获取删选列表。进行数据重新请求
            }
        });
        xlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                DLog.i("onItemClick--" + position);
            }
        });
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                if (TextUtils.isEmpty(amapLocation.getAddress())) {
                    startLocate();
                } else {
                    //请求列表
                    presenter.analysis(amapLocation.getAddress());
                }
                DLog.i(amapLocation);
            } else {
                ToastApp.showToast("请链接网络或者打开GPS进行定位");
            }
            mlocationClient.stopLocation();
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


    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void onDateSuccess(List<SimplingTarget> result) {
        data = result;
        if (data != null && data.size() > 1) {
            xlist.setVisibility(View.VISIBLE);
            adapter.setData(data);
            textView.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有指标异常项");
        }
    }

    @Override
    public void onDateFailed(String info) {

    }
}

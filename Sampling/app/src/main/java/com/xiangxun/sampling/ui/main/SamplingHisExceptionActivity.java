package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.HisExceptionInfo.HisException;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.adapter.SamplingHisExceptionAdapter;
import com.xiangxun.sampling.ui.biz.HisExceptionListener.HisExceptionInterface;
import com.xiangxun.sampling.ui.presenter.HisExceptionPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史地块异常查询各个功能
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHisExceptionActivity extends BaseActivity implements HisExceptionInterface, LocationToolsListener {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView xlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private SamplingHisExceptionAdapter adapter;

    private List<HisException> data;

    private HisExceptionPresenter presenter;

    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史地块异常");
        //直接请求地块异常信息
        presenter = new HisExceptionPresenter(this);
        //啟動定位
        if (Api.TESTING) {
            //测试环境下，经纬度写死。手动让其修改。
            //定位成功回调信息，设置相关消息
            presenter.hisList("绵竹市九龙镇");
        } else {
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
    }


    @Override
    protected void loadData() {
        adapter = new SamplingHisExceptionAdapter(data, R.layout.item_planning_list, this);
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

        xlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到地块详情页面，只是展示效果。不能进行任何操作
                HisException e = (HisException) parent.getItemAtPosition(position);
                if (e != null) {
                    Intent intent = new Intent(SamplingHisExceptionActivity.this, SamplingHisExceptionPageActivity.class);
                    intent.putExtra("id", e.id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDateSuccess(List<HisException> result) {
        data = result;
        if (data != null && data.size() > 0) {
            xlist.setVisibility(View.VISIBLE);
            adapter.setData(data);
            textView.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有指地块异常");
        }
    }

    @Override
    public void onDateFailed(String info) {
        if (data != null && data.size() > 0) {
            xlist.setVisibility(View.VISIBLE);
            adapter.setData(data);
            textView.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有指地块异常");
        }
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        //请求列表
        presenter.hisList(amapLocation.getAddress());
        DLog.i(amapLocation.getAddress());
    }

    @Override
    public void locationFail() {
        LocationTools.getInstance().start();
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
                            AndPermission.rationaleDialog(SamplingHisExceptionActivity.this, rationale).show();
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

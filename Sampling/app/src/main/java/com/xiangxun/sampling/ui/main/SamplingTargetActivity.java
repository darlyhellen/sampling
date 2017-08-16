package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment.SearchListener;
import com.xiangxun.sampling.ui.adapter.SamplingTargetAdapter;
import com.xiangxun.sampling.ui.biz.TargetListener.TargetInterface;
import com.xiangxun.sampling.ui.presenter.TargetPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
public class SamplingTargetActivity extends BaseActivity implements TargetInterface, LocationToolsListener, SearchListener {

    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView xlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private SamplingTargetAdapter adapter;

    private List<SimplingTarget> data;

    private TargetPresenter presenter;

    private String resID;
    //样品名称
    private String sampleName;
    //指标名称
    private String sampleTarget;
    //是否超标
    private String sampleOver;

    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("指标查询");
        presenter = new TargetPresenter(this);
        //啟動定位
        if (Api.TESTING) {
            //测试环境下，经纬度写死。手动让其修改。
            //定位成功回调信息，设置相关消息
            presenter.analysis("绵竹市九龙镇", resID, sampleOver, sampleName, sampleTarget);
        } else {
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
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
        titleView.setRightViewRightOneListener(R.mipmap.ic_title_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击获取筛选结果
                SearchWorkOrderDialogFragment dialog = new SearchWorkOrderDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CLASS", "SamplingTargetActivity");
                bundle.putString("SampleName", sampleName);
                bundle.putString("Target", sampleTarget);
                bundle.putString("Over", sampleOver);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "SearchWorkOrderDialogFragment");
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
    public void locationSuccess(AMapLocation amapLocation) {
        //请求列表
        presenter.analysis(amapLocation.getAddress(), resID, sampleOver, sampleName, sampleTarget);
        DLog.i(amapLocation.getAddress());
    }

    @Override
    public void locationFail() {
        LocationTools.getInstance().start();
    }


    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

    @Override
    public void onDateSuccess(SimplingTargetResult result) {
        data = result.result;
        resID = result.resId;
        if (data != null && data.size() > 0) {
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
                            AndPermission.rationaleDialog(SamplingTargetActivity.this, rationale).show();
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

    @Override
    public void findParamers(String samplename, String target, String over) {
        DLog.i(getClass().getSimpleName(), samplename + target + over);
        sampleName = samplename;
        sampleTarget = target;
        sampleOver = over;
        presenter.analysis(null, resID, sampleOver, sampleName, sampleTarget);
    }


}

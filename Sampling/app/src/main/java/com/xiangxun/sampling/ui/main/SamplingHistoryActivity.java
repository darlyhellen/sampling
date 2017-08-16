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
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.LocationTools;
import com.xiangxun.sampling.common.LocationTools.LocationToolsListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.Api;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment.SearchListener;
import com.xiangxun.sampling.ui.adapter.StickyAdapter;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener.SamplingHistoryInterface;
import com.xiangxun.sampling.ui.presenter.SamplingHistoryPresenter;
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
 * @TODO:历史采样展示历史数据
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHistoryActivity extends BaseActivity implements SamplingHistoryInterface, SearchListener, LocationToolsListener {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;


    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;
    private List<PlannningData.Scheme> data;
    private StickyAdapter adapter;

    private SamplingHistoryPresenter presenter;

    private String hisName;
    private String location;

    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史采样");
        presenter = new SamplingHistoryPresenter(this);
        LocationTools.getInstance().setLocationToolsListener(this);
        LocationTools.getInstance().start();
        //啟動定位
        if (Api.TESTING) {
            //测试环境下，经纬度写死。手动让其修改。
            //定位成功回调信息，设置相关消息
            presenter.getHistory(hisName, "绵竹市九龙镇");
        } else {
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
    }

    @Override
    protected void loadData() {
        adapter = new StickyAdapter(null, R.layout.item_planning_list, this, false);
        wlist.setAdapter(adapter);
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
                //历史记录中的选择
                SearchWorkOrderDialogFragment dialog = new SearchWorkOrderDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CLASS", "SamplingHistoryActivity");
                bundle.putString("hisName", hisName);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "SearchWorkOrderDialogFragment");
            }
        });
        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                PlannningData.Scheme planning = (PlannningData.Scheme) parent.getItemAtPosition(position);
                Intent intent = new Intent(SamplingHistoryActivity.this, SamplingHisPointActivity.class);
                intent.putExtra("Scheme", planning);//传递过去方案对象
                startActivity(intent);
            }
        });
    }


    @Override
    public void onLoginSuccess(List<PlannningData.Scheme> info) {
        data = info;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有已完成的任务");
        }
    }

    @Override
    public void onLoginFailed() {
        wlist.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("没有已完成的任务");
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
    public void findParamers(String hisName, String target, String over) {
        this.hisName = hisName;
        presenter.getHistory(hisName, location);
    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        location = amapLocation.getAddress();
        presenter.getHistory(hisName, amapLocation.getAddress());
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
                            AndPermission.rationaleDialog(SamplingHistoryActivity.this, rationale).show();
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

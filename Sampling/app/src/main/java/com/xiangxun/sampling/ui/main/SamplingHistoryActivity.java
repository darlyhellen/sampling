package com.xiangxun.sampling.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.xiangxun.sampling.ui.adapter.HistoryAdapter;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener.SamplingHistoryInterface;
import com.xiangxun.sampling.ui.presenter.SamplingHistoryPresenter;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;
import com.xiangxun.sampling.widget.xlistView.XListView;
import com.xiangxun.sampling.widget.xlistView.XListView.IXListViewListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史采样展示历史数据
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHistoryActivity extends BaseActivity implements SamplingHistoryInterface, SearchListener, LocationToolsListener, IXListViewListener {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;


    @ViewsBinder(R.id.id_planning_wlist)
    private XListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;
    private List<PlannningData.Scheme> data;
    private HistoryAdapter adapter;


    @ViewsBinder(R.id.id_planning_linear)
    private LinearLayout bg;
    @ViewsBinder(R.id.id_planning_name)
    private TextView name;
    @ViewsBinder(R.id.id_planning_dept)
    private TextView dept;
    @ViewsBinder(R.id.id_planning_place)
    private TextView position;
    @ViewsBinder(R.id.id_planning_tvs)
    private TextView desc;

    private SamplingHistoryPresenter presenter;

    private String resID;

    private String hisName;
    private String samplyName;

    private String location;
    private LoadDialog loading;
    //权限问题
    private String[] PERMISSIONS_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private int currentPage = 1;
    private int PageSize = 20;
    private int totalSize = 0;
    private int listState = Api.LISTSTATEFIRST;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史采样");
        loading = new LoadDialog(this);
        loading.setTitle(R.string.location_loading);

        presenter = new SamplingHistoryPresenter(this);
        //初始化顶部标签
        bg.setBackgroundResource(R.mipmap.title_bg);
        name.setText("任务名称");
        name.setTextColor(getResources().getColor(R.color.white));
        name.setTextSize(16);
        dept.setText("采样类型");
        dept.setTextColor(getResources().getColor(R.color.white));
        dept.setTextSize(16);
        position.setText("位置");
        position.setTextColor(getResources().getColor(R.color.white));
        position.setTextSize(16);
        //啟動定位
        if (Api.TESTING) {
            //测试环境下，经纬度写死。手动让其修改。
            //定位成功回调信息，设置相关消息
            location = "绵竹市齐天镇";
            presenter.getHistory(currentPage, hisName,samplyName, resID, location);
        } else {
            loading.show();
            LocationTools.getInstance().setLocationToolsListener(this);
            LocationTools.getInstance().start();
        }
    }

    @Override
    protected void loadData() {
        data = new ArrayList<PlannningData.Scheme>();
        adapter = new HistoryAdapter(null, R.layout.item_planning_list, this, false);
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
                bundle.putString("samplyName", samplyName);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "SearchWorkOrderDialogFragment");
            }
        });
        wlist.setPullLoadEnable(true);
        wlist.setXListViewListener(this);
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
    public void onLoginSuccess(PlannningData.ResultData info) {
        resID = info.regionId;
        stopXListView();
        setWorkOrderData(info.result);
        if (wlist.getCount() > 1) {
            bg.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            bg.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有已完成的任务");
        }
    }

    @Override
    public void onLoginFailed() {
        stopXListView();
        DLog.i("onLoginFailed");
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
        currentPage = 1;
        listState = Api.LISTSTATEREFRESH;
        this.hisName = hisName;
        this.samplyName = target;
        presenter.getHistory(currentPage, hisName,samplyName, resID, location);
    }

    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        loading.dismiss();
        location = amapLocation.getAddress();
        presenter.getHistory(currentPage, hisName,samplyName, resID, amapLocation.getAddress());
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

    @Override
    public void onRefresh(View v) {
        currentPage = 1;
        listState = Api.LISTSTATEREFRESH;
        presenter.getHistory(currentPage, hisName,samplyName, resID, location);
    }

    @Override
    public void onLoadMore(View v) {
        if (totalSize < PageSize) {
            ToastApp.showToast("已经是最后一页");
            wlist.removeFooterView(wlist.mFooterView);
        } else {
            currentPage++;
            listState = Api.LISTSTATELOADMORE;
            presenter.getHistory(currentPage, hisName,samplyName, resID, location);
        }
    }

    // xLisView 停止
    private void stopXListView() {
        wlist.stopRefresh();
        wlist.stopLoadMore();
    }

    protected void setWorkOrderData(List<PlannningData.Scheme> orderBeans) {
        wlist.removeFooterView(wlist.mFooterView);
        if (orderBeans.size() > PageSize - 1) {
            wlist.addFooterView(wlist.mFooterView);
        }
        switch (listState) {
            case Api.LISTSTATEFIRST:
                data.clear();
                data.addAll(orderBeans);
                adapter.setData(data);
                wlist.smoothScrollToPosition(1);
                break;
            case Api.LISTSTATEREFRESH:
                data.clear();
                data.addAll(orderBeans);
                adapter.setData(data);
                break;
            case Api.LISTSTATELOADMORE:
                data.addAll(orderBeans);
                adapter.setData(data);
                break;
        }
        totalSize = orderBeans.size();
    }

}
